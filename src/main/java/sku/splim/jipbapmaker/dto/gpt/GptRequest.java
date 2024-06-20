package sku.splim.jipbapmaker.dto.gpt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GptRequest {
    private Long id;
    private String thriftyItems;  // 알뜰
}

