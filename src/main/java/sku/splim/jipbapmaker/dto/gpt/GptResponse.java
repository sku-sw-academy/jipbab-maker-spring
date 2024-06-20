package sku.splim.jipbapmaker.dto.gpt;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GptResponse {
    String title;
    String content;
    String imageUrl; // 이미지 URL 필드 추가

    public GptResponse(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}