package sku.splim.jipbapmaker.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthLoginRequest {
    private String email;
    private String password;
}
