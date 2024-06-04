package sku.splim.jipbapmaker.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.config.GptProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class GptService {

    private final OpenAiService service;

    private final static String GPT_MODEL = "gpt-3.5-turbo";

    private static final Duration DURATION = Duration.ofSeconds(120);

    @Autowired
    public GptService(GptProperties gptProperties) {
        this.service = new OpenAiService(gptProperties.getApiKey(), DURATION);
    }

    private List<ChatMessage> getMessage(String content1, String content2) {

        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage message1 = new ChatMessage("system", content1);
        ChatMessage message2 = new ChatMessage("user", content2);

        messages.add(message1);
        messages.add(message2);

        return messages;
    }

    public String getGptAnswer(String content1, String content2) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(getMessage(content1, content2))
                .model(GPT_MODEL)
                .temperature(0.2)
                .build();

        // 받은 데이터
        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }

}

