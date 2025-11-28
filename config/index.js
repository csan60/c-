/**
 * Application Configuration
 * 应用配置文件
 * 
 * 该文件用于管理应用的全局配置，包括API地址、超时设置、日志级别等
 * 支持通过环境变量进行配置，优先读取环境变量，否则使用默认值
 */

const config = {
  // API Base URL - 支持环境变量配置
  baseUrl: process.env.VUE_APP_BASE_API || 'http://localhost:8080/daxueshengxinlijiankangpingce/',

  // Request Timeout (milliseconds)
  timeout: parseInt(process.env.VUE_APP_TIMEOUT) || 30000,

  // Application Name
  appName: process.env.VUE_APP_NAME || 'College Student Mental Health Assessment System',

  // Environment
  env: process.env.NODE_ENV || 'development',

  // Whether to enable debug logs
  debug: process.env.NODE_ENV === 'development',

  // Log Level: 'none' | 'error' | 'warn' | 'info' | 'debug'
  logLevel: process.env.VUE_APP_LOG_LEVEL || 'info',

  // API Response Code Definition
  responseCode: {
    SUCCESS: 0,
    UNAUTHORIZED: 401,
    FORBIDDEN: 403,
    NOT_FOUND: 404,
    SERVER_ERROR: 500,
  },

  // Token Storage Key
  tokenKey: 'token',

  // User Role Storage Key
  roleKey: 'role',

  // Current Table Storage Key
  tableKey: 'nowTable',

  // Pagination Default Settings
  pagination: {
    defaultPage: 1,
    defaultLimit: 10,
    defaultLimits: [10, 20, 50, 100],
  },

  // Upload Configuration
  upload: {
    maxSize: 10 * 1024 * 1024, // 10MB
    acceptImage: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'],
    acceptVideo: ['mp4', 'avi', 'mov', 'wmv', 'flv'],
    acceptDocument: ['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'],
  },

  // Swiper Configuration
  swiper: {
    autoplay: true,
    interval: 5000,
    duration: 1000,
  },
};

export default config;
