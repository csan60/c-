# Deployment Guide

本文档描述如何将应用部署到不同平台。

---

## 环境准备

### 必需工具

1. **HBuilderX**  
   - 下载地址: https://www.dcloud.io/hbuilderx.html
   - 建议使用最新稳定版

2. **Node.js**  
   - 版本要求: >= 14.0.0
   - 下载地址: https://nodejs.org/

3. **微信开发者工具**（如需发布小程序）  
   - 下载地址: https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html

---

## 部署前准备

### 1. 安装依赖

```bash
npm install
```

### 2. 配置环境变量

复制 `.env.example` 到 `.env.production`，并修改为生产环境配置：

```bash
cp .env.example .env.production
```

编辑 `.env.production`：

```bash
VUE_APP_BASE_API=https://your-production-api.com/api/
NODE_ENV=production
VUE_APP_NAME=College Student Mental Health Assessment System
VUE_APP_TIMEOUT=30000
VUE_APP_ENABLE_MOCK=false
VUE_APP_LOG_LEVEL=warn
```

### 3. 修改 manifest.json

根据目标平台修改相应配置：

#### 微信小程序
```json
{
  "mp-weixin": {
    "appid": "你的小程序AppID",
    "setting": {
      "urlCheck": false
    }
  }
}
```

#### H5
```json
{
  "h5": {
    "router": {
      "base": "/your-project-path/"
    }
  }
}
```

### 4. 代码检查

运行代码检查，确保没有错误：

```bash
npm run lint
```

---

## H5 部署

### 方法一：使用 HBuilderX

1. 打开 HBuilderX
2. 选择 `发行` -> `网站-H5手机版`
3. 选择发行目录（默认为 `unpackage/dist/build/h5`）
4. 点击发行

### 方法二：命令行构建

```bash
npm run build:h5
```

构建产物位于 `unpackage/dist/build/h5/`

### 部署到服务器

#### 使用 Nginx

1. 将构建产物上传到服务器
2. 配置 Nginx：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        root /var/www/html/h5;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # API 代理（可选）
    location /api/ {
        proxy_pass https://your-backend-api.com/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

3. 重启 Nginx：

```bash
sudo systemctl restart nginx
```

#### 使用 CDN

1. 将 `static/` 目录下的静态资源上传到 CDN
2. 修改 `manifest.json` 中的资源路径

---

## 微信小程序部署

### 1. HBuilderX 构建

1. 打开 HBuilderX
2. 选择 `发行` -> `小程序-微信`
3. 填写小程序名称、AppID
4. 点击发行

### 2. 微信开发者工具

1. 打开微信开发者工具
2. 导入项目（选择 `unpackage/dist/build/mp-weixin` 目录）
3. 预览和测试
4. 点击 `上传` 按钮上传代码

### 3. 微信公众平台

1. 登录 [微信公众平台](https://mp.weixin.qq.com/)
2. 进入 `开发管理` -> `开发版本`
3. 选择刚上传的版本，提交审核
4. 审核通过后，发布上线

### 注意事项

- 确保服务器域名已在 `开发设置` -> `服务器域名` 中配置
- 配置业务域名（如有需要）
- 设置隐私政策和用户信息收集说明
- 配置所需的权限（如定位、相机等）

---

## App 部署

### Android

#### 1. 云打包（推荐）

1. 打开 HBuilderX
2. 选择 `发行` -> `原生App-云打包`
3. 选择 Android 平台
4. 配置应用信息：
   - 应用名称
   - 应用图标
   - 启动图
   - 版本号
5. 选择证书（使用 DCloud 公共证书或自有证书）
6. 点击打包

#### 2. 本地打包

需要安装 Android SDK 和配置环境，参考官方文档：
https://uniapp.dcloud.io/tutorial/run-nativeApp

#### 3. 应用市场上架

将打包好的 APK 上传到各大应用市场：
- 华为应用市场
- 小米应用商店
- OPPO 软件商店
- vivo 应用商店
- 腾讯应用宝
- 百度手机助手
- 360 手机助手

### iOS

#### 1. 准备工作

- 注册 Apple Developer 账号
- 创建 App ID
- 创建开发/发布证书
- 创建 Provisioning Profile

#### 2. 云打包

1. 打开 HBuilderX
2. 选择 `发行` -> `原生App-云打包`
3. 选择 iOS 平台
4. 上传证书和描述文件
5. 配置应用信息
6. 点击打包

#### 3. App Store 上架

1. 在 [App Store Connect](https://appstoreconnect.apple.com/) 创建应用
2. 使用 Xcode 上传 IPA 文件
3. 填写应用信息、截图等
4. 提交审核

---

## 持续集成/部署（CI/CD）

### GitHub Actions 示例

创建 `.github/workflows/deploy.yml`：

```yaml
name: Deploy

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Setup Node.js
      uses: actions/setup-node@v2
      with:
        node-version: '14'
    
    - name: Install dependencies
      run: npm install
    
    - name: Build H5
      run: npm run build:h5
    
    - name: Deploy to Server
      uses: easingthemes/ssh-deploy@v2.1.5
      env:
        SSH_PRIVATE_KEY: ${{ secrets.SSH_PRIVATE_KEY }}
        REMOTE_HOST: ${{ secrets.REMOTE_HOST }}
        REMOTE_USER: ${{ secrets.REMOTE_USER }}
        TARGET: /var/www/html/h5
        SOURCE: unpackage/dist/build/h5/
```

---

## 版本管理

### 语义化版本

遵循 [Semantic Versioning](https://semver.org/) 规范：

- **主版本号（Major）**: 不兼容的 API 修改
- **次版本号（Minor）**: 向下兼容的功能新增
- **修订号（Patch）**: 向下兼容的问题修正

示例：`1.2.3`

### 更新版本号

修改以下文件：
1. `package.json` - `version` 字段
2. `manifest.json` - `versionName` 和 `versionCode`

---

## 性能优化建议

### H5 优化

1. **启用 Gzip 压缩**
2. **配置浏览器缓存**
3. **使用 CDN 加速静态资源**
4. **图片懒加载**
5. **代码分割**

### 小程序优化

1. **分包加载**（在 pages.json 中配置 subPackages）
2. **图片优化**（使用 webp 格式，压缩图片）
3. **减少 setData 调用频率**
4. **使用组件化开发**

### App 优化

1. **减少包体积**（移除未使用的库和资源）
2. **启动优化**（延迟加载非必需资源）
3. **内存优化**（及时释放不用的资源）

---

## 监控与日志

### 配置日志收集

推荐使用第三方日志服务：
- **Sentry**：错误追踪
- **友盟统计**：用户行为分析
- **腾讯移动分析**

### 性能监控

- 页面加载时间
- API 响应时间
- 错误率
- 崩溃率

---

## 回滚策略

### H5

1. 保留多个版本的构建产物
2. 通过修改 Nginx 配置快速切换版本
3. 使用蓝绿部署或金丝雀发布

### 小程序

1. 在微信公众平台保留历史版本
2. 如出现问题，可以回退到上一个版本
3. 紧急修复后重新提交审核

### App

1. 强制更新机制（严重 Bug）
2. 提供旧版本下载（兼容性问题）
3. 应用市场可以下架问题版本

---

## 安全检查清单

- [ ] 生产环境 API 地址已配置 HTTPS
- [ ] 已移除所有 console.log 和调试代码
- [ ] 已配置正确的跨域策略
- [ ] Token 过期时间合理设置
- [ ] 敏感信息已从前端代码中移除
- [ ] 已配置内容安全策略（CSP）
- [ ] 已测试所有核心功能
- [ ] 已进行压力测试

---

## 常见问题

### Q: H5 部署后页面空白？
A: 检查 `manifest.json` 中的 `router.base` 路径是否正确。

### Q: 小程序无法访问后端 API？
A: 确保服务器域名已在微信公众平台配置，且使用 HTTPS。

### Q: App 打包失败？
A: 检查证书是否有效，Bundle ID 是否正确配置。

### Q: 版本更新后用户看到的还是旧版本？
A: 清除浏览器缓存，或在文件名中加入版本号/hash。

---

## 联系支持

如在部署过程中遇到问题，请：

1. 查看项目文档
2. 搜索相关 Issue
3. 联系项目维护者

---

祝部署顺利！🚀
