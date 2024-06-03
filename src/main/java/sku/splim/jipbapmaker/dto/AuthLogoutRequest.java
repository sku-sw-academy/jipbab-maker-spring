package sku.splim.jipbapmaker.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthLogoutRequest {
    private Long id;
    private String refreshToken;
}