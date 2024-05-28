package sku.splim.jipbapmaker.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthLoginResponse {
    Long id;
    String accessToken;

    public static AuthLoginResponse of(Long id, String accessToken) {
        return AuthLoginResponse.builder()
                .id(id)
                .accessToken(accessToken)
                .build();
    }
}