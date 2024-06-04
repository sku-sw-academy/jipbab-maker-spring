package sku.splim.jipbapmaker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthLogoutRequest {
    private Long id;
    private String refreshToken;

    public AuthLogoutRequest(@JsonProperty("id") Long id, @JsonProperty("refreshToken") String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
    }
}