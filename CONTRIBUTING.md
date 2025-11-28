# Contributing Guidelines

感谢您对本项目的关注！无论您是提交 Bug、新增功能还是改进文档，都欢迎您的贡献。

---

## 🐛 如何报告问题

如果您发现了 Bug 或有改进建议，请先检查是否已存在相关 Issue。如果没有，请创建新的 Issue 并提供以下信息：

1. **问题描述**：清晰描述问题现象
2. **复现步骤**：详细的复现步骤
3. **期望行为**：您期望的正确行为
4. **实际行为**：实际发生的情况
5. **环境信息**：
   - 操作系统
   - uni-app 版本
   - HBuilderX 版本
   - 浏览器或小程序开发工具版本
   - 其他相关信息
6. **截图或日志**：如有必要，请附上截图或错误日志

---

## 🚀 提交代码

### 1. Fork 项目

点击项目页面右上角的 Fork 按钮，将项目 Fork 到您的 GitHub 账号下。

### 2. 克隆仓库

```bash
git clone https://github.com/YOUR_USERNAME/PROJECT_NAME.git
cd PROJECT_NAME
```

### 3. 创建分支

基于 `main` 分支创建新的功能分支：

```bash
git checkout -b feature/your-feature-name
# 或者修复 Bug 的分支
git checkout -b fix/your-bug-fix
```

分支命名规范：
- `feature/xxx` - 新功能
- `fix/xxx` - Bug 修复
- `docs/xxx` - 文档更新
- `style/xxx` - 代码格式调整
- `refactor/xxx` - 代码重构
- `test/xxx` - 测试相关
- `chore/xxx` - 构建/工具相关

### 4. 进行开发

- 遵循项目的代码风格（使用 ESLint 和 Prettier）
- 确保代码通过 Lint 检查：`npm run lint`
- 如果修改了功能，请更新相关文档
- 提交信息要清晰明了

### 5. 提交代码

```bash
git add .
git commit -m "feat: add xxx feature"
```

提交信息格式（遵循 Conventional Commits）：
- `feat:` - 新功能
- `fix:` - Bug 修复
- `docs:` - 文档更新
- `style:` - 代码格式调整（不影响功能）
- `refactor:` - 重构代码
- `perf:` - 性能优化
- `test:` - 测试相关
- `chore:` - 构建/工具配置
- `revert:` - 回退提交

示例：
```
feat: add user authentication module
fix: resolve login page redirect issue
docs: update API documentation
```

### 6. 推送代码

```bash
git push origin feature/your-feature-name
```

### 7. 创建 Pull Request

1. 访问您 Fork 的项目页面
2. 点击 "New Pull Request" 按钮
3. 填写 PR 标题和描述，说明您的改动
4. 等待项目维护者 Review

---

## 📝 代码规范

### JavaScript/Vue 规范

- 使用 ES6+ 语法
- 使用 2 空格缩进
- 使用单引号
- 语句末尾加分号
- 遵循 ESLint 规则

### 命名规范

- **文件名**：小写字母，使用连字符分隔（kebab-case）
  - 例如：`user-info.vue`, `api-service.js`
- **组件名**：大驼峰命名（PascalCase）
  - 例如：`UserInfo`, `LoginForm`
- **变量/函数名**：小驼峰命名（camelCase）
  - 例如：`userName`, `getUserInfo()`
- **常量名**：全大写，下划线分隔
  - 例如：`MAX_COUNT`, `API_BASE_URL`

### 注释规范

- 函数和复杂逻辑必须添加注释
- 使用 JSDoc 格式注释函数
- 中英文注释均可，但保持一致性

示例：
```javascript
/**
 * 获取用户信息
 * @param {string} userId - 用户ID
 * @returns {Promise<Object>} 用户信息对象
 */
function getUserInfo(userId) {
  // 实现代码
}
```

---

## 🧪 测试

在提交 PR 之前，请确保：

1. 代码能够正常运行
2. 通过 ESLint 检查：`npm run lint`
3. 在主流平台测试（H5、微信小程序等）
4. 没有引入新的警告或错误

---

## 📖 文档

如果您的改动涉及到用户使用方式的变化，请同步更新：

- `README.md` - 项目说明文档
- API 文档（如有）
- 代码注释

---

## 💡 开发建议

1. **保持 PR 的大小适中**：一个 PR 只解决一个问题或添加一个功能
2. **及时响应 Review 意见**：维护者可能会提出修改建议
3. **保持代码整洁**：删除调试代码、console.log 等
4. **遵循项目架构**：保持与现有代码风格一致
5. **关注性能**：避免引入性能问题

---

## 🎯 优先级

我们特别欢迎以下方面的贡献：

1. Bug 修复
2. 性能优化
3. 文档完善
4. 测试覆盖
5. 代码重构（提高可维护性）
6. 新功能（需先讨论）

---

## ❓ 寻求帮助

如果您在开发过程中遇到问题，可以：

1. 查看项目文档
2. 搜索已有的 Issue
3. 在 Issue 中提问
4. 联系项目维护者

---

再次感谢您的贡献！🙏
