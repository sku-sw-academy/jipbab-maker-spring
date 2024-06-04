package sku.splim.jipbapmaker.dto.gpt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GptChatResponse {
    private String content;
}