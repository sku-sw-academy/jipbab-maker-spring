package sku.splim.jipbapmaker.dto.gpt;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GptChatResponse {
    String title;
    String content;
}