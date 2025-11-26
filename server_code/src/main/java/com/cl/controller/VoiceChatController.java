package com.cl.controller;

import com.cl.entity.ChatMessage;
import com.cl.entity.ChatMessageResponse;
import com.cl.entity.VoiceChatResponse;
import com.cl.service.AsrService;
import com.cl.service.ChatSessionService;
import com.cl.service.LlmService;
import com.cl.service.TtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/voice")
public class VoiceChatController {

    @Autowired
    private AsrService asrService;

    @Autowired
    private LlmService llmService;

    @Autowired
    private TtsService ttsService;

    @Autowired
    private ChatSessionService chatSessionService;

    // 音频保存位置（请自己修改）
    private static final String AUDIO_SAVE_PATH = "C:/ai_audio/";
    private static final String AUDIO_URL_PREFIX = "http://localhost:8080/audio/";

    @PostMapping("/chat")
    public VoiceChatResponse chat(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam("sessionId") String sessionId
    ) throws Exception {

        VoiceChatResponse resp = new VoiceChatResponse();

        /** ---------------------- 1. 语音识别 ---------------------- */
        String userText = asrService.recognize(audioFile.getBytes());
        resp.setUserText(userText);

        /** ---------------------- 2. 加入聊天上下文 ---------------------- */
        chatSessionService.appendMessage(sessionId, new ChatMessageResponse("user", userText));
        List<ChatMessageResponse> history = chatSessionService.getHistory(sessionId);

        /** ---------------------- 3. DeepSeek 生成 AI 文本 ---------------------- */
        String assistantText = llmService.chat(sessionId, userText, history);
        resp.setAssistantText(assistantText);

        // 保存到会话
        chatSessionService.appendMessage(sessionId, new ChatMessageResponse("assistant", assistantText));

        /** ---------------------- 4. 讯飞 TTS 将文本转成语音 ---------------------- */
        byte[] mp3Bytes = ttsService.synthesize(assistantText);

        String fileName = "ai_" + System.currentTimeMillis() + ".mp3";
        Files.write(Paths.get(AUDIO_SAVE_PATH + fileName), mp3Bytes);

        String audioUrl = AUDIO_URL_PREFIX + fileName;
        resp.setAssistantAudioUrl(audioUrl);

        return resp;
    }
}
