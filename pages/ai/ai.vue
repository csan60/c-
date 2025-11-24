<template>
  <view class="container">
    <!-- 顶部标题栏 -->
    <view class="header">
      <text class="title">AI智能问答</text>
      <view class="subtitle">随时解答您的各种问题</view>
    </view>
    
    <!-- 聊天内容区域 -->
    <scroll-view 
      class="chat-container" 
      scroll-y 
      :scroll-top="scrollTop"
      @scroll="handleScroll"
    >
      <view 
        v-for="message in messages" 
        :key="message.id"
        :class="['message', message.role]"
      >
        <image 
          class="avatar" 
          :src="message.role === 'user' ? userAvatar : aiAvatar"
        ></image>
        <view :class="['bubble', message.role]">
          <text>{{ message.content }}</text>
          <view class="time">{{ message.time }}</view>
        </view>
      </view>
      
      <!-- 加载状态 -->
      <view class="loading" v-if="isLoading">
        <view class="dot-flashing"></view>
        <text>AI正在思考中...</text>
      </view>
    </scroll-view>
    
    <!-- 底部输入区域 -->
    <view class="input-area">
      <input 
        class="input" 
        placeholder="输入您的问题..." 
        confirm-type="send" 
        v-model="inputValue"
      />
      <button 
        class="send-btn" 
        v-if="!isLoading && inputValue.length>0"
        @click="sendMessage" 
      >
        发送
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      messages: [
        {
          id: 1,
          role: 'assistant',
          content: '您好！我是AI助手，有什么可以帮您的吗？',
          time: this.getCurrentTime()
        }
      ],
      inputValue: '',
      isLoading: false,
      scrollTop: 0,
      autoFocus: true
    }
  },
  mounted() {
    this.scrollToBottom();
  },
  methods: {
    sendMessage() {
      const content = this.inputValue.trim()
      if (content.length===0 || this.isLoading) {
        uni.showToast({
          title: "请输入问题内容",
          icon: 'none'
        })
        return
      }
      this.scrollToBottom()
      const userMessage = {
        id: Date.now(),
        role: 'user',
        content: content,
        time: this.getCurrentTime()
      }
      
      this.messages = [...this.messages, userMessage]
      this.inputValue = ''
      this.isLoading = true
      
      this.getAIResponse(content)
    .then(aiResponse => {
      const aiMessage = {
        id: Date.now() + 1,
        role: 'assistant',
        content: aiResponse,
        time: this.getCurrentTime()
      };
      
      this.messages = [...this.messages, aiMessage];
    })
    .catch(error => {
      console.error('获取AI回复失败:', error);
      // 可以添加错误处理消息
    })
    .finally(() => {
      this.isLoading = false;
      this.scrollToBottom();
    });
    },
    scrollToBottom() {
      this.$nextTick(() => {
        this.scrollTop = 999999
      })
    },
    getCurrentTime() {
      const now = new Date()
      const hours = now.getHours().toString().padStart(2, '0')
      const minutes = now.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    },
    async getAIResponse(question) {
      try {
        const res = await this.$http.get('zixun?problem='+question)
        console.log('AI接口返回:', res)
        if (res.code === 0) {
         return res.data;
        }else{
          uni.showToast({
          title: res.data,
          icon: 'none'
        })
        }
      } catch (e) {
        console.error('AI接口请求失败:', e)
        uni.showToast({
          title: 'AI服务暂不可用，请稍后重试',
          icon: 'none'
        })
      }
      
      return `关于"${question}"，这是一个很好的问题。根据我的分析，建议您可以进一步了解相关知识，或者提供更具体的信息，我可以给出更精准的回答。`
    },
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}

/* 头部样式 */
.header {
  padding: 20rpx 30rpx;
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  color: white;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.title {
  font-size: 36rpx;
  font-weight: bold;
}

.subtitle {
  font-size: 24rpx;
  opacity: 0.9;
  margin-top: 8rpx;
}

/* 聊天容器 */
.chat-container {
  flex: 1;
  padding: 20rpx 30rpx;
  overflow: hidden;
  box-sizing: border-box;
}

/* 消息通用样式 */
.message {
  display: flex;
  margin-bottom: 30rpx;
}

.message.user {
  justify-content: flex-end;
}

.message.assistant {
  justify-content: flex-start;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.bubble {
  max-width: 70%;
  padding: 20rpx 24rpx;
  border-radius: 16rpx;
  position: relative;
  word-break: break-word;
  font-size: 28rpx;
  line-height: 1.5;
}

/* 用户消息气泡 */
.bubble.user {
  background-color: #6e8efb;
  color: white;
  border-top-left-radius: 0;
  margin-left: 20rpx;
}

/* AI消息气泡 */
.bubble.assistant {
  background-color: white;
  color: #333;
  border-top-right-radius: 0;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
  margin-right: 20rpx;
}

.time {
  font-size: 20rpx;
  color: #999;
  margin-top: 10rpx;
  text-align: right;
}

.bubble.user .time {
  color: rgba(255, 255, 255, 0.7);
}

.ai-tag {
  position: absolute;
  top: -16rpx;
  left: 20rpx;
  background-color: #a777e3;
  color: white;
  font-size: 20rpx;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
}

/* 加载状态 */
.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx;
  color: #999;
  font-size: 26rpx;
}

.dot-flashing {
  position: relative;
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background-color: #a777e3;
  color: #a777e3;
  animation: dot-flashing 1s infinite linear alternate;
  animation-delay: 0.5s;
  margin-right: 15rpx;
}

.dot-flashing::before, .dot-flashing::after {
  content: "";
  display: inline-block;
  position: absolute;
  top: 0;
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background-color: #a777e3;
  color: #a777e3;
}

.dot-flashing::before {
  left: -30rpx;
  animation: dot-flashing 1s infinite alternate;
  animation-delay: 0s;
}

.dot-flashing::after {
  left: 30rpx;
  animation: dot-flashing 1s infinite alternate;
  animation-delay: 1s;
}

@keyframes dot-flashing {
  0% {
    opacity: 0.3;
    transform: scale(0.8);
  }
  50%, 100% {
    opacity: 1;
    transform: scale(1);
  }
}

/* 输入区域 */
.input-area {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: white;
  border-top: 1rpx solid #eee;
}

.input {
  flex: 1;
  background-color: #f5f7fa;
  border-radius: 40rpx;
  padding: 20rpx 30rpx;
  font-size: 28rpx;
  margin-right: 20rpx;
  height: 80rpx;
  box-sizing: border-box;
}

.send-btn {
  width: 120rpx;
  height: 80rpx;
  border-radius: 40rpx;
  background-color: #6e8efb;
  color: white;
  font-size: 28rpx;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
  border: none;
}

.send-btn:active {
  transform: scale(0.95);
}

.send-btn[disabled] {
  background-color: #ccc;
  color: #999;
}
</style>