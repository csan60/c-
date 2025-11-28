# API Documentation

本文档描述了应用中使用的主要 API 接口及其用法。

---

## 基础配置

### Base URL

- **开发环境**: `http://localhost:8080/daxueshengxinlijiankangpingce/`
- **生产环境**: 在 `.env.production` 中配置

### 请求头

所有请求都会自动添加以下请求头：

```javascript
{
  "Content-Type": "application/json;charset=UTF-8",
  "Token": "用户登录后的token" // 自动从本地存储获取
}
```

### 响应格式

所有 API 响应都遵循统一格式：

```javascript
{
  "code": 0,           // 0表示成功，其他值表示错误
  "msg": "提示信息",    // 响应消息
  "data": {}           // 响应数据
}
```

### 响应代码

| Code | 说明 | 处理方式 |
| --- | --- | --- |
| 0 | 成功 | 返回数据 |
| 401 | 未授权/Token过期 | 自动跳转登录页 |
| 403 | 无权限 | 显示错误提示 |
| 404 | 资源不存在 | 显示错误提示 |
| 500 | 服务器错误 | 显示错误提示 |

---

## 用户认证 API

### 1. 登录

```javascript
this.$api.login(tableName, { username, password })
```

**参数**:
- `tableName`: 用户表名（如 `'yonghu'`、`'users'`）
- `username`: 用户名
- `password`: 密码

**响应**:
```javascript
{
  "code": 0,
  "msg": "登录成功",
  "data": {
    "token": "xxx",
    "role": "用户",
    // 其他用户信息
  }
}
```

**示例**:
```javascript
const res = await this.$api.login('yonghu', {
  username: 'test',
  password: '123456'
});
uni.setStorageSync('token', res.data.token);
uni.setStorageSync('role', res.data.role);
```

### 2. 注册

```javascript
this.$api.register(tableName, userData)
```

**参数**:
- `tableName`: 用户表名
- `userData`: 用户注册信息对象

**示例**:
```javascript
await this.$api.register('yonghu', {
  yonghuName: '张三',
  yonghuPhone: '13800138000',
  yonghuIdNumber: '110101199001011234',
  username: 'zhangsan',
  password: '123456'
});
```

### 3. 重置密码

```javascript
this.$api.resetPass(tableName, username)
```

### 4. 获取当前用户信息

```javascript
this.$api.session(tableName)
```

**响应**:
```javascript
{
  "code": 0,
  "data": {
    "id": 1,
    "username": "zhangsan",
    // 其他用户信息
  }
}
```

---

## 通用 CRUD API

### 1. 分页查询

```javascript
this.$api.page(tableName, { page, limit, ...filters })
```

**参数**:
- `tableName`: 表名（如 `'news'`、`'xinlijiankang'`）
- `page`: 页码（从1开始）
- `limit`: 每页条数
- `...filters`: 其他查询条件

**响应**:
```javascript
{
  "code": 0,
  "data": {
    "total": 100,        // 总记录数
    "pageSize": 10,      // 每页条数
    "totalPage": 10,     // 总页数
    "currPage": 1,       // 当前页
    "list": [...]        // 数据列表
  }
}
```

**示例**:
```javascript
const res = await this.$api.page('news', {
  page: 1,
  limit: 10,
  newsName: '心理健康' // 可选的筛选条件
});
this.newsList = res.data.list;
```

### 2. 列表查询（不分页）

```javascript
this.$api.list(tableName, params)
```

**示例**:
```javascript
const res = await this.$api.list('xinlijiankang', {
  limit: 6
});
```

### 3. 详情查询

```javascript
this.$api.detail(tableName, id)
// 或
this.$api.info(tableName, id)
```

**示例**:
```javascript
const res = await this.$api.detail('news', 123);
this.newsDetail = res.data;
```

### 4. 新增

```javascript
this.$api.add(tableName, data)
// 或
this.$api.save(tableName, data)
```

**示例**:
```javascript
await this.$api.add('xinlijiankangLiuyan', {
  refid: this.newsId,
  nickname: this.userInfo.username,
  content: this.liuyanContent
});
```

### 5. 修改

```javascript
this.$api.update(tableName, data)
```

**注意**: `data` 中必须包含 `id` 字段

**示例**:
```javascript
await this.$api.update('yonghu', {
  id: this.userId,
  yonghuName: '新名字',
  yonghuPhone: '13900139000'
});
```

### 6. 删除

```javascript
this.$api.del(tableName, ids)
```

**参数**:
- `ids`: 可以是单个ID或ID数组

**示例**:
```javascript
// 删除单条
await this.$api.del('news', [123]);

// 删除多条
await this.$api.del('news', [123, 456, 789]);
```

---

## 特殊功能 API

### 1. 智能推荐

```javascript
this.$api.recommend(tableName, page, limit)
```

**示例**:
```javascript
const res = await this.$api.recommend('xinlijiankang', 1, 5);
this.recommendList = res.data.list;
```

### 2. 文件上传

```javascript
this.$api.upload(callback)
```

**示例**:
```javascript
this.$api.upload((result) => {
  console.log('上传成功', result.file);
  this.photoUrl = result.file;
});
```

### 3. 视频上传

```javascript
this.$api.uploadMedia(callback)
```

### 4. 评分查询

```javascript
this.$api.queryScore(params)
```

---

## 自定义查询 API

### 1. 条件查询

```javascript
this.$api.requestCondition(tableName, functionName, conditions)
```

**参数**:
- `conditions`: 条件数组，格式: `[{ key: 'field', val: 'value' }]`

**示例**:
```javascript
const res = await this.$api.requestCondition('news', 'list', [
  { key: 'newsTypes', val: '1' }
]);
```

### 2. 条件查询（带数据）- GET

```javascript
this.$api.requestConditionDataGet(tableName, functionName, conditions, data)
```

### 3. 条件查询（带数据）- POST

```javascript
this.$api.requestConditionDataPost(tableName, functionName, conditions, data)
```

### 4. Map参数查询

```javascript
this.$api.requestMap(tableName, functionName, data)
```

---

## 使用工具方法

### HTTP 直接调用

```javascript
// GET 请求
this.$http.get('url', { param1: 'value1' })

// POST 请求
this.$http.post('url', { data: 'value' })

// PUT 请求
this.$http.put('url', { data: 'value' })

// DELETE 请求
this.$http.delete('url', { id: 123 })
```

### 完整请求配置

```javascript
this.$http.request({
  url: 'custom/endpoint',
  method: 'POST',
  data: { key: 'value' },
  header: { 'Custom-Header': 'value' },
  timeout: 60000
})
```

---

## 错误处理

所有 API 调用都应使用 `try-catch` 或 `.catch()` 处理错误：

```javascript
try {
  const res = await this.$api.page('news', { page: 1, limit: 10 });
  this.newsList = res.data.list;
} catch (error) {
  console.error('获取新闻列表失败', error);
  this.$utils.msg('加载失败，请稍后重试');
}
```

---

## 最佳实践

1. **统一错误处理**: 使用 try-catch 包裹异步调用
2. **加载状态**: 在请求前显示 loading，请求后隐藏
3. **数据验证**: 在调用 API 前验证必填参数
4. **Token 管理**: 登录成功后立即保存 token
5. **登出处理**: 清除本地存储的 token 和用户信息
6. **请求节流**: 对于频繁操作，添加防抖或节流

**示例完整流程**:
```javascript
async loadNewsList() {
  uni.showLoading({ title: '加载中...' });
  try {
    const res = await this.$api.page('news', {
      page: this.page,
      limit: this.limit
    });
    this.newsList = res.data.list;
    this.total = res.data.total;
  } catch (error) {
    this.$logger.error('加载新闻失败', error);
    this.$utils.msg('加载失败');
  } finally {
    uni.hideLoading();
  }
}
```

---

## 环境配置

在 `.env` 文件中配置 API Base URL：

```bash
# .env.development
VUE_APP_BASE_API=http://localhost:8080/daxueshengxinlijiankangpingce/

# .env.production
VUE_APP_BASE_API=https://api.example.com/daxueshengxinlijiankangpingce/
```

---

如有疑问或需要补充，请联系开发团队。
