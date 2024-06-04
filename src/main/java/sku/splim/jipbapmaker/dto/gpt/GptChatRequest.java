package sku.splim.jipbapmaker.dto.gpt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class GptChatRequest {
    private Long id;
    private String thriftyItems;  // 알뜰
}

