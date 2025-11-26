# 本地开发环境配置指南

本文档提供详细的本地开发环境搭建步骤，帮助开发者快速启动项目。

---

## 📋 前置要求

### 必需软件及版本

| 软件 | 版本要求 | 下载地址 | 用途 |
|-----|---------|---------|-----|
| **JDK** | 17 | [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) / [OpenJDK](https://adoptium.net/) | Java 后端运行环境 |
| **Maven** | 3.8+ | [官网](https://maven.apache.org/download.cgi) | Java 项目构建（或使用项目自带 mvnw） |
| **MySQL** | 8.0+ | [官网](https://dev.mysql.com/downloads/mysql/) | 关系型数据库 |
| **Redis** | 6.0+ | [官网](https://redis.io/download) | 缓存数据库 |
| **Python** | 3.9+ | [官网](https://www.python.org/downloads/) | Python 服务运行环境 |
| **Node.js** | 18+ | [官网](https://nodejs.org/) | Vue 前端开发环境 |
| **Git** | 2.x | [官网](https://git-scm.com/) | 版本控制 |
| **HBuilderX** | 3.x/4.x | [官网](https://www.dcloud.io/hbuilderx.html) | Uni-App 开发工具（可选） |

### 可选软件

- **CUDA Toolkit** 11.8+ (GPU 加速 AI 模型)
- **Docker** (容器化运行数据库)
- **Postman** / **Apifox** (API 调试)
- **Redis Desktop Manager** (Redis 可视化)
- **Navicat** / **MySQL Workbench** (MySQL 可视化)

---

## 🚀 快速启动

### 方案一：启动 Uni-App 心理健康平台（根目录）

#### 步骤 1: 使用 HBuilderX 启动（推荐）

1. **安装 HBuilderX**
   - 下载并安装 [HBuilderX](https://www.dcloud.io/hbuilderx.html)
   - 建议下载"标准版"即可

2. **打开项目**
   - 启动 HBuilderX
   - 菜单：`文件` → `打开目录` → 选择本项目根目录

3. **配置后端地址**
   - 打开 `api/base.js`
   - 修改 API 基础地址：
   ```javascript
   // 开发环境
   const BASE_URL = 'http://localhost:8080'
   // 或你的实际后端地址
   ```

4. **运行项目**
   - 点击工具栏"运行"按钮
   - 选择运行平台：
     - **H5**: 运行到浏览器 → Chrome
     - **微信小程序**: 运行到小程序模拟器 → 微信开发者工具（需先安装）
     - **App**: 运行到手机或模拟器

5. **微信小程序配置（如需）**
   - 安装[微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
   - 在 HBuilderX 中配置微信开发者工具路径：
     - `工具` → `设置` → `运行配置` → `小程序运行配置`
   - 首次运行会自动打开微信开发者工具

#### 步骤 2: 使用命令行启动（可选）

```bash
# 如果项目有 package.json，可以使用 CLI
npm install -g @dcloudio/uvm
uvm

# 运行到 H5
npm run dev:h5

# 运行到微信小程序
npm run dev:mp-weixin
```

---

### 方案二：启动 ITAP 智能教学助手平台

完整启动需要按顺序启动 4 个服务。

#### 步骤 1: 安装和启动 MySQL

**使用系统服务安装（Linux）:**

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql

# CentOS/RHEL
sudo yum install mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld
```

**使用 Docker 安装（推荐）:**

```bash
# 拉取镜像并运行
docker run -d \
  --name mysql-itap \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e MYSQL_DATABASE=itap \
  -v mysql-data:/var/lib/mysql \
  mysql:8.0

# 查看运行状态
docker ps | grep mysql-itap

# 查看日志
docker logs mysql-itap
```

**创建数据库:**

```bash
# 登录 MySQL
mysql -u root -p
# 输入密码: 123456

# 创建数据库
CREATE DATABASE IF NOT EXISTS itap 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

# 查看数据库
SHOW DATABASES;

# 退出
EXIT;
```

**导入初始化 SQL（如有提供）:**

```bash
# 找到项目中的 SQL 文件（可能在 backend/src/main/resources/ 或单独的 sql/ 目录）
mysql -u root -p itap < /path/to/init.sql
```

#### 步骤 2: 安装和启动 Redis

**使用系统服务安装（Linux）:**

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server

# CentOS/RHEL
sudo yum install redis
sudo systemctl start redis
sudo systemctl enable redis

# 测试连接
redis-cli ping
# 应返回: PONG
```

**使用 Docker 安装（推荐）:**

```bash
# 拉取镜像并运行
docker run -d \
  --name redis-itap \
  -p 6379:6379 \
  redis:6-alpine

# 测试连接
docker exec -it redis-itap redis-cli ping
# 应返回: PONG
```

**Windows 安装:**

```powershell
# 使用 Chocolatey
choco install redis-64

# 或下载 Windows 版本
# https://github.com/tporadowski/redis/releases

# 启动服务
redis-server
```

#### 步骤 3: 启动 Python AI 模型服务

```bash
cd project20250624gpp03-main/backend-python

# 创建虚拟环境（推荐）
python3 -m venv venv

# 激活虚拟环境
# Linux/Mac:
source venv/bin/activate
# Windows:
# venv\Scripts\activate

# 升级 pip
pip install --upgrade pip setuptools wheel

# 安装依赖
pip install -r requirements.txt

# 如果安装失败或速度慢，使用国内镜像
pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
```

**配置模型路径（重要）:**

编辑 `main.py` 文件，找到 `load_model()` 函数：

```python
def load_model():
    curl_command = [
        "curl", "-X", "POST", "http://127.0.0.1:8001/switch-model",
        "-H", "Content-Type: application/json",
        "-d", '''{
            "model": "/path/to/your/RWKV-model.pth",  # ← 修改为实际模型路径
            "strategy": "cuda fp16",  # CPU 环境改为 "cpu fp32"
            "tokenizer": "", 
            "customCuda": true, 
            "deploy": false
        }'''
    ]
    subprocess.run(curl_command)
```

**处理模型文件（两种方案）:**

**方案 A: 下载模型（生产环境）**
```bash
# 下载 RWKV 模型（需要较大磁盘空间）
# 模型下载地址: https://huggingface.co/BlinkDL/rwkv-4-world
# 或根据项目文档提供的模型链接下载

# 下载后修改 main.py 中的模型路径
```

**方案 B: 暂时跳过模型加载（开发调试）**
```python
# 在 main.py 的最后部分注释掉模型加载
if __name__ == "__main__":
    os.environ["RWKV_RUNNER_PARAMS"] = " ".join(sys.argv[1:])
    print("--- %s seconds ---" % (time.time() - start_time))
    
    # 注释掉以下代码块
    # import threading
    # def run_server():
    #     uvicorn.run("main:app", port=args.port, host=args.host, workers=1)
    # server_thread = threading.Thread(target=run_server)
    # server_thread.start()
    # time.sleep(5)
    # load_model()  # ← 注释这行
    # server_thread.join()
    
    # 改为直接运行
    uvicorn.run("main:app", port=args.port, host=args.host, workers=1)
```

**启动服务:**

```bash
# 启动服务（默认 8001 端口）
python main.py --host 0.0.0.0 --port 8001

# 成功启动后会显示:
# INFO:     Uvicorn running on http://0.0.0.0:8001 (Press CTRL+C to quit)
# INFO:     Started reloader process
# INFO:     Started server process
# INFO:     Waiting for application startup.
# INFO:     Application startup complete.

# 访问 API 文档
# http://localhost:8001/docs
```

**GPU 环境配置（可选）:**

```bash
# 检查 CUDA 是否可用
python -c "import torch; print(torch.cuda.is_available())"

# 如果返回 False，安装 GPU 版本的 PyTorch
pip uninstall torch torchvision

# CUDA 11.8
pip install torch torchvision --index-url https://download.pytorch.org/whl/cu118

# CUDA 12.1
pip install torch torchvision --index-url https://download.pytorch.org/whl/cu121
```

#### 步骤 4: 启动 Java Spring Boot 后端

**配置数据库连接:**

编辑 `backend/src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: user-center
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root              # ← 修改为你的 MySQL 用户名
    password: 123456            # ← 修改为你的 MySQL 密码
    url: jdbc:mysql://localhost:3306/itap?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
  data:
    redis:
      host: localhost           # ← Redis 地址
      port: 6379                # ← Redis 端口
      password:                 # 如果 Redis 有密码，在这里配置
```

**启动服务（方式一：Maven Wrapper - 推荐）:**

```bash
cd project20250624gpp03-main/backend

# Linux/Mac
./mvnw clean spring-boot:run

# Windows
mvnw.cmd clean spring-boot:run
```

**启动服务（方式二：先构建再运行）:**

```bash
# 构建项目（跳过测试）
./mvnw clean package -DskipTests

# 运行 JAR 包
java -jar target/user-center-0.0.1-SNAPSHOT.jar

# 后台运行（Linux/Mac）
nohup java -jar target/user-center-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

**启动服务（方式三：使用 IDE）:**

1. 使用 IntelliJ IDEA 打开 `backend/` 目录
2. 等待 Maven 依赖下载完成
3. 找到主类 `UserCenterApplication` (通常在 `src/main/java/.../UserCenterApplication.java`)
4. 右键 → `Run 'UserCenterApplication'`

**验证启动成功:**

```bash
# 检查端口是否监听
netstat -tuln | grep 8080
# 或
lsof -i:8080

# 访问健康检查端点
curl http://localhost:8080/actuator/health

# 访问 API 文档
# Knife4j: http://localhost:8080/doc.html
# Swagger: http://localhost:8080/swagger-ui.html
```

#### 步骤 5: 启动 Vue 3 Web 管理端

**安装依赖:**

```bash
cd project20250624gpp03-main/vue-ui

# 使用 npm
npm install

# 如果速度慢，使用淘宝镜像
npm install --registry=https://registry.npmmirror.com

# 或永久配置淘宝镜像
npm config set registry https://registry.npmmirror.com

# 或使用 pnpm（更快）
npm install -g pnpm
pnpm install
```

**配置后端地址:**

根据项目实际情况，找到 API 配置文件（通常在 `src/api/` 或 `src/utils/request.js`），修改后端地址：

```javascript
// 示例配置
const API_BASE_URL = {
  java: 'http://localhost:8080',      // Java 后端地址
  python: 'http://localhost:8001'     // Python AI 服务地址
}

// 或在 vite.config.js 中配置代理
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
```

**启动开发服务器:**

```bash
# 启动（默认 5173 端口）
npm run dev

# 或指定端口
npm run dev -- --port 3000

# 成功启动后会显示:
# VITE v6.x.x  ready in xxx ms
# ➜  Local:   http://localhost:5173/
# ➜  Network: http://192.168.x.x:5173/
```

**访问应用:**

打开浏览器访问: http://localhost:5173

---

## 🔍 启动验证清单

启动完成后，使用以下清单验证各服务是否正常：

| 服务 | 检查方法 | 预期结果 |
|-----|---------|---------|
| MySQL | `mysql -u root -p` 并执行 `SHOW DATABASES;` | 能看到 `itap` 数据库 |
| Redis | `redis-cli ping` | 返回 `PONG` |
| Python AI | 访问 http://localhost:8001/docs | 显示 FastAPI 文档页面 |
| Java 后端 | 访问 http://localhost:8080/doc.html | 显示 Knife4j 文档页面 |
| Vue 前端 | 访问 http://localhost:5173 | 显示登录页面或管理界面 |

**端口占用检查:**

```bash
# Linux/Mac
lsof -i:3306  # MySQL
lsof -i:6379  # Redis
lsof -i:8001  # Python
lsof -i:8080  # Java
lsof -i:5173  # Vue

# Windows
netstat -ano | findstr "3306"
netstat -ano | findstr "6379"
netstat -ano | findstr "8001"
netstat -ano | findstr "8080"
netstat -ano | findstr "5173"
```

---

## 🐛 常见问题排查

### MySQL 相关

**问题 1: 无法连接 MySQL**
```
Error: Communications link failure
```

**解决方案:**
```bash
# 检查 MySQL 是否运行
sudo systemctl status mysql
# 或
docker ps | grep mysql

# 检查端口
netstat -tuln | grep 3306

# 检查防火墙
sudo ufw status
sudo ufw allow 3306

# 测试连接
mysql -h localhost -P 3306 -u root -p
```

**问题 2: 数据库不存在**
```
Error: Unknown database 'itap'
```

**解决方案:**
```sql
-- 登录 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE itap CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 验证
SHOW DATABASES;
```

**问题 3: 权限不足**
```
Error: Access denied for user 'root'@'localhost'
```

**解决方案:**
```sql
-- 重置密码
ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
FLUSH PRIVILEGES;

-- 或创建新用户
CREATE USER 'itap'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON itap.* TO 'itap'@'localhost';
FLUSH PRIVILEGES;
```

### Redis 相关

**问题: Redis 连接失败**
```
Error: Unable to connect to Redis
```

**解决方案:**
```bash
# 检查 Redis 是否运行
redis-cli ping

# 如果没有响应，启动 Redis
sudo systemctl start redis
# 或
docker start redis-itap

# 查看 Redis 日志
sudo journalctl -u redis -f
# 或
docker logs redis-itap

# 检查 Redis 配置
redis-cli CONFIG GET bind
# 如果只绑定 127.0.0.1，可能需要修改配置允许外部访问
```

### Python 服务相关

**问题 1: torch 安装失败**
```
ERROR: Could not build wheels for torch
```

**解决方案:**
```bash
# 方案 A: 升级 pip 和构建工具
pip install --upgrade pip setuptools wheel

# 方案 B: 安装预编译版本
# CPU 版本（适合开发）
pip install torch torchvision --index-url https://download.pytorch.org/whl/cpu

# GPU 版本
pip install torch torchvision --index-url https://download.pytorch.org/whl/cu118

# 方案 C: 使用 conda
conda install pytorch torchvision -c pytorch
```

**问题 2: FAISS 安装失败**
```
ERROR: Could not build wheels for faiss-cpu
```

**解决方案:**
```bash
# 使用 conda 安装（推荐）
conda install -c conda-forge faiss-cpu

# 或使用预编译版本
pip install faiss-cpu --no-cache-dir
```

**问题 3: 模块导入错误**
```
ModuleNotFoundError: No module named 'xxx'
```

**解决方案:**
```bash
# 确认虚拟环境已激活
which python  # 应显示虚拟环境路径

# 重新安装依赖
pip install -r requirements.txt --force-reinstall
```

### Java 后端相关

**问题 1: JDK 版本不匹配**
```
Error: Unsupported class file major version
```

**解决方案:**
```bash
# 检查 Java 版本
java -version
javac -version

# 应显示 Java 17
# 如果不是，设置 JAVA_HOME
export JAVA_HOME=/path/to/jdk-17
export PATH=$JAVA_HOME/bin:$PATH

# Windows
# 设置系统环境变量 JAVA_HOME
```

**问题 2: Maven 依赖下载失败**

**解决方案:**
```xml
<!-- 在 pom.xml 或 ~/.m2/settings.xml 中配置阿里云镜像 -->
<mirrors>
  <mirror>
    <id>aliyunmaven</id>
    <mirrorOf>*</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

**问题 3: 端口被占用**
```
Error: Port 8080 was already in use
```

**解决方案:**
```bash
# 查找占用进程
lsof -i:8080
# 或
netstat -tuln | grep 8080

# 杀死进程
kill -9 <PID>

# 或修改 application.yml 中的端口
server:
  port: 8081
```

### Vue 前端相关

**问题 1: Node 版本不兼容**
```
error: The engine "node" is incompatible with this module
```

**解决方案:**
```bash
# 使用 nvm 管理 Node 版本
# 安装 nvm: https://github.com/nvm-sh/nvm

# 安装 Node 18
nvm install 18
nvm use 18

# 或使用 n
npm install -g n
n 18

# 验证版本
node -v  # 应显示 v18.x.x
```

**问题 2: 依赖安装失败**
```
npm ERR! code ERESOLVE
```

**解决方案:**
```bash
# 方案 A: 清理后重新安装
rm -rf node_modules package-lock.json
npm install

# 方案 B: 使用 --legacy-peer-deps
npm install --legacy-peer-deps

# 方案 C: 使用淘宝镜像
npm install --registry=https://registry.npmmirror.com

# 方案 D: 使用 pnpm
npm install -g pnpm
pnpm install
```

**问题 3: 跨域问题**
```
Access to XMLHttpRequest has been blocked by CORS policy
```

**解决方案:**

在 `vite.config.js` 中配置代理:
```javascript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
```

或在 Java 后端添加 CORS 配置（已配置在 main.py 和 Spring Boot 中）

---

## 🔧 开发工具配置

### IntelliJ IDEA (Java 后端)

1. **导入项目**
   - `File` → `Open` → 选择 `backend/` 目录
   - 选择 `Open as Project`

2. **配置 JDK**
   - `File` → `Project Structure` → `Project`
   - SDK 选择 Java 17

3. **安装插件**
   - Lombok
   - MyBatisX
   - Rainbow Brackets

4. **配置 Maven**
   - `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Maven`
   - Maven home directory: 选择本地 Maven 或使用 Bundled
   - User settings file: ~/.m2/settings.xml

### PyCharm (Python 服务)

1. **导入项目**
   - `File` → `Open` → 选择 `backend-python/` 目录

2. **配置解释器**
   - `File` → `Settings` → `Project` → `Python Interpreter`
   - 添加虚拟环境: `Add` → `Existing Environment` → 选择 `venv/bin/python`

3. **安装插件**
   - Requirements
   - .env files support

### VS Code (Vue 前端 / 通用)

1. **安装扩展**
   - Vue - Official (Volar)
   - ESLint
   - Prettier
   - Auto Rename Tag
   - Path Intellisense

2. **配置 settings.json**
```json
{
  "editor.formatOnSave": true,
  "editor.defaultFormatter": "esbenp.prettier-vscode",
  "[vue]": {
    "editor.defaultFormatter": "Vue.volar"
  }
}
```

---

## 📚 参考文档

### 官方文档

- [Spring Boot 文档](https://spring.io/projects/spring-boot)
- [MyBatis-Plus 文档](https://baomidou.com/)
- [FastAPI 文档](https://fastapi.tiangolo.com/)
- [Vue 3 文档](https://cn.vuejs.org/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [Uni-App 文档](https://uniapp.dcloud.net.cn/)
- [Vite 文档](https://cn.vitejs.dev/)

### 技术栈学习资源

- [RWKV 模型](https://github.com/BlinkDL/RWKV-LM)
- [LangChain 文档](https://python.langchain.com/)
- [FAISS 向量库](https://github.com/facebookresearch/faiss)

---

## 💡 开发建议

1. **使用虚拟环境**: Python 使用 venv，Node 使用 nvm
2. **配置 Git**: 设置 `.gitignore`，不要提交敏感信息
3. **分支管理**: 不要直接在 main 分支开发
4. **代码规范**: 遵循团队代码规范，使用 ESLint/Prettier
5. **API 文档**: 及时更新 Swagger/Knife4j 注解
6. **日志记录**: 合理使用日志级别（DEBUG/INFO/WARN/ERROR）
7. **异常处理**: 统一异常处理和错误码
8. **数据库**: 使用 MyBatis-Plus 代码生成器
9. **前后端联调**: 使用 Postman 保存 API 测试用例
10. **定期备份**: 定期备份数据库和重要文件

---

## 🔐 安全注意事项

⚠️ **重要提示**:

1. **不要提交敏感信息到 Git**
   - 数据库密码
   - API 密钥
   - JWT 密钥
   - Redis 密码

2. **使用环境变量**
   ```bash
   # .env 文件（不要提交到 Git）
   DB_PASSWORD=your_password
   REDIS_PASSWORD=your_redis_password
   JWT_SECRET=your_jwt_secret
   ```

3. **生产环境配置**
   - 使用强密码
   - 启用 HTTPS
   - 配置防火墙
   - 限制 Redis 访问
   - 配置数据库访问白名单

---

## 📞 获取帮助

如果遇到无法解决的问题：

1. 查看项目 README.md
2. 查看 API 文档（Swagger/Knife4j）
3. 检查日志文件
4. 搜索相关错误信息
5. 联系项目团队成员

---

**祝开发顺利！** 🎉
