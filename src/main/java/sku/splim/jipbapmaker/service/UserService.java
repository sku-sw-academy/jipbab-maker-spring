package sku.splim.jipbapmaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.config.jwt.TokenProvider;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.AddUserRequest;
import sku.splim.jipbapmaker.dto.AuthLoginRequest;
import sku.splim.jipbapmaker.dto.AuthLoginResponse;
import sku.splim.jipbapmaker.repository.UserRepository;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public AuthLoginResponse login(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        if (bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 인증에 성공하면 토큰을 생성하고 반환
            Duration tokenExpirationDuration = Duration.ofDays(1); // 1일 동안 유효
            String accessToken = tokenProvider.generateToken(user, tokenExpirationDuration);
            return AuthLoginResponse.of(user.getId(), accessToken);
        } else {
            // 비밀번호가 일치하지 않는 경우 예외 발생
            throw new BadCredentialsException("Invalid email or password");
        }
    }

}
