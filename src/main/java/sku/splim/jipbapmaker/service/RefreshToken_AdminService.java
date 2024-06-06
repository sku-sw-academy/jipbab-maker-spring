package sku.splim.jipbapmaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.RefreshToken_admin;
import sku.splim.jipbapmaker.repository.RefreshToken_AdminRepository;

@RequiredArgsConstructor
@Service
public class RefreshToken_AdminService {
    private final RefreshToken_AdminRepository refreshToken_adminRepository;

    public RefreshToken_admin findByRefreshToken(String refreshToken) {
        return refreshToken_adminRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
