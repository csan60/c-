# AI API Key 设置完整指南

## 📌 快速开始

### 方法一：图形界面设置（推荐新手）

1. **启动程序**
   - 双击运行注入器程序
   - 输入验证码（`sjyssr.net` 或 `aaa`）进入主界面

2. **打开AI配置窗口**
   - 点击界面上的 **"AI配置"** 按钮
   - 会弹出一个新的配置窗口

3. **填写配置信息**
   
   **API地址** (Endpoint):
   ```
   OpenAI:    https://api.openai.com/v1/chat/completions
   DeepSeek:  https://api.deepseek.com/v1/chat/completions
   Kimi:      https://api.moonshot.cn/v1/chat/completions
   ```
   
   **API Key** (密钥):
   ```
   粘贴您从服务商获得的API密钥
   格式示例: sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```
   
   **模型** (Model):
   ```
   OpenAI:    gpt-3.5-turbo  (快速、便宜)
              gpt-4          (高质量、贵)
   DeepSeek:  deepseek-chat  (性价比高)
   Kimi:      moonshot-v1-8k
   ```

4. **保存并测试**
   - 点击 **"保存配置"** 按钮
   - 点击 **"测试连接"** 验证配置
   - 如果看到"OK"或成功提示，说明配置正确

---

### 方法二：手动编辑配置文件（适合高级用户）

1. **找到配置文件**
   - 在程序exe同目录下找到 `ai_config.ini` 文件
   - 如果没有，运行一次程序会自动创建

2. **编辑配置文件**
   ```ini
   [AI]
   Endpoint=https://api.openai.com/v1/chat/completions
   ApiKey=sk-你的实际API密钥
   Model=gpt-3.5-turbo
   
   [Settings]
   AutoHide=1
   
   [Injector]
   DllPath=
   ```

3. **保存并重启程序**

---

## 🔑 如何获取API Key

### 1️⃣ OpenAI (官方，需海外环境)

**步骤：**
1. 访问 https://platform.openai.com/signup
2. 注册/登录账号（需国外手机号）
3. 访问 https://platform.openai.com/api-keys
4. 点击 **"Create new secret key"**
5. 给密钥起个名字（如：Injector）
6. **重要**：立即复制密钥（只显示一次！）
7. 粘贴到配置窗口的"API Key"框中

**价格参考：**
- gpt-3.5-turbo: $0.0015/1K tokens (约0.5元/千tokens)
- gpt-4: $0.03/1K tokens (约20元/千tokens)

**注意事项：**
- 需要国际信用卡充值
- 国内需要科学上网
- 有免费额度（新用户$5，3个月有效）

---

### 2️⃣ DeepSeek (国产，推荐！)

**步骤：**
1. 访问 https://platform.deepseek.com
2. 注册账号（支持国内手机号）
3. 点击左侧 **"API keys"** 菜单
4. 点击 **"创建API Key"**
5. 复制生成的密钥
6. 粘贴到配置中

**配置示例：**
```
API地址: https://api.deepseek.com/v1/chat/completions
API Key: sk-xxxxxxxxxxxxxxxx
模型: deepseek-chat
```

**优势：**
- ✅ 国内可直接访问
- ✅ 价格便宜（约OpenAI的1/10）
- ✅ 中文理解能力强
- ✅ 新用户有免费额度
- ✅ 支持支付宝充值

**价格参考：**
- deepseek-chat: ¥0.001/1K tokens (非常便宜)

---

### 3️⃣ Kimi (月之暗面，国产)

**步骤：**
1. 访问 https://platform.moonshot.cn
2. 注册登录（支持国内手机号）
3. 进入 **"API Key管理"**
4. 创建新的API Key
5. 复制密钥

**配置示例：**
```
API地址: https://api.moonshot.cn/v1/chat/completions
API Key: sk-xxxxxxxxxxxxxxxx
模型: moonshot-v1-8k
```

**特点：**
- 长上下文支持
- 中文优化
- 国内访问快

---

### 4️⃣ 其他兼容服务

#### 通义千问 (阿里云)
- 网址: https://dashscope.aliyun.com
- 需要转换为OpenAI格式

#### 本地部署（Ollama等）
```
API地址: http://localhost:11434/v1/chat/completions
API Key: 随便填（本地不需要）
模型: llama2, mistral等
```

---

## ✅ 配置验证

### 测试步骤
1. 完成配置后，点击 **"测试连接"** 按钮
2. 等待几秒（正在向API发送测试请求）
3. 查看结果：

**成功示例：**
```
✅ 测试结果：OK
或
✅ 测试结果：你好，我已准备好帮助你...
```

**失败示例：**
```
❌ 错误：HTTP 401 - Incorrect API key
❌ 错误：HTTP 429 - Rate limit exceeded
❌ 错误：无法连接到服务器
```

### 常见错误及解决

#### 错误1: "Incorrect API key"
**原因**：API Key错误
**解决**：
- 检查是否完整复制了API Key
- 确认没有多余的空格
- 重新生成新的API Key

#### 错误2: "Rate limit exceeded"
**原因**：请求过于频繁或超出配额
**解决**：
- 等待几分钟后重试
- 检查账户余额
- 升级付费计划

#### 错误3: "无法连接到服务器"
**原因**：网络问题
**解决**：
- 检查网络连接
- 如使用OpenAI，需要科学上网
- 尝试切换到国内服务（DeepSeek、Kimi）

#### 错误4: "Invalid model"
**原因**：模型名称错误
**解决**：
- 确认模型名称拼写正确
- 查看服务商文档确认支持的模型

---

## 📝 完整配置示例

### OpenAI配置（海外用户）
```ini
[AI]
Endpoint=https://api.openai.com/v1/chat/completions
ApiKey=sk-proj-abcdefghijklmnopqrstuvwxyz1234567890
Model=gpt-3.5-turbo

[Settings]
AutoHide=1

[Injector]
DllPath=C:\Tools\winmm.dll
```

### DeepSeek配置（国内用户推荐）
```ini
[AI]
Endpoint=https://api.deepseek.com/v1/chat/completions
ApiKey=sk-1234567890abcdefghijklmnopqrstuvwxyz
Model=deepseek-chat

[Settings]
AutoHide=1

[Injector]
DllPath=D:\Injector\winmm.dll
```

### Kimi配置（国内用户）
```ini
[AI]
Endpoint=https://api.moonshot.cn/v1/chat/completions
ApiKey=sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
Model=moonshot-v1-8k

[Settings]
AutoHide=1

[Injector]
DllPath=
```

---

## 🎯 使用建议

### 推荐配置

**预算充足 + 海外环境：**
- 使用 OpenAI GPT-4
- 准确度最高
- 响应速度快

**预算有限 + 海外环境：**
- 使用 OpenAI GPT-3.5-turbo
- 性价比高
- 速度快

**国内用户（强烈推荐）：**
- 使用 DeepSeek
- 无需科学上网
- 价格便宜
- 中文理解好

**长文本需求：**
- 使用 Kimi moonshot-v1-8k
- 支持8K上下文

### 模型选择建议

| 模型 | 速度 | 准确度 | 价格 | 适用场景 |
|------|------|--------|------|----------|
| gpt-3.5-turbo | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | 💰💰 | 日常答题 |
| gpt-4 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | 💰💰💰💰💰 | 复杂问题 |
| deepseek-chat | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | 💰 | 高性价比 |
| moonshot-v1-8k | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | 💰💰 | 长文本 |

---

## 💡 高级技巧

### 1. 多API配置切换
如果有多个API Key，可以备份配置文件：
```
ai_config_openai.ini
ai_config_deepseek.ini
ai_config_kimi.ini
```
需要时重命名为 `ai_config.ini`

### 2. 代理设置
如果需要代理访问，修改系统代理设置：
- Windows设置 > 网络和Internet > 代理
- 或使用代理软件（如Clash、V2Ray）

### 3. 节省费用
- 优先使用 gpt-3.5-turbo 而非 gpt-4
- 使用国内服务（DeepSeek）
- 控制问题长度
- 避免频繁测试

---

## ⚠️ 安全警告

### 保护您的API Key

❌ **不要做：**
- 将API Key分享给他人
- 将配置文件上传到公开平台
- 在代码中硬编码API Key
- 使用公共电脑后不删除配置

✅ **应该做：**
- 定期更换API Key
- 监控API使用情况
- 设置使用限额
- 配置文件设为只读权限

### 费用控制

**OpenAI控制台：**
1. 访问 https://platform.openai.com/account/billing/limits
2. 设置 Monthly budget（月度预算）
3. 开启邮件通知

**DeepSeek控制台：**
1. 访问账户设置
2. 设置单日/单月限额
3. 余额不足时自动停止

---

## 🆘 故障排除

### 问题：配置保存后无效
**解决：**
1. 确认 `ai_config.ini` 文件可写
2. 以管理员权限运行程序
3. 检查文件路径是否正确

### 问题：中文乱码
**解决：**
1. 确保配置文件编码为 UTF-8
2. 使用记事本另存为，选择UTF-8编码

### 问题：API响应超时
**解决：**
1. 检查网络连接
2. 尝试切换服务商
3. 使用国内服务

---

## 📞 获取帮助

如果遇到问题：
1. 查看程序同目录下的日志文件 `Injector_Log_*.txt`
2. 访问作者网站：http://sjyssr.net
3. 查看GitHub项目文档

---

**最后更新**: 2024-12-01  
**版本**: v3.0
