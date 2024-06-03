package sku.splim.jipbapmaker.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthLoginResponse {
    Long id;
    String accessToken;
    String refreshToken;

    public static AuthLoginResponse of(Long id, String accessToken, String refreshToken) {
        return AuthLoginResponse.builder()
                .id(id)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}