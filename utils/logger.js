/**
 * Logger Utility
 * 日志工具类 - 根据环境和配置输出日志
 */

const isDev = process.env.NODE_ENV === 'development';
const logLevel = process.env.VUE_APP_LOG_LEVEL || 'info';

const levels = {
  none: 0,
  error: 1,
  warn: 2,
  info: 3,
  debug: 4,
};

const currentLevel = levels[logLevel] || levels.info;

class Logger {
  static formatMessage(level, message, data) {
    const timestamp = new Date().toISOString();
    const prefix = `[${timestamp}] [${level.toUpperCase()}]`;
    return data ? `${prefix} ${message}` : `${prefix} ${message}`;
  }

  static log(level, message, data) {
    if (levels[level] > currentLevel) {
      return;
    }

    const formattedMessage = this.formatMessage(level, message, data);

    if (data) {
      console[level](formattedMessage, data);
    } else {
      console[level](formattedMessage);
    }
  }

  static error(message, data) {
    this.log('error', message, data);
  }

  static warn(message, data) {
    this.log('warn', message, data);
  }

  static info(message, data) {
    this.log('info', message, data);
  }

  static debug(message, data) {
    if (!isDev) return;
    this.log('debug', message, data);
  }

  static api(method, url, params, response) {
    if (!isDev) return;
    this.debug(`API ${method} ${url}`, { params, response });
  }

  static apiError(method, url, error) {
    this.error(`API Error ${method} ${url}`, error);
  }
}

export default Logger;
