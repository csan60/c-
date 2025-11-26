-- 数据库迁移脚本：将本地图片路径迁移到云存储URL
-- 执行前请备份数据库！
-- 请根据实际的云存储域名修改 'https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com'

-- 1. 更新config表中的轮播图配置
-- 假设轮播图存储在config表中，name字段为'swiperList'或类似
UPDATE config 
SET value = REPLACE(value, '"img":"', '"img":"https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/uploads/')
WHERE name LIKE '%swiper%' OR name LIKE '%banner%'
AND value LIKE '%"img":"%'
AND value NOT LIKE '%http%';

-- 2. 更新config表中的头像文件配置
UPDATE config 
SET value = CONCAT('https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/uploads/', value)
WHERE name = 'faceFile'
AND value IS NOT NULL 
AND value != ''
AND value NOT LIKE 'http%';

-- 3. 更新用户表中的头像字段（假设表名为users，字段名为avatar或touxiang）
-- 请根据实际表名和字段名修改
UPDATE users 
SET avatar = CONCAT('https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/uploads/', avatar)
WHERE avatar IS NOT NULL 
AND avatar != ''
AND avatar NOT LIKE 'http%';

-- 如果头像字段名为touxiang
UPDATE users 
SET touxiang = CONCAT('https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/uploads/', touxiang)
WHERE touxiang IS NOT NULL 
AND touxiang != ''
AND touxiang NOT LIKE 'http%';

-- 4. 更新商品表中的图片字段（假设表名为goods，字段名为picture）
-- 请根据实际表名和字段名修改
UPDATE goods 
SET picture = CONCAT('https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/uploads/', picture)
WHERE picture IS NOT NULL 
AND picture != ''
AND picture NOT LIKE 'http%';

-- 5. 更新新闻/文章表中的图片字段（假设表名为news，字段名为picture）
-- 请根据实际表名和字段名修改
UPDATE news 
SET picture = CONCAT('https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/uploads/', picture)
WHERE picture IS NOT NULL 
AND picture != ''
AND picture NOT LIKE 'http%';

-- 6. 更新其他可能包含图片的表
-- 请根据实际业务需求添加更多表的更新语句
-- 示例：
-- UPDATE table_name 
-- SET image_field = CONCAT('https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com/uploads/', image_field)
-- WHERE image_field IS NOT NULL 
-- AND image_field != ''
-- AND image_field NOT LIKE 'http%';

-- 7. 查询验证脚本（执行迁移后运行以验证结果）
-- 检查config表
SELECT name, value FROM config WHERE name LIKE '%swiper%' OR name LIKE '%banner%' OR name = 'faceFile';

-- 检查用户表
SELECT id, avatar, touxiang FROM users WHERE avatar LIKE 'http%' OR touxiang LIKE 'http%' LIMIT 10;

-- 检查商品表
SELECT id, picture FROM goods WHERE picture LIKE 'http%' LIMIT 10;

-- 检查新闻表
SELECT id, picture FROM news WHERE picture LIKE 'http%' LIMIT 10;

-- 注意事项：
-- 1. 执行前请务必备份数据库
-- 2. 请将 'https://your-bucket-name.oss-cn-hangzhou.aliyuncs.com' 替换为实际的云存储域名
-- 3. 请根据实际的表名和字段名修改SQL语句
-- 4. 建议先在测试环境执行并验证结果
-- 5. 执行后请验证前端页面图片显示是否正常