import Vue from "vue";
import App from "./App";
import utils from "./utils/utils.js";
import api from "./api/index.js";
import http from "./api/http.js";
import base from "./api/base.js";
import config from "./config";
import Logger from "@/utils/logger";
import * as validate from "utils/validate.js";
// 工具类
import { isAuth } from "@/utils/system";

Vue.prototype.$utils = utils;
Vue.prototype.$base = base;
Vue.prototype.$api = api;
Vue.prototype.$http = http;
Vue.prototype.$config = config;
Vue.prototype.$logger = Logger;
Vue.prototype.$validate = validate;
// 判断权限方法
Vue.prototype.isAuth = isAuth;

Vue.config.productionTip = false;
App.mpType = "app";
const app = new Vue({
  ...App,
});
app.$mount();
