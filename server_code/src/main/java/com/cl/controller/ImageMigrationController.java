package com.cl.controller;

import com.cl.utils.ImageUploadTool;
import com.cl.utils.ImageMigrationUtil;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 图片迁移控制器
 * 提供图片从本地存储迁移到云存储的API接口
 */
@RestController
@RequestMapping("/admin/migration")
public class ImageMigrationController {

    @Autowired
    private ImageUploadTool imageUploadTool;

    @Autowired
    private ImageMigrationUtil imageMigrationUtil;

    /**
     * 批量上传本地图片到云存储
     * @return 上传结果
     */
    @PostMapping("/upload-images")
    public R uploadImages() {
        try {
            ImageUploadTool.UploadResult result = imageUploadTool.batchUploadImages();

            if (result.getErrorMessage() != null) {
                return R.error(result.getErrorMessage());
            }

            // 打印统计信息
            result.printSummary();

            return R.ok().put("data", result)
                    .put("message", String.format("上传完成！总计 %d 个文件，成功 %d 个，失败 %d 个",
                            result.getTotalFiles(), result.getSuccessCount(), result.getFailedCount()));

        } catch (Exception e) {
            e.printStackTrace();
            return R.error("批量上传过程中发生异常: " + e.getMessage());
        }
    }

    /**
     * 迁移数据库中的图片路径
     * @return 迁移结果
     */
    @PostMapping("/migrate-database")
    public R migrateDatabase() {
        try {
            imageMigrationUtil.migrateAllImages();
            return R.ok("数据库图片路径迁移完成");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("数据库迁移过程中发生异常: " + e.getMessage());
        }
    }

    /**
     * 验证迁移结果
     * @return 验证结果
     */
    @GetMapping("/verify")
    public R verifyMigration() {
        try {
            imageMigrationUtil.verifyMigration();
            return R.ok("迁移验证完成，请查看控制台输出");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("验证过程中发生异常: " + e.getMessage());
        }
    }

    /**
     * 完整的迁移流程
     * 1. 上传本地图片到云存储
     * 2. 更新数据库中的图片路径
     * 3. 验证迁移结果
     * @return 迁移结果
     */
    @PostMapping("/full-migration")
    public R fullMigration() {
        try {
            // 步骤1: 上传图片
            System.out.println("=== 开始步骤1: 上传本地图片到云存储 ===");
            ImageUploadTool.UploadResult uploadResult = imageUploadTool.batchUploadImages();

            if (uploadResult.getErrorMessage() != null) {
                return R.error("图片上传失败: " + uploadResult.getErrorMessage());
            }

            uploadResult.printSummary();

            // 步骤2: 迁移数据库
            System.out.println("=== 开始步骤2: 更新数据库中的图片路径 ===");
            imageMigrationUtil.migrateAllImages();

            // 步骤3: 验证结果
            System.out.println("=== 开始步骤3: 验证迁移结果 ===");
            imageMigrationUtil.verifyMigration();

            String message = String.format(
                "完整迁移流程执行完成！\n" +
                "图片上传: 总计 %d 个文件，成功 %d 个，失败 %d 个\n" +
                "数据库迁移: 已完成\n" +
                "验证结果: 请查看控制台输出",
                uploadResult.getTotalFiles(),
                uploadResult.getSuccessCount(),
                uploadResult.getFailedCount()
            );

            return R.ok().put("uploadResult", uploadResult).put("message", message);

        } catch (Exception e) {
            e.printStackTrace();
            return R.error("完整迁移过程中发生异常: " + e.getMessage());
        }
    }

    /**
     * 获取迁移状态信息
     * @return 状态信息
     */
    @GetMapping("/status")
    public R getStatus() {
        try {
            // 这里可以添加检查云存储连接、本地文件目录等状态的逻辑
            return R.ok("迁移工具状态正常");
        } catch (Exception e) {
            return R.error("获取状态失败: " + e.getMessage());
        }
    }
}