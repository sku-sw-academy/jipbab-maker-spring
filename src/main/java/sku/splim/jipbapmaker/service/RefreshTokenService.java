package sku.splim.jipbapmaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.RefreshToken;
import sku.splim.jipbapmaker.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}