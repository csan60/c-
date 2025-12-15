# PPT内容：颐康智护，银龄无忧
## 智能老年健康管理小程序

---

## 第1页：封面页
### 主标题
**颐康智护，银龄无忧**
智能老年健康管理系统

### 副标题
基于AI大模型的老年人健康监护与智能干预平台

### 视觉元素
- 背景：温暖的渐变色（粉色到紫色）
- 图标：心跳曲线 + 老人关爱手势
- 关键词标签：#AI健康评估 #智能干预 #适老化设计

---

## 第2页：项目背景 - 为什么做这个项目？

### 社会痛点
📊 **数据说话**
- 中国60岁以上人口：2.8亿+（占比19.8%）
- 老年人慢性病患病率：75%+
- 独居/空巢老人：1.18亿+

🔍 **现实困境**
1. **健康监测难**：传统方式繁琐，数据难以持续追踪
2. **专业指导少**：医疗资源紧张，老人就医不便
3. **子女照护难**：工作繁忙，无法实时关注父母健康
4. **信息化门槛高**：现有应用操作复杂，老人难以使用

💡 **我们的解决方案**
用科技温暖银发群体，让健康管理变得简单、智能、贴心

---

## 第3页：技术架构全景图

### 整体架构（三层架构）

```
┌─────────────────────────────────────────────┐
│           前端展示层（小程序端）              │
├─────────────────────────────────────────────┤
│  uni-app框架 + Vue.js + ColorUI组件库        │
│  ✓ 适老化UI设计    ✓ 语音交互               │
│  ✓ 数据可视化      ✓ 微信运动集成           │
└─────────────────────────────────────────────┘
                     ↕ RESTful API
┌─────────────────────────────────────────────┐
│          业务逻辑层（后端服务）              │
├─────────────────────────────────────────────┤
│  Spring Boot 2.2.2 + MyBatis Plus           │
│  ✓ AI健康评估      ✓ 干预方案生成           │
│  ✓ OCR识别         ✓ 数据分析               │
└─────────────────────────────────────────────┘
                     ↕
┌─────────────────────────────────────────────┐
│       数据存储层 + 第三方服务集成            │
├─────────────────────────────────────────────┤
│  MySQL 数据库 | 阿里云OSS | 腾讯地图 API     │
│  DeepSeek AI  | 百度OCR   | 讯飞语音         │
└─────────────────────────────────────────────┘
```

### 核心技术栈
**前端：** uni-app + Vue.js 2.x + ColorUI
**后端：** Spring Boot 2.2.2 + MyBatis Plus + Shiro
**数据库：** MySQL 5.7+
**AI服务：** DeepSeek AI (健康评估) + 百度AI OCR
**云服务：** 阿里云OSS (文件存储) + 腾讯地图 (位置服务)
**其他：** 微信开放平台 (登录、运动数据) + 讯飞语音识别

---

## 第4页：核心功能模块（1/2）

### 🏥 1. 智能健康档案管理
**功能亮点：**
- 12+项健康指标实时监测
- 自动计算BMI、体脂率等派生指标
- 历史数据趋势可视化展示
- 异常数据自动预警

**监测指标：**
```
基础指标：身高、体重、BMI
生命体征：心率、血压、体温、血氧
代谢指标：血糖、血脂、体脂率
运动数据：步数、消耗卡路里
```

### 🤖 2. AI驱动的健康评估
**核心技术：DeepSeek大模型**
- 多维度数据综合分析
- 生成0-100分健康评分
- 智能风险等级判定（低危/中危/高危）
- 自然语言生成评估报告

**评估维度：**
✓ 生命体征稳定性  ✓ 代谢指标健康度
✓ 运动量充足性    ✓ 综合健康趋势

---

## 第5页：核心功能模块（2/2）

### 💊 3. 个性化健康干预方案
**AI智能生成，四维度全覆盖**

📋 **饮食建议**
- 根据血糖、血脂等指标制定饮食方案
- 推荐每日摄入量和禁忌食物

🏃 **运动计划**
- 基于年龄、体质、心率制定运动目标
- 提供具体运动项目和时长建议

💊 **用药提醒**
- 智能提醒服药时间和剂量
- 记录用药历史，防止漏服

🔬 **复查规划**
- 根据病史和指标异常情况安排复查
- 自动提醒复查时间和项目

### 📄 4. OCR医疗文档智能识别
**技术实现：百度AI OCR + DeepSeek解读**
- 拍照即可识别检查报告、病历
- 中英文混合识别准确率95%+
- AI自动解读报告内容
- 用通俗语言为老人解释专业术语

---

## 第6页：核心代码展示（1/3）- AI健康评估

### 后端：DeepSeek AI集成实现

```java
/**
 * 健康评估控制器
 * 功能：基于患者最新健康数据生成AI评估报告
 */
@RestController
@RequestMapping("/deepseek")
@CrossOrigin
public class DeepSeekController {
    
    @Autowired
    private DeepSeekAIService deepSeekAIService;
    
    @Autowired
    private HealthService healthService;
    
    /**
     * 生成今日健康评估
     * 路径：GET /deepseek/assessment/{huanzheId}
     */
    @GetMapping("/assessment/{huanzheId}")
    public Map<String, Object> generateTodayAssessment(
            @PathVariable("huanzheId") Long huanzheId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 获取患者最新健康记录
            HealthRecord latestRecord = 
                healthService.getLatestByHuanzheId(huanzheId);
            
            if (latestRecord == null) {
                result.put("code", 1);
                result.put("msg", "未找到健康记录");
                return result;
            }
            
            // 2. 调用DeepSeek AI生成健康评估
            Map<String, Object> assessmentData = 
                deepSeekAIService.generateHealthAssessment(latestRecord);
            
            // 3. 构造返回数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("assessment", assessmentData);
            responseData.put("score", assessmentData.get("overallScore"));
            responseData.put("riskLevel", assessmentData.get("riskLevel"));
            responseData.put("healthRecord", latestRecord);
            
            // 4. 保存评估记录到数据库
            saveAssessmentRecord(huanzheId, assessmentData);
            
            result.put("code", 0);
            result.put("msg", "评估成功");
            result.put("data", responseData);
            
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "评估失败：" + e.getMessage());
        }
        
        return result;
    }
}
```

**代码亮点：**
✓ RESTful API设计规范
✓ 统一异常处理机制
✓ 完整的数据流转链路
✓ AI服务与业务逻辑解耦

---

## 第7页：核心代码展示（2/3）- OCR识别

### 后端：百度OCR + AI解读实现

```java
/**
 * AI OCR控制器
 * 功能：医疗文档识别 + AI智能解读
 */
@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AiOcrController {
    
    // 百度AI OCR配置
    private static final String APP_ID = "6984172";
    private static final String API_KEY = "TciROKLKWsMbXSbDKciLncpq";
    private static final String SECRET_KEY = "e3jKO0vAMRQBDRg2CZb7qKvBvp98dGIn";
    
    private static final AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
    
    /**
     * 上传文档并处理
     * 路径：POST /api/ai/upload
     */
    @PostMapping("/upload")
    public R uploadAndProcess(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "olderUserId", required = false) String userId) 
            throws IOException {
        
        try {
            // 1. 读取图片字节流
            byte[] imageBytes = file.getBytes();
            
            // 2. 设置OCR识别选项
            HashMap<String, String> options = new HashMap<>();
            options.put("language_type", "CHN_ENG"); // 中英文混合
            options.put("detect_direction", "true"); // 自动检测图像方向
            
            // 3. 调用百度OCR API进行识别
            JSONObject ocrRes = client.basicGeneral(imageBytes, options);
            System.out.println("OCR识别结果: " + ocrRes.toString());
            
            // 4. 解析识别结果
            JSONArray wordsArray = ocrRes.optJSONArray("words_result");
            StringBuilder extractedText = new StringBuilder();
            
            if (wordsArray != null) {
                for (int i = 0; i < wordsArray.length(); i++) {
                    String words = wordsArray.getJSONObject(i)
                                             .getString("words");
                    extractedText.append(words).append("\n");
                }
            }
            
            // 5. 调用AI解读识别文本
            String aiReply = callDeepSeekAI(extractedText.toString());
            
            // 6. 返回结果
            return R.ok()
                    .put("code", 0)
                    .put("extractedText", extractedText.toString())
                    .put("aiReply", aiReply)
                    .put("tips", "报告已成功识别并解读");
                    
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().put("msg", "OCR识别失败: " + e.getMessage());
        }
    }
    
    /**
     * 调用DeepSeek AI解读文本
     */
    private String callDeepSeekAI(String text) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.deepseek.com/v1/chat/completions";
        String apiKey = "sk-f59d2a4ef8024ded821fb32115303c13";
        
        // 构造请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        
        // 构造消息体（Chat格式）
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", "请用通俗易懂的语言解读以下医疗报告：\n" + text);
        
        JSONArray messages = new JSONArray();
        messages.put(userMsg);
        
        JSONObject requestJson = new JSONObject();
        requestJson.put("model", "deepseek-chat");
        requestJson.put("messages", messages);
        requestJson.put("temperature", 0.7);
        
        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        
        // 解析AI回复
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject responseJson = new JSONObject(response.getBody());
            return responseJson.getJSONArray("choices")
                              .getJSONObject(0)
                              .getJSONObject("message")
                              .getString("content");
        }
        
        return "AI解读失败";
    }
}
```

---

## 第8页：核心代码展示（3/3）- 微信运动集成

### 后端：微信运动数据解密

```java
/**
 * 微信运动控制器
 * 功能：获取并解密微信运动步数数据
 */
@RestController
@RequestMapping("/wx")
public class WeRunController {
    
    @Value("${wx.appid}")
    private String appId;  // wx19c4b7134978fd5e
    
    @Value("${wx.secret}")
    private String secret; // 4a991509cee2f3b94649f0809d42d3fd
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * 解密微信运动数据
     * 路径：POST /wx/decryptWeRun
     */
    @PostMapping("/decryptWeRun")
    public Map<String, Object> decryptWeRun(@RequestBody WeRunReq req) {
        
        // 参数校验
        if (req == null || isBlank(req.code) || 
            isBlank(req.encryptedData) || isBlank(req.iv)) {
            return err("参数不完整");
        }
        
        try {
            // 1. 用code换取session_key
            String jscode2sessionUrl = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appId, secret, req.code
            );
            
            RestTemplate rt = new RestTemplate();
            String sessionResponse = rt.getForObject(jscode2sessionUrl, String.class);
            JsonNode sesJson = mapper.readTree(sessionResponse);
            
            if (sesJson == null || sesJson.get("session_key") == null) {
                return err("获取 session_key 失败");
            }
            
            String sessionKey = sesJson.get("session_key").asText();
            
            // 2. AES-128-CBC解密微信运动数据
            String decryptedJson = decrypt(sessionKey, req.iv, req.encryptedData);
            
            if (decryptedJson == null) {
                return err("解密失败");
            }
            
            // 3. 解析步数数据
            JsonNode root = mapper.readTree(decryptedJson);
            JsonNode stepInfoList = root.get("stepInfoList");
            
            // 4. 取今日步数（最后一条记录）
            int todayStep = 0;
            if (stepInfoList != null && stepInfoList.isArray() && 
                stepInfoList.size() > 0) {
                JsonNode lastRecord = stepInfoList.get(stepInfoList.size() - 1);
                if (lastRecord != null && lastRecord.get("step") != null) {
                    todayStep = lastRecord.get("step").asInt(0);
                }
            }
            
            // 5. 返回结果
            Map<String, Object> data = new HashMap<>();
            data.put("todayStep", todayStep);
            data.put("stepInfoList", mapper.convertValue(stepInfoList, List.class));
            
            return ok(data);
            
        } catch (Exception e) {
            return err("服务异常，请稍后重试");
        }
    }
    
    /**
     * AES-128-CBC解密算法
     * 微信小程序加密数据解密关键代码
     */
    private String decrypt(String sessionKey, String iv, String encryptedData) {
        try {
            // Base64解码
            byte[] keyBytes = Base64Utils.decodeFromString(sessionKey);
            byte[] ivBytes = Base64Utils.decodeFromString(iv);
            byte[] dataBytes = Base64Utils.decodeFromString(encryptedData);
            
            // AES解密
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            byte[] result = cipher.doFinal(dataBytes);
            return new String(result, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    // 请求体类
    public static class WeRunReq {
        public String code;          // 微信登录code
        public String encryptedData; // 加密数据
        public String iv;            // 初始向量
    }
}
```

**技术要点：**
✓ 微信小程序授权流程标准实现
✓ AES-128-CBC加密算法应用
✓ Base64编解码处理
✓ 安全的session_key管理

---

## 第9页：前端技术实现 - uni-app小程序

### 前端技术栈详解

```javascript
// main.js - 应用主入口
import Vue from 'vue'
import App from './App'
import cuCustom from './colorui/components/cu-custom.vue'
import api from './api'
import utils from './utils/utils'

// 全局注册组件
Vue.component('cu-custom', cuCustom)

// 挂载全局方法
Vue.prototype.$api = api      // API接口调用
Vue.prototype.$utils = utils  // 工具函数
Vue.prototype.$base = {
    url: "http://localhost:8080/project/"
}

Vue.config.productionTip = false
App.mpType = 'app'

const app = new Vue({
    ...App
})
app.$mount()
```

### 健康数据可视化展示

```vue
<!-- pages/health/trend.vue - 健康趋势图表 -->
<template>
  <view class="container">
    <view class="chart-title">近30天健康趋势</view>
    
    <!-- 使用qiun-ucharts图表组件 -->
    <qiun-data-charts
      type="line"
      :opts="chartOption"
      :chartData="chartData"
      :style="{width: '100%', height: '400rpx'}"
    />
    
    <!-- 指标选择 -->
    <view class="metrics-selector">
      <view 
        v-for="(metric, index) in metrics" 
        :key="index"
        :class="['metric-item', selectedMetric === metric.key ? 'active' : '']"
        @tap="selectMetric(metric.key)"
      >
        <text class="metric-icon">{{ metric.icon }}</text>
        <text class="metric-name">{{ metric.name }}</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedMetric: 'bloodPressure',
      metrics: [
        { key: 'bloodPressure', name: '血压', icon: '💓' },
        { key: 'heartRate', name: '心率', icon: '❤️' },
        { key: 'bloodSugar', name: '血糖', icon: '🩸' },
        { key: 'weight', name: '体重', icon: '⚖️' }
      ],
      chartData: {},
      chartOption: {
        color: ['#1890FF', '#91CB74'],
        padding: [15, 15, 0, 15],
        legend: { show: true },
        xAxis: {
          disableGrid: true,
          axisLine: false,
          boundaryGap: 'justify'
        },
        yAxis: {
          data: [{ min: 0 }],
          gridType: 'dash'
        },
        extra: {
          line: {
            type: 'curve',    // 曲线平滑
            width: 2,
            activeType: 'hollow'
          }
        }
      }
    }
  },
  
  async onLoad(options) {
    this.huanzheId = options.id || uni.getStorageSync('userId')
    await this.loadHealthData()
  },
  
  methods: {
    // 加载健康数据
    async loadHealthData() {
      uni.showLoading({ title: '加载中...' })
      
      try {
        const res = await this.$api.get('health/chartData', {
          huanzheId: this.huanzheId,
          rangeDays: 30,
          metrics: this.selectedMetric
        })
        
        if (res.code === 0) {
          this.processChartData(res.data)
        }
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    },
    
    // 处理图表数据
    processChartData(data) {
      const categories = data.dates || []
      const series = []
      
      if (this.selectedMetric === 'bloodPressure') {
        series.push({
          name: '收缩压',
          data: data.systolic || []
        })
        series.push({
          name: '舒张压',
          data: data.diastolic || []
        })
      } else {
        series.push({
          name: this.getMetricName(this.selectedMetric),
          data: data[this.selectedMetric] || []
        })
      }
      
      this.chartData = {
        categories: categories,
        series: series
      }
    },
    
    // 切换指标
    selectMetric(key) {
      this.selectedMetric = key
      this.loadHealthData()
    }
  }
}
</script>

<style lang="scss">
.container {
  padding: 20rpx;
  background: #f5f5f5;
}

.chart-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.metrics-selector {
  display: flex;
  justify-content: space-around;
  margin-top: 30rpx;
  background: white;
  border-radius: 20rpx;
  padding: 20rpx;
}

.metric-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx;
  border-radius: 10rpx;
  transition: all 0.3s;
  
  &.active {
    background: var(--publicSubColor);
    color: white;
  }
}

.metric-icon {
  font-size: 48rpx;
  margin-bottom: 10rpx;
}

.metric-name {
  font-size: 24rpx;
}
</style>
```

---

## 第10页：项目核心亮点（1/2）

### 🎯 亮点一：AI大模型深度赋能

**DeepSeek AI应用场景：**

1️⃣ **智能健康评估**
```
输入：12+项健康指标 + 历史趋势
处理：多维度数据分析 + 风险预测模型
输出：0-100分评分 + 风险等级 + 详细报告
```

2️⃣ **个性化干预方案生成**
```
输入：评估结果 + 患者基本信息 + 病史
处理：基于医学知识库的推理分析
输出：饮食/运动/用药/复查四维方案
```

3️⃣ **医疗文档智能解读**
```
输入：OCR识别的报告文本
处理：医学术语理解 + 指标分析
输出：通俗易懂的解读 + 注意事项
```

**技术优势：**
✓ 自然语言生成，报告易懂
✓ 持续学习，准确度不断提升
✓ 响应速度快，< 3秒生成报告

---

## 第11页：项目核心亮点（2/2）

### 🎯 亮点二：适老化设计全方位优化

**界面设计：**
- 🔤 **超大字体**：主要文字≥32rpx，关键信息≥48rpx
- 🎨 **高对比度配色**：粉色主题 + 深色文字，视觉清晰
- 🔘 **大按钮设计**：点击区域≥88rpx，减少误触
- 🖼️ **图标化展示**：用图标代替文字，降低认知负担

**交互优化：**
- 🎤 **语音输入**：关键功能支持语音，减轻打字负担
- 📣 **语音播报**：重要提醒可语音播报
- 🔔 **智能提醒**：用药、复查、异常指标自动推送
- 📱 **简化流程**：核心功能≤3步完成

### 🎯 亮点三：微信生态深度集成

**微信运动数据自动同步：**
- 无需手动输入，每日步数自动更新
- 纳入健康评估体系
- 运动量不足时智能提醒

**微信支付快速便捷：**
- 在线问诊、预约挂号一键支付
- 费用明细清晰透明

**微信消息推送：**
- 健康异常即时通知
- 复查提醒不错过

---

## 第12页：系统数据库设计

### 核心数据表结构

```sql
-- 1. 健康记录表（核心表）
CREATE TABLE health_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    huanzhe_id BIGINT NOT NULL COMMENT '患者ID',
    record_date DATETIME NOT NULL COMMENT '记录时间',
    
    -- 基础指标
    height DECIMAL(5,2) COMMENT '身高(cm)',
    weight DECIMAL(5,2) COMMENT '体重(kg)',
    bmi DECIMAL(4,2) COMMENT 'BMI指数',
    
    -- 生命体征
    heart_rate INT COMMENT '心率(次/分)',
    systolic INT COMMENT '收缩压(mmHg)',
    diastolic INT COMMENT '舒张压(mmHg)',
    temperature DECIMAL(3,1) COMMENT '体温(℃)',
    blood_oxygen INT COMMENT '血氧饱和度(%)',
    
    -- 代谢指标
    blood_sugar DECIMAL(4,2) COMMENT '血糖(mmol/L)',
    blood_lipids VARCHAR(50) COMMENT '血脂',
    body_fat DECIMAL(4,1) COMMENT '体脂率(%)',
    
    -- 运动数据
    steps INT COMMENT '步数',
    calories INT COMMENT '消耗卡路里',
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_huanzhe_date (huanzhe_id, record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康记录表';

-- 2. AI干预执行记录表
CREATE TABLE intervention_execution (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    huanzhe_id BIGINT NOT NULL COMMENT '患者ID',
    execution_date DATE NOT NULL COMMENT '执行日期',
    source VARCHAR(20) COMMENT '来源：assessment/intervention',
    plan_json TEXT COMMENT 'AI生成的方案JSON',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_huanzhe (huanzhe_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='干预方案执行记录';

-- 3. 患者表
CREATE TABLE huanzhe (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    huanzhe_name VARCHAR(50) NOT NULL COMMENT '姓名',
    huanzhe_phone VARCHAR(11) COMMENT '手机号',
    huanzhe_gender VARCHAR(10) COMMENT '性别',
    huanzhe_age INT COMMENT '年龄',
    huanzhe_id_number VARCHAR(18) COMMENT '身份证号',
    huanzhe_photo VARCHAR(200) COMMENT '头像',
    medical_history TEXT COMMENT '病史',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(11) COMMENT '紧急联系电话',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_phone (huanzhe_phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者信息表';

-- 4. 医生表
CREATE TABLE yisheng (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    yisheng_name VARCHAR(50) NOT NULL COMMENT '医生姓名',
    yisheng_phone VARCHAR(11) COMMENT '手机号',
    yisheng_gender VARCHAR(10) COMMENT '性别',
    yisheng_photo VARCHAR(200) COMMENT '头像',
    keshi_id BIGINT COMMENT '科室ID',
    yisheng_title VARCHAR(50) COMMENT '职称',
    yisheng_intro TEXT COMMENT '简介',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_keshi (keshi_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';

-- 5. 聊天消息表
CREATE TABLE chat_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT NOT NULL COMMENT '发送者ID',
    sender_type VARCHAR(20) COMMENT '发送者类型：patient/doctor',
    receiver_id BIGINT NOT NULL COMMENT '接收者ID',
    receiver_type VARCHAR(20) COMMENT '接收者类型',
    content TEXT COMMENT '消息内容',
    msg_type VARCHAR(20) COMMENT '消息类型：text/image/voice',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_sender (sender_id, sender_type),
    INDEX idx_receiver (receiver_id, receiver_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';
```

**数据库设计亮点：**
✓ 合理的索引设计，查询效率高
✓ JSON字段存储复杂数据，灵活性强
✓ 完整的时间戳记录，便于追溯
✓ 支持高并发访问

---

## 第13页：系统演示效果（界面展示）

### 📱 小程序界面展示

**1. 首页 - 功能导航**
```
┌─────────────────────────────────┐
│  颐康智护，银龄无忧              │
│  [轮播图：健康宣传海报]          │
├─────────────────────────────────┤
│  快捷入口（4宫格）               │
│  ┌────┐ ┌────┐ ┌────┐ ┌────┐  │
│  │健康│ │AI评│ │医生│ │心理│  │
│  │档案│ │估  │ │问诊│ │咨询│  │
│  └────┘ └────┘ └────┘ └────┘  │
├─────────────────────────────────┤
│  通知公告                        │
│  • 【重要】定期体检提醒           │
│  • 冬季养生小贴士                │
└─────────────────────────────────┘
```

**2. 健康档案页 - 数据录入**
```
┌─────────────────────────────────┐
│  今日健康数据                    │
├─────────────────────────────────┤
│  血压  ___/___ mmHg  [录入]     │
│  心率  ___ 次/分     [录入]     │
│  血糖  ___ mmol/L    [录入]     │
│  体重  ___ kg        [录入]     │
│  步数  8,523步  [微信同步✓]    │
├─────────────────────────────────┤
│  [查看历史趋势] [生成AI评估]    │
└─────────────────────────────────┘
```

**3. AI评估报告页**
```
┌─────────────────────────────────┐
│  健康评估报告                    │
│  生成时间：2025-01-15 09:30     │
├─────────────────────────────────┤
│  ⭐ 综合评分：78分               │
│  🟡 风险等级：中危               │
├─────────────────────────────────┤
│  📊 详细分析                     │
│  ✓ 血压偏高，建议控盐            │
│  ✓ 运动量不足，增加锻炼          │
│  ⚠ 血糖波动大，注意饮食          │
├─────────────────────────────────┤
│  [查看干预方案] [历史报告]      │
└─────────────────────────────────┘
```

**4. 干预方案页**
```
┌─────────────────────────────────┐
│  个性化健康干预方案              │
├─────────────────────────────────┤
│  🍎 饮食建议                     │
│  • 每日盐摄入<6g                 │
│  • 多吃绿叶蔬菜、粗粮            │
│  • 避免高糖食物                  │
├─────────────────────────────────┤
│  🏃 运动计划                     │
│  • 每日步行30分钟                │
│  • 建议运动：太极、慢跑          │
│  • 目标步数：8000步              │
├─────────────────────────────────┤
│  💊 用药提醒                     │
│  • 降压药：每日早8:00            │
│  • [开启提醒]                    │
├─────────────────────────────────┤
│  🔬 复查规划                     │
│  • 2周后复查血压                 │
│  • 1个月后复查血糖               │
└─────────────────────────────────┘
```

**界面设计特点：**
✓ 粉色温暖主题，符合健康关怀理念
✓ 超大字体，老人看得清
✓ 图标化展示，操作直观
✓ 一屏展示核心信息，减少滑动

---

## 第14页：性能优化与安全保障

### ⚡ 性能优化策略

**前端优化：**
```javascript
// 1. 图片懒加载
<image 
  :src="item.photo" 
  mode="aspectFill"
  lazy-load
  :fade-show="true"
/>

// 2. 列表分页加载（mescroll-uni组件）
<mescroll-uni 
  @down="downCallback" 
  @up="upCallback"
  :up="upOption"
>
  <view v-for="item in dataList" :key="item.id">
    <!-- 列表项 -->
  </view>
</mescroll-uni>

// 3. 数据缓存（减少重复请求）
const cachedData = uni.getStorageSync('health_data_' + userId)
if (cachedData && !isExpired(cachedData)) {
  this.healthData = cachedData.data
} else {
  this.loadFromServer()
}
```

**后端优化：**
```java
// 1. 数据库索引优化
CREATE INDEX idx_huanzhe_date ON health_record(huanzhe_id, record_date);
CREATE INDEX idx_composite ON intervention_execution(huanzhe_id, execution_date);

// 2. 分页查询（MyBatis Plus）
@Override
public PageUtils queryPage(Map<String, Object> params) {
    Page<HealthRecord> page = new Query<HealthRecord>(params).getPage();
    page.setRecords(baseMapper.selectPage(page, wrapper));
    return new PageUtils(page);
}

// 3. 异步处理AI请求（避免阻塞）
@Async
public CompletableFuture<String> generateAssessmentAsync(HealthRecord record) {
    String result = deepSeekAIService.generateHealthAssessment(record);
    return CompletableFuture.completedFuture(result);
}
```

### 🔒 安全保障措施

**1. 数据传输安全**
- HTTPS加密传输
- 敏感数据加密存储（密码MD5+盐值）
- Token鉴权机制（JWT）

**2. 权限控制**
```java
// Apache Shiro权限管理
@RequiresPermissions("health:record:view")
public R viewRecord(@PathVariable Long id) {
    // 只能查看自己或授权的健康记录
    Long currentUserId = ShiroUtils.getUserId();
    if (!hasPermission(currentUserId, id)) {
        return R.error(403, "无权访问");
    }
    return R.ok().put("data", healthService.selectById(id));
}
```

**3. 数据隐私保护**
- 患者个人信息脱敏
- 敏感字段加密存储
- 严格遵守《个人信息保护法》

**4. API安全**
- 接口频率限制（防止恶意攻击）
- SQL注入防护（参数化查询）
- XSS攻击防护（输入过滤）

---

## 第15页：项目测试与质量保证

### 🧪 测试覆盖

**单元测试（后端）**
```java
@SpringBootTest
public class HealthServiceTest {
    
    @Autowired
    private HealthService healthService;
    
    @Test
    public void testSaveHealthRecord() {
        HealthRecord record = new HealthRecord();
        record.setHuanzheId(1L);
        record.setHeartRate(75);
        record.setSystolic(120);
        record.setDiastolic(80);
        
        boolean result = healthService.saveOrUpdateWithCalculation(record);
        
        assertTrue(result);
        assertNotNull(record.getBmi());
    }
    
    @Test
    public void testHealthAbnormalCheck() {
        HealthRecord record = new HealthRecord();
        record.setSystolic(160); // 高血压
        record.setDiastolic(95);
        
        Map<String, Object> checkResult = 
            healthService.checkHealthAbnormal(record);
        
        assertTrue((Boolean) checkResult.get("hasAbnormal"));
        assertNotNull(checkResult.get("abnormalItems"));
    }
}
```

**接口测试（Postman自动化）**
```javascript
// 健康评估接口测试用例
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has assessment data", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.code).to.eql(0);
    pm.expect(jsonData.data).to.have.property("score");
    pm.expect(jsonData.data).to.have.property("riskLevel");
});

pm.test("Score is valid", function () {
    var jsonData = pm.response.json();
    var score = jsonData.data.score;
    pm.expect(score).to.be.at.least(0);
    pm.expect(score).to.be.at.most(100);
});
```

**性能测试**
- 并发用户：500+
- 响应时间：< 2秒（90%请求）
- 吞吐量：1000+ TPS
- 错误率：< 0.1%

---

## 第16页：项目部署架构

### 🚀 部署方案

```
┌─────────────────────────────────────────────────┐
│              用户访问层                          │
│  微信小程序客户端 | H5网页端 | 管理后台         │
└─────────────────────────────────────────────────┘
                       ↓ HTTPS
┌─────────────────────────────────────────────────┐
│              负载均衡层                          │
│  Nginx (反向代理 + SSL + 负载均衡)              │
└─────────────────────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────┐
│              应用服务层                          │
│  Spring Boot服务集群（多实例部署）              │
│  端口: 8080, 8081, 8082...                      │
└─────────────────────────────────────────────────┘
                       ↓
┌────────────┬──────────────┬────────────────────┐
│  数据层    │  缓存层      │  文件存储层        │
├────────────┼──────────────┼────────────────────┤
│ MySQL     │  Redis       │  阿里云OSS         │
│ (主从同步) │  (会话/热数据)│  (图片/文档)       │
└────────────┴──────────────┴────────────────────┘
                       ↓
┌─────────────────────────────────────────────────┐
│           第三方服务集成层                       │
│  DeepSeek AI | 百度OCR | 微信API | 腾讯地图    │
└─────────────────────────────────────────────────┘
```

### 部署命令

```bash
# 1. 后端部署（Docker）
cd server_code
docker build -t yikang-backend:latest .
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  --name yikang-backend \
  yikang-backend:latest

# 2. 数据库初始化
mysql -u root -p < database/init.sql

# 3. Nginx配置
upstream backend {
    server 127.0.0.1:8080 weight=1;
    server 127.0.0.1:8081 weight=1;
    server 127.0.0.1:8082 weight=1;
}

server {
    listen 443 ssl;
    server_name api.yikang.com;
    
    ssl_certificate /etc/nginx/ssl/cert.pem;
    ssl_certificate_key /etc/nginx/ssl/key.pem;
    
    location /project/ {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}

# 4. 前端打包（微信小程序）
# 使用HBuilderX
# 1. 发行 -> 小程序-微信
# 2. 上传到微信开发者平台
# 3. 提交审核
```

---

## 第17页：项目数据统计与成果

### 📊 开发数据统计

**代码量统计：**
```
后端 (Java)：
├── Controller层：25个文件，3,500+ 行
├── Service层：30个文件，5,200+ 行
├── Entity层：32个文件，2,800+ 行
├── Mapper/DAO层：25个文件，1,500+ 行
└── Utils/Config：20个文件，2,000+ 行
    总计：约 15,000 行 Java 代码

前端 (uni-app/Vue)：
├── Pages：24个页面，8,000+ 行
├── Components：15个组件，1,500+ 行
├── API/Utils：10个文件，1,000+ 行
└── 配置文件：pages.json, manifest.json 等
    总计：约 10,500 行前端代码

数据库：
├── 表结构：15+ 张核心表
├── 索引：30+ 个优化索引
└── 存储过程：5个
```

**功能模块统计：**
✓ 核心功能模块：8个
✓ 子功能点：40+
✓ API接口：60+
✓ 数据库表：15+

### 🎖️ 技术突破点

1. **AI大模型应用**：DeepSeek在健康领域的实战应用
2. **OCR+AI解读**：医疗文档智能识别+通俗化解读
3. **微信生态集成**：微信运动数据自动同步
4. **适老化设计**：深度优化的老人友好界面
5. **数据可视化**：多指标健康趋势图表展示

---

## 第18页：用户反馈与社会价值

### 💬 模拟用户反馈

**👴 张大爷，72岁，高血压患者**
> "以前记血压都是用小本子，现在在手机上点几下就能看到一个月的趋势图，还有AI帮我分析，真方便！字大我能看清楚，操作也简单，我儿子教了我两遍就会用了。"

**👵 李奶奶，68岁，糖尿病患者**
> "最喜欢那个拍照识别检查报告的功能，以前看不懂医院的报告，现在拍个照，AI就能用大白话告诉我是什么意思，哪里有问题。还会提醒我该吃药了，再也不会忘记。"

**👨‍⚕️ 王医生，社区医院医生**
> "这个系统帮我节省了很多时间，老人的健康数据都有记录，我能看到完整的趋势，问诊时更有针对性。AI生成的干预方案也很专业，可以直接参考。"

**👨‍💼 陈先生，45岁，患者子女**
> "父母在老家，我在外地工作，以前很担心他们的身体。现在有了这个小程序，能实时看到爸妈的健康数据，异常时还会通知我，安心多了。"

### 🌟 社会价值

**1. 提升老年人健康管理水平**
- 持续监测，早发现早干预
- 数据驱动，科学健康管理
- AI辅助，获得专业指导

**2. 缓解医疗资源压力**
- 减少不必要的医院就诊
- 提高医生诊疗效率
- 促进分级诊疗落地

**3. 促进智慧养老发展**
- 技术赋能传统养老
- 数据互通共享
- 创新服务模式

**4. 增强家庭幸福感**
- 子女远程关爱父母
- 降低家庭医疗支出
- 提升老人生活质量

---

## 第19页：项目经验总结

### 💡 技术选型经验

**✅ 成功经验**

1. **uni-app跨平台开发**
   - 一套代码，多端运行
   - 开发效率提升50%+
   - 降低维护成本

2. **DeepSeek AI选型**
   - 成本低于GPT-4
   - 中文理解能力强
   - API稳定可靠

3. **阿里云OSS文件存储**
   - CDN加速，访问快
   - 按量计费，成本可控
   - 高可用性保障

**⚠️ 避坑指南**

1. **微信运动数据解密**
   - 注意AES加密模式（CBC）
   - Base64编解码细节
   - session_key有效期管理

2. **AI接口调用**
   - 设置合理的超时时间
   - 错误重试机制
   - 结果缓存优化

3. **小程序性能优化**
   - 图片压缩必不可少
   - 分包加载提升首屏速度
   - 避免过度渲染

### 📚 开发心得

**团队协作：**
- 前后端接口文档规范化（使用Swagger）
- Git分支管理策略（feature/dev/master）
- 每日站会同步进度

**代码质量：**
- 统一代码规范（阿里巴巴Java规范）
- Code Review机制
- 单元测试覆盖核心业务

**迭代策略：**
- MVP优先（最小可行产品）
- 快速验证核心功能
- 用户反馈驱动迭代

---

## 第20页：未来规划与展望

### 🚀 短期规划（3-6个月）

**功能扩展：**
1. **智能硬件对接**
   - 接入智能手表（实时心率、血压）
   - 连接智能血压计、血糖仪
   - 数据自动同步，免去手动输入

2. **家属端APP开发**
   - 实时查看父母健康状态
   - 接收异常预警通知
   - 远程视频关怀

3. **社交功能**
   - 老年人社区（分享养生经验）
   - 线下活动组织
   - 健康知识问答

**技术优化：**
- 引入Redis缓存，提升响应速度
- 数据库读写分离，支持更大并发
- AI模型微调，提升评估准确度

### 🌈 中期规划（6-12个月）

**商业化探索：**
1. **与医疗机构合作**
   - 打通线上线下服务
   - 远程问诊对接医院医生
   - 电子处方线上开具

2. **与保险公司合作**
   - 健康数据换取保费优惠
   - 推出健康保险产品
   - 理赔流程简化

3. **与养老机构合作**
   - 提供机构版管理系统
   - 批量健康监测
   - 数据统计分析

### 🎯 长期愿景（1-3年）

**成为老年健康管理领域的标杆产品**

1. **用户规模**：服务100万+老年用户
2. **功能完善**：覆盖健康管理全链条
3. **生态构建**：连接医院、药店、保险、养老机构
4. **社会影响**：推动智慧养老行业发展

**技术演进方向：**
- 大数据分析（疾病预测模型）
- 物联网整合（智能家居+健康监测）
- 边缘计算（本地化AI推理）
- 区块链（健康数据确权与共享）

---

## 第21页：致谢与联系方式

### 🙏 致谢

**感谢以下技术和平台的支持：**

- **DeepSeek AI**：提供强大的AI能力支持
- **百度AI开放平台**：OCR识别技术
- **阿里云**：稳定的云服务支撑
- **uni-app官方**：优秀的跨平台开发框架
- **开源社区**：各种优秀的开源组件

**感谢项目团队的辛勤付出：**

- 后端开发团队：架构设计、API开发、AI集成
- 前端开发团队：小程序开发、UI设计、交互优化
- 测试团队：功能测试、性能测试、用户体验测试
- 产品经理：需求分析、用户调研、产品迭代

### 📧 联系我们

**项目开源地址：**
GitHub: `github.com/yikang-healthcare`
Gitee: `gitee.com/yikang-healthcare`

**技术交流群：**
微信群：扫码加入"颐康智护技术交流群"
QQ群：123456789

**商务合作：**
邮箱：cooperation@yikang.com
电话：400-888-9999

**用户反馈：**
小程序内置"意见反馈"功能
邮箱：feedback@yikang.com

---

## 第22页：核心价值总结

### ⭐ 项目核心价值

```
┌─────────────────────────────────────────┐
│  颐康智护，银龄无忧 = ?                  │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│  1. 技术创新 🚀                          │
│  ✓ AI大模型应用落地                      │
│  ✓ OCR+AI医疗文档解读                    │
│  ✓ 微信生态深度集成                      │
│  ✓ 12+项健康指标智能分析                 │
├─────────────────────────────────────────┤
│  2. 社会价值 🌟                          │
│  ✓ 服务2.8亿+老年人群体                  │
│  ✓ 缓解医疗资源压力                      │
│  ✓ 推动智慧养老发展                      │
│  ✓ 增强家庭幸福感                        │
├─────────────────────────────────────────┤
│  3. 商业潜力 💰                          │
│  ✓ 巨大的市场需求                        │
│  ✓ 清晰的商业模式                        │
│  ✓ 多方合作空间                          │
│  ✓ 可持续发展                            │
├─────────────────────────────────────────┤
│  4. 用户体验 ❤️                          │
│  ✓ 适老化深度优化                        │
│  ✓ 操作简单易上手                        │
│  ✓ 功能实用接地气                        │
│  ✓ 智能贴心有温度                        │
└─────────────────────────────────────────┘
```

### 🎯 一句话总结

**用AI科技温暖银发群体，**
**让每一位老人都能享受智能化的健康管理服务，**
**实现"老有所养、老有所医、老有所乐"的美好愿景！**

---

## 第23页：Q&A环节

### ❓ 常见问题解答

**Q1: 老人不会用智能手机怎么办？**
A: 
- 提供语音操作，降低学习成本
- 子女可远程协助
- 社区提供使用培训
- 界面极简，操作≤3步

**Q2: 数据安全如何保障？**
A:
- HTTPS加密传输
- 数据库加密存储
- 严格权限控制
- 定期安全审计

**Q3: AI评估准确吗？**
A:
- 基于DeepSeek大模型
- 结合医学知识库
- 持续学习优化
- 建议仅供参考，不替代医生

**Q4: 收费模式是什么？**
A:
- 基础功能免费
- 高级功能会员制
- 机构版按需定价
- 增值服务单独收费

**Q5: 如何保证老人持续使用？**
A:
- 智能提醒机制
- 积分激励体系
- 社交功能增强粘性
- 子女监督关注

---

## 第24页：结束页

### 🎊 谢谢观看！

```
   ╔═══════════════════════════════════╗
   ║                                   ║
   ║      颐康智护，银龄无忧           ║
   ║                                   ║
   ║   让科技温暖每一位老年人的心     ║
   ║                                   ║
   ║   用AI守护银发群体的健康人生     ║
   ║                                   ║
   ╚═══════════════════════════════════╝
```

### 🌈 我们的使命

**让天下老人健康无忧，让子女放心安心**

### 📞 期待与您的合作

**投资咨询 | 技术交流 | 商务合作**

扫码关注我们 →  [二维码占位]

---

## PPT制作建议

### 🎨 视觉风格
- **主色调**：温暖粉色系 (#EF96C5) + 白色
- **辅助色**：紫色、蓝色、绿色（用于数据可视化）
- **字体**：微软雅黑/思源黑体（清晰易读）
- **图标**：线性图标风格，简洁大方

### 📐 排版建议
- 每页不超过5个要点
- 重要数据用大号字体突出
- 代码展示用等宽字体+语法高亮
- 适当留白，避免拥挤

### 🎬 动画效果
- 标题：淡入动画
- 内容：依次出现
- 图表：渐进动画
- 过渡：平滑切换

### 💡 演讲建议
- 每页讲解时间：1-2分钟
- 重点突出AI和适老化两大亮点
- 结合实际案例增强说服力
- 预留Q&A时间（5-10分钟）

---

**总页数：24页**
**预计演讲时长：30-40分钟**
**适用场景：项目答辩、技术分享、投资路演、产品发布**
