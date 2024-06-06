package sku.splim.jipbapmaker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.config.jwt.TokenProvider;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.RefreshToken;
import sku.splim.jipbapmaker.domain.RefreshToken_admin;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.AddUserRequest;
import sku.splim.jipbapmaker.dto.AuthLoginRequest;
import sku.splim.jipbapmaker.dto.AuthLoginResponse;
import sku.splim.jipbapmaker.repository.AdminRepository;
import sku.splim.jipbapmaker.repository.RefreshTokenRepository;
import sku.splim.jipbapmaker.repository.RefreshToken_AdminRepository;
import sku.splim.jipbapmaker.repository.UserRepository;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshToken_AdminRepository refreshToken_adminRepository;
    private final LogService logService;

    public Long save(String email, String name, String passwrod) {
        return adminRepository.save(Admin.builder()
                .email(email)
                .name(name)
                .password(bCryptPasswordEncoder.encode(passwrod))
                .build()).getId();
    }

    public Admin findById(Long userId) {
        return adminRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected admin"));
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + request.getEmail()));

        if (bCryptPasswordEncoder.matches(request.getPassword(), admin.getPassword())) {
            // 인증에 성공하면 토큰을 생성하고 반환
            Duration accessTokenExpirationDuration = Duration.ofHours(1); // 1시간 동안 유효
            Duration refreshTokenExpirationDuration = Duration.ofDays(1); // 1일 동안 유효
            String accessToken = tokenProvider.generateToken(admin, accessTokenExpirationDuration);
            String refreshToken = tokenProvider.generateToken(admin, refreshTokenExpirationDuration);
            refreshToken_adminRepository.save(RefreshToken_admin.builder()
                    .admin(admin)
                    .refreshToken(refreshToken)
                    .build());
            logService.login(admin);
            return AuthLoginResponse.of(admin.getId(), accessToken, refreshToken);
        } else {
            // 비밀번호가 일치하지 않는 경우 예외 발생
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    public AuthLoginResponse login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));

        if (bCryptPasswordEncoder.matches(password, admin.getPassword())) {
            // 인증에 성공하면 토큰을 생성하고 반환
            Duration accessTokenExpirationDuration = Duration.ofHours(1); // 1시간 동안 유효
            Duration refreshTokenExpirationDuration = Duration.ofDays(1); // 1일 동안 유효
            String accessToken = tokenProvider.generateToken(admin, accessTokenExpirationDuration);
            String refreshToken = tokenProvider.generateToken(admin, refreshTokenExpirationDuration);
            refreshToken_adminRepository.save(RefreshToken_admin.builder()
                    .admin(admin)
                    .refreshToken(refreshToken)
                    .build());
            logService.login(admin);
            return AuthLoginResponse.of(admin.getId(), accessToken, refreshToken);
        } else {
            // 비밀번호가 일치하지 않는 경우 예외 발생
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Transactional
    public void logout(Admin admin) {
        logService.logOut(admin);
        refreshToken_adminRepository.deleteByAdmin(admin);
    }
}
