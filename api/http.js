/**
 * 通用uni-app网络请求
 * 基于 Promise 对象实现更简单的 request 使用方式，支持请求和响应拦截
 */
import base from './base';
import config from './config';
import Logger from '@/utils/logger';
import { STORAGE_KEYS, RESPONSE_CODE } from '@/constants';

export default {
    config: {
        baseUrl: base.url,
        header: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        data: {},
        method: 'GET',
        dataType: 'json',
        timeout: config.timeout,
        success() {},
        fail() {},
        complete() {}
    },
    interceptor: {
        request: null,
        response: null
    },
    request(options) {
        if (!options) {
            options = {};
        }
        options.baseUrl = options.baseUrl || this.config.baseUrl;
        options.dataType = options.dataType || this.config.dataType;
        options.url = options.baseUrl + options.url;
        options.data = options.data || {};
        options.method = options.method || this.config.method;
        options.timeout = options.timeout || this.config.timeout;
        
        const token = uni.getStorageSync(STORAGE_KEYS.TOKEN);
        if (token) {
            options.header = Object.assign({}, this.config.header, options.header, { Token: token });
        } else {
            options.header = Object.assign({}, this.config.header, options.header);
        } 
        return new Promise((resolve, reject) => {
            let _config = null;
            options.complete = (response) => {
                const statusCode = response.statusCode;
                response.config = _config;
                
                // Log API response in development
                Logger.api(_config.method, _config.url, _config.data, response.data);
                
                // Response interceptor
                if (this.interceptor.response) {
                    const newResponse = this.interceptor.response(response);
                    if (newResponse) {
                        response = newResponse;
                    }
                }
                
                // Handle HTTP status codes
                if (statusCode === 200) {
                    const rs = response.data;
                    if (rs.code === RESPONSE_CODE.SUCCESS) {
                        resolve(response.data);
                    } else if (rs.code === RESPONSE_CODE.UNAUTHORIZED) {
                        Logger.warn('Unauthorized access, redirecting to login');
                        uni.removeStorageSync(STORAGE_KEYS.TOKEN);
                        uni.removeStorageSync(STORAGE_KEYS.ROLE);
                        uni.navigateTo({
                            url: '/pages/login/login',
                            fail: () => {
                                uni.reLaunch({
                                    url: '/pages/login/login'
                                });
                            }
                        });
                        reject(response);
                    } else {
                        const errorMsg = rs.msg || 'Request failed';
                        Logger.error('API Error', { code: rs.code, message: errorMsg });
                        uni.showToast({
                            title: errorMsg,
                            icon: 'none',
                            duration: 2000
                        });
                        reject(response);
                    }
                } else {
                    const errorMsg = '接口执行异常';
                    Logger.apiError(_config.method, _config.url, { statusCode, response });
                    uni.showToast({
                        title: errorMsg,
                        icon: 'none',
                        duration: 2000
                    });
                    reject(response);
                }
            };
            
            _config = Object.assign({}, this.config, options);
            _config.requestId = new Date().getTime();
            
            // Request interceptor
            if (this.interceptor.request) {
                this.interceptor.request(_config);
            }
            
            // Log API request in development
            Logger.debug(`API ${_config.method} ${_config.url}`, _config.data);
            
            uni.request(_config);
        });
    },
    get(url, data, options) {
        if (!options) {
            options = {};
        }
        options.url = url;
        options.data = data;
        options.method = 'GET';
        return this.request(options);
    },
    post(url, data, options) {
        if (!options) {
            options = {};
        }
        options.url = url;
        options.data = data;
        options.method = 'POST';
        return this.request(options);
    },
    put(url, data, options) {
        if (!options) {
            options = {};
        }
        options.url = url;
        options.data = data;
        options.method = 'PUT';
        return this.request(options);
    },
    delete(url, data, options) {
        if (!options) {
            options = {};
        }
        options.url = url;
        options.data = data;
        options.method = 'DELETE';
        return this.request(options);
    }
};
