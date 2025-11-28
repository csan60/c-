/**
 * API Configuration
 * API配置文件 - 支持环境变量配置
 */

const config = {
  // API Base URL
  url: process.env.VUE_APP_BASE_API || 'http://localhost:8080/daxueshengxinlijiankangpingce/',
  
  // Request Timeout
  timeout: 30000,
  
  // Token Key
  tokenKey: 'token',
  
  // Response Code
  CODE: {
    SUCCESS: 0,
    UNAUTHORIZED: 401,
    FORBIDDEN: 403,
    NOT_FOUND: 404,
    SERVER_ERROR: 500,
  },
};

export default config;
