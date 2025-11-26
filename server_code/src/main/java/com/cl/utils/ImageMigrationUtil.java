package com.cl.utils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.entity.ConfigEntity;
import com.cl.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 图片迁移工具类
 * 用于将本地存储的图片路径迁移到云存储URL
 */
@Component
public class ImageMigrationUtil {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ConfigService configService;
    
    @Value("${aliyun.oss.domain:https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com}")
    private String ossDomain;
    
    /**
     * 迁移所有图片路径到云存储
     */
    public void migrateAllImages() {
        System.out.println("开始迁移图片到云存储...");
        
        // 迁移config表中的配置
        migrateConfigImages();
        
        // 迁移用户头像
        migrateUserAvatars();
        
        // 迁移其他表的图片字段
        migrateOtherTableImages();
        
        System.out.println("图片迁移完成！");
    }
    
    /**
     * 迁移config表中的图片配置
     */
    public void migrateConfigImages() {
        System.out.println("迁移config表中的图片配置...");
        
        // 获取所有可能包含图片的配置
        List<ConfigEntity> configs = configService.selectList(
            new EntityWrapper<ConfigEntity>()
                .like("name", "swiper")
                .or()
                .like("name", "banner")
                .or()
                .eq("name", "faceFile")
        );
        
        for (ConfigEntity config : configs) {
            String value = config.getValue();
            if (value != null && !value.isEmpty() && !value.contains("http")) {
                String newValue;
                if (config.getName().contains("swiper") || config.getName().contains("banner")) {
                    // 处理JSON格式的轮播图配置
                    newValue = value.replaceAll(
                        "\"img\":\"([^\"]+)\"", 
                        "\"img\":\"" + ossDomain + "/uploads/$1\""
                    );
                } else {
                    // 处理单个文件名
                    newValue = ossDomain + "/uploads/" + value;
                }
                
                config.setValue(newValue);
                configService.updateById(config);
                System.out.println("更新配置: " + config.getName() + " -> " + newValue);
            }
        }
    }
    
    /**
     * 迁移用户头像
     */
    public void migrateUserAvatars() {
        System.out.println("迁移用户头像...");
        
        // 检查表是否存在avatar字段
        if (checkColumnExists("users", "avatar")) {
            String sql = "UPDATE users SET avatar = CONCAT(?, avatar) " +
                        "WHERE avatar IS NOT NULL AND avatar != '' AND avatar NOT LIKE 'http%'";
            int count = jdbcTemplate.update(sql, ossDomain + "/uploads/");
            System.out.println("更新了 " + count + " 个用户avatar字段");
        }
        
        // 检查表是否存在touxiang字段
        if (checkColumnExists("users", "touxiang")) {
            String sql = "UPDATE users SET touxiang = CONCAT(?, touxiang) " +
                        "WHERE touxiang IS NOT NULL AND touxiang != '' AND touxiang NOT LIKE 'http%'";
            int count = jdbcTemplate.update(sql, ossDomain + "/uploads/");
            System.out.println("更新了 " + count + " 个用户touxiang字段");
        }
    }
    
    /**
     * 迁移其他表的图片字段
     */
    public void migrateOtherTableImages() {
        System.out.println("迁移其他表的图片字段...");
        
        // 定义需要迁移的表和字段
        String[][] tableFields = {
            {"goods", "picture"},
            {"news", "picture"},
            {"articles", "image"},
            {"products", "image"},
            // 可以根据实际情况添加更多表和字段
        };
        
        for (String[] tableField : tableFields) {
            String tableName = tableField[0];
            String fieldName = tableField[1];
            
            if (checkTableExists(tableName) && checkColumnExists(tableName, fieldName)) {
                String sql = String.format(
                    "UPDATE %s SET %s = CONCAT(?, %s) " +
                    "WHERE %s IS NOT NULL AND %s != '' AND %s NOT LIKE 'http%%'",
                    tableName, fieldName, fieldName, fieldName, fieldName, fieldName
                );
                
                try {
                    int count = jdbcTemplate.update(sql, ossDomain + "/uploads/");
                    System.out.println("更新了表 " + tableName + " 的 " + count + " 条记录");
                } catch (Exception e) {
                    System.err.println("更新表 " + tableName + " 失败: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * 检查表是否存在
     */
    private boolean checkTableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE() AND table_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查字段是否存在
     */
    private boolean checkColumnExists(String tableName, String columnName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.columns " +
                        "WHERE table_schema = DATABASE() AND table_name = ? AND column_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 验证迁移结果
     */
    public void verifyMigration() {
        System.out.println("验证迁移结果...");
        
        // 检查config表
        List<Map<String, Object>> configs = jdbcTemplate.queryForList(
            "SELECT name, value FROM config WHERE name LIKE '%swiper%' OR name LIKE '%banner%' OR name = 'faceFile'"
        );
        System.out.println("Config表迁移结果:");
        for (Map<String, Object> config : configs) {
            System.out.println("  " + config.get("name") + ": " + config.get("value"));
        }
        
        // 检查用户表
        if (checkTableExists("users")) {
            List<Map<String, Object>> users = jdbcTemplate.queryForList(
                "SELECT id, avatar, touxiang FROM users WHERE (avatar LIKE 'http%' OR touxiang LIKE 'http%') LIMIT 5"
            );
            System.out.println("Users表迁移结果（前5条）:");
            for (Map<String, Object> user : users) {
                System.out.println("  用户ID: " + user.get("id") + ", avatar: " + user.get("avatar") + ", touxiang: " + user.get("touxiang"));
            }
        }
    }
}