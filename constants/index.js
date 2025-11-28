/**
 * Application Constants
 * 应用常量定义
 */

// User Roles
export const USER_ROLES = {
  ADMIN: '管理员',
  USER: '用户',
  COUNSELOR: '咨询师',
};

// Storage Keys
export const STORAGE_KEYS = {
  TOKEN: 'token',
  ROLE: 'role',
  TABLE: 'nowTable',
  USER_INFO: 'userInfo',
};

// Response Codes
export const RESPONSE_CODE = {
  SUCCESS: 0,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  SERVER_ERROR: 500,
};

// Page Status
export const PAGE_STATUS = {
  LOADING: 'loading',
  SUCCESS: 'success',
  ERROR: 'error',
  EMPTY: 'empty',
};

// Upload File Types
export const FILE_TYPES = {
  IMAGE: ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp'],
  VIDEO: ['mp4', 'avi', 'mov', 'wmv', 'flv'],
  DOCUMENT: ['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt'],
};

// Max Upload Size (10MB)
export const MAX_UPLOAD_SIZE = 10 * 1024 * 1024;

// Pagination Config
export const PAGINATION = {
  DEFAULT_PAGE: 1,
  DEFAULT_LIMIT: 10,
  MAX_LIMIT: 100,
};

// Navigation Types
export const NAV_TYPES = {
  NAVIGATE: 'navigateTo',
  REDIRECT: 'redirectTo',
  SWITCH_TAB: 'switchTab',
  RELAUNCH: 'reLaunch',
  NAVIGATE_BACK: 'navigateBack',
};

// Request Methods
export const REQUEST_METHODS = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE',
};

// Toast Icons
export const TOAST_ICONS = {
  SUCCESS: 'success',
  ERROR: 'error',
  LOADING: 'loading',
  NONE: 'none',
};

// Default Duration (ms)
export const DEFAULT_DURATION = 2000;

// Regex Patterns
export const REGEX_PATTERNS = {
  EMAIL: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/,
  MOBILE: /^1[0-9]{10}$/,
  PHONE: /^([0-9]{3,4}-)?[0-9]{7,8}$/,
  ID_CARD: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
  URL: /^http[s]?:\/\/.*/,
  NUMBER: /(^-?[+-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$)|(^$)/,
  INTEGER: /(^-?\d+$)|(^$)/,
};

export default {
  USER_ROLES,
  STORAGE_KEYS,
  RESPONSE_CODE,
  PAGE_STATUS,
  FILE_TYPES,
  MAX_UPLOAD_SIZE,
  PAGINATION,
  NAV_TYPES,
  REQUEST_METHODS,
  TOAST_ICONS,
  DEFAULT_DURATION,
  REGEX_PATTERNS,
};
