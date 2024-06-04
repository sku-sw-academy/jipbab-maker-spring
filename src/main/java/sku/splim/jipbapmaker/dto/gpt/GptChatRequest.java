package sku.splim.jipbapmaker.dto.gpt;

import com.theokanning.openai.completion.chat.ChatMessage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class GptChatRequest {
    private List<ChatMessage> chatMessages;
}
