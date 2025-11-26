# 仓库结构说明

本仓库包含两个独立的项目，都与教育和健康服务相关。

## 项目概览

### 1. 在线心理健康咨询平台（根目录）
**位置**: 仓库根目录  
**技术栈**: Uni-App + Vue.js  
**应用类型**: 移动端应用 / 小程序  
**用途**: 大学生心理健康测评管理系统

**主要功能**:
- 用户注册、登录、密码管理
- 在线心理健康测评
- AI 智能咨询
- 心理健康资讯浏览与收藏
- 论坛交流
- 咨询师信息查看与预约
- 心理咨询记录管理

### 2. 智能教学助手平台（ITAP）
**位置**: `project20250624gpp03-main/`  
**技术栈**: Java Spring Boot + Python FastAPI + Vue 3  
**应用类型**: 全栈 Web 应用  
**用途**: 基于 AI 大模型的智能教学辅助系统

**核心价值**:
- 备课设计自动化
- 学生个性化练习辅导
- 教学数据分析可视化
- 构建"教-学-评-练"一体化智能教育生态

---

## 详细目录结构

```
/
├── project20250624gpp03-main/          # 智能教学助手平台（ITAP）
│   ├── backend/                        # Java Spring Boot 后端服务
│   │   ├── src/
│   │   │   ├── main/
│   │   │   │   ├── java/
│   │   │   │   │   └── org/cancan/usercenter/
│   │   │   │   │       ├── controller/        # REST API 控制器
│   │   │   │   │       ├── service/           # 业务逻辑层
│   │   │   │   │       ├── mapper/            # MyBatis 数据访问层
│   │   │   │   │       ├── model/             # 数据模型
│   │   │   │   │       └── config/            # 配置类
│   │   │   │   └── resources/
│   │   │   │       ├── application.yml        # Spring Boot 配置
│   │   │   │       └── mapper/                # MyBatis XML 映射文件
│   │   │   └── test/                          # 单元测试
│   │   ├── pom.xml                            # Maven 依赖管理
│   │   ├── mvnw                               # Maven Wrapper (Linux/Mac)
│   │   └── mvnw.cmd                           # Maven Wrapper (Windows)
│   │
│   ├── backend-python/                        # Python FastAPI AI 服务
│   │   ├── routes/                            # API 路由模块
│   │   │   ├── completion.py                  # 文本生成接口
│   │   │   ├── config.py                      # 配置管理接口
│   │   │   ├── search.py                      # 知识库搜索接口
│   │   │   ├── create.py                      # 内容创建接口
│   │   │   ├── exercise.py                    # 练习题生成接口
│   │   │   └── download.py                    # 文件下载接口
│   │   ├── utils/                             # 工具类
│   │   │   ├── rwkv.py                        # RWKV 模型工具
│   │   │   ├── torch.py                       # PyTorch 工具
│   │   │   └── log.py                         # 日志工具
│   │   ├── rwkv_pip/                          # RWKV 模型核心库
│   │   ├── config/                            # 配置文件目录
│   │   ├── main.py                            # FastAPI 应用入口
│   │   ├── requirements.txt                   # Python 依赖清单
│   │   └── global_var.py                      # 全局变量管理
│   │
│   ├── vue-ui/                                # Vue 3 Web 管理端
│   │   ├── public/                            # 静态资源
│   │   ├── src/
│   │   │   ├── assets/                        # 项目资源（图片、样式等）
│   │   │   ├── components/                    # Vue 组件
│   │   │   ├── views/                         # 页面视图
│   │   │   ├── router/                        # Vue Router 路由配置
│   │   │   ├── store/                         # Vuex/Pinia 状态管理
│   │   │   ├── api/                           # API 请求封装
│   │   │   ├── utils/                         # 工具函数
│   │   │   ├── App.vue                        # 根组件
│   │   │   └── main.js                        # 应用入口
│   │   ├── package.json                       # npm 依赖管理
│   │   ├── vite.config.js                     # Vite 构建配置
│   │   └── index.html                         # HTML 入口
│   │
│   ├── pom.xml                                # Maven 父项目配置
│   ├── README.md                              # 项目说明文档
│   └── .gitignore                             # Git 忽略规则
│
├── pages/                                     # Uni-App 页面目录
│   ├── login/                                 # 登录页面
│   ├── register/                              # 注册页面
│   ├── index/                                 # 首页
│   ├── ai/                                    # AI 咨询页面
│   ├── exampaper/                             # 试卷评测
│   ├── exampapertopic/                        # 试卷题目管理
│   ├── examrecord/                            # 评测记录
│   ├── forum/                                 # 论坛模块
│   ├── news/                                  # 通知公告
│   ├── xinlijiankang/                         # 心理健康资讯
│   ├── xinlijiankangCollection/               # 心理健康收藏
│   ├── xinlijiankangLiuyan/                   # 心理健康留言
│   ├── yonghu/                                # 用户管理
│   ├── zhixunshi/                             # 咨询师管理
│   ├── zhixunshiChat/                         # 心理咨询聊天
│   ├── zhixunshiYuyue/                        # 咨询师预约
│   ├── center/                                # 个人中心
│   ├── user-info/                             # 用户信息
│   ├── changepassword/                        # 修改密码
│   └── forget/                                # 忘记密码
│
├── components/                                # Uni-App 公共组件
│   ├── uni-ui/                                # uni-ui 组件库
│   └── ...                                    # 其他自定义组件
│
├── api/                                       # API 接口封装
│   ├── index.js                               # API 统一导出
│   ├── http.js                                # HTTP 请求封装
│   └── base.js                                # API 基础配置
│
├── utils/                                     # 工具函数
│   ├── utils.js                               # 通用工具
│   ├── validate.js                            # 表单验证
│   ├── system.js                              # 系统工具（权限等）
│   └── theme.css                              # 主题样式
│
├── static/                                    # 静态资源
│   ├── tabs/                                  # tabBar 图标
│   ├── logo.png                               # 应用图标
│   └── ...                                    # 其他图片资源
│
├── assets/                                    # 资源文件
│   └── css/                                   # 样式文件
│
├── colorui/                                   # ColorUI 组件库
│   ├── main.css                               # 主样式
│   └── icon.css                               # 图标样式
│
├── uni_modules/                               # uni-app 插件市场模块
│   ├── uni-file-picker/                       # 文件选择器
│   ├── uni-data-select/                       # 数据选择器
│   └── ...                                    # 其他 uni_modules
│
├── unpackage/                                 # 编译输出目录
│   └── dist/                                  # 构建产物
│
├── test/                                      # 测试文件
│
├── .hbuilderx/                                # HBuilderX 配置
├── .idea/                                     # IntelliJ IDEA 配置
│
├── App.vue                                    # Uni-App 根组件
├── main.js                                    # Uni-App 应用入口
├── pages.json                                 # Uni-App 页面配置
├── manifest.json                              # Uni-App 应用配置
└── uni.scss                                   # Uni-App 全局样式变量
```

---

## 项目模块说明

### Uni-App 心理健康平台模块

#### 用户模块
- 用户注册、登录、密码找回
- 个人信息管理
- 密码修改

#### 心理健康模块
- 心理健康资讯浏览
- 资讯收藏
- 资讯留言评论

#### 测评模块
- 在线心理测评
- 测评记录查看
- 测评结果分析

#### 咨询师模块
- 咨询师列表浏览
- 咨询师预约
- 心理咨询聊天

#### AI 咨询模块
- AI 智能问答
- 心理健康知识查询

#### 论坛模块
- 发帖、回帖
- 我的帖子管理
- 帖子详情查看

#### 通知公告模块
- 公告列表
- 公告详情

### ITAP 智能教学助手平台模块

#### 教师端功能
- **智能备课**: 基于课程大纲自动生成教学内容
- **题库管理**: 自动生成考核题目（选择题、判断题等）
- **学情分析**: 学生答题自动批改与趋势报告
- **资源管理**: 教学资源导出与管理

#### 学生端功能
- **在线练习**: 智能题目推荐与练习
- **错题反馈**: 错题整理与知识点巩固
- **智能问答**: 结合课程知识库的 AI 助手
- **评测建议**: 个性化学习建议

#### 管理端功能
- **用户管理**: 教师、学生、管理员账号管理
- **资源管理**: 课件、试题、答题记录管理
- **数据可视化**: 教学效果分析大屏、统计图表

---

## 技术架构

### Uni-App 心理健康平台

```
┌─────────────────────────────────────┐
│         Uni-App 前端应用             │
│  (H5/微信小程序/App多端支持)          │
└─────────────┬───────────────────────┘
              │ HTTP/HTTPS
              ↓
┌─────────────────────────────────────┐
│      Java Spring Boot 后端           │
│  (端口 8080，需单独部署)              │
└─────────────┬───────────────────────┘
              │
         ┌────┴────┐
         ↓         ↓
    ┌────────┐ ┌─────┐
    │ MySQL  │ │Redis│
    └────────┘ └─────┘
```

### ITAP 智能教学助手平台

```
┌──────────────────────────────────────────────┐
│           Vue 3 Web 管理端 (5173)             │
│         Element Plus + Vite + Axios          │
└───────────────────┬──────────────────────────┘
                    │ HTTP API
        ┌───────────┴──────────┐
        ↓                      ↓
┌──────────────────┐  ┌──────────────────────┐
│ Java Spring Boot │  │ Python FastAPI       │
│   后端 (8080)    │  │  AI 服务 (8001)      │
│                  │  │                      │
│ • REST API       │  │ • RWKV 大模型        │
│ • 业务逻辑       │←→│ • LangChain          │
│ • 权限控制       │  │ • FAISS 向量库       │
│ • MyBatis-Plus   │  │ • 知识库检索         │
└────────┬─────────┘  └──────────────────────┘
         │
    ┌────┴────┐
    ↓         ↓
┌────────┐ ┌─────┐
│ MySQL  │ │Redis│
│  8.0+  │ │ 6.0+│
└────────┘ └─────┘
```

---

## 端口分配

| 服务 | 默认端口 | 说明 |
|-----|---------|-----|
| Vue 3 前端 (dev) | 5173 | Vite 开发服务器 |
| Java Spring Boot | 8080 | REST API 服务 |
| Python FastAPI | 8001 | AI 模型服务 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存服务 |

---

## 关键配置文件

### Uni-App 心理健康平台

| 文件 | 说明 |
|-----|-----|
| `manifest.json` | 应用配置（appid、版本号、平台配置） |
| `pages.json` | 页面路由配置、tabBar 配置 |
| `App.vue` | 全局样式、生命周期 |
| `main.js` | 应用入口、全局组件/插件注册 |
| `api/base.js` | API 基础地址配置 |

### ITAP 智能教学助手平台

| 文件 | 说明 |
|-----|-----|
| `backend/src/main/resources/application.yml` | Spring Boot 配置（数据库、Redis、端口等） |
| `backend/pom.xml` | Maven 依赖配置 |
| `backend-python/requirements.txt` | Python 依赖清单 |
| `backend-python/main.py` | FastAPI 应用入口和模型加载配置 |
| `vue-ui/package.json` | npm 依赖配置 |
| `vue-ui/vite.config.js` | Vite 构建配置、开发服务器配置 |

---

## 数据流向

### 教学内容生成流程
```
教师输入课程大纲
    ↓
Vue 前端 → Java 后端 (8080)
    ↓
Java 后端调用 → Python AI 服务 (8001)
    ↓
RWKV 大模型 + 知识库检索
    ↓
生成教学内容
    ↓
返回 → Java 后端 → Vue 前端
    ↓
教师查看/编辑/保存
```

### 学生练习流程
```
学生请求练习题
    ↓
前端 → Java 后端
    ↓
查询题库 OR 调用 AI 生成新题
    ↓
返回题目 → 学生作答
    ↓
提交答案 → 自动批改
    ↓
保存答题记录 + 错题分析
```

---

## 开发团队分工

| 成员 | 主要负责 |
|-----|---------|
| 王乾旭 | 大模型服务开发 (backend-python/)、知识库构建、模型服务集成 |
| 洪宇灿 | Java 后端开发 (backend/)、接口开发、数据处理 |
| 吴佳昊 | 前端开发 (vue-ui/ + Uni-App)、页面交互设计 |
| 孙逍遥 | 可视化模块开发、学情分析大屏、统计图表 (ECharts) |

---

## 相关文档

- 详细的本地环境配置，请参考 `LOCAL_SETUP.md`
- API 接口文档，访问运行中的服务：
  - Java 后端: http://localhost:8080/doc.html (Knife4j)
  - Python AI 服务: http://localhost:8001/docs (FastAPI)

---

## 许可与说明

本项目仅用于教育实训用途，涉及的大模型及其使用需遵守相应开源协议。
