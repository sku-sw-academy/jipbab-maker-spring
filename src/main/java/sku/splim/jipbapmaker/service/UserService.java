package sku.splim.jipbapmaker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.config.jwt.TokenProvider;
import sku.splim.jipbapmaker.domain.RefreshToken;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.AddUserRequest;
import sku.splim.jipbapmaker.dto.AuthLoginRequest;
import sku.splim.jipbapmaker.dto.AuthLoginResponse;
import sku.splim.jipbapmaker.repository.RefreshTokenRepository;
import sku.splim.jipbapmaker.repository.UserRepository;
import java.util.*;
import java.time.Duration;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
            Duration accessTokenExpirationDuration = Duration.ofHours(1); // 1시간 동안 유효
            Duration refreshTokenExpirationDuration = Duration.ofDays(1); // 1일 동안 유효
            String accessToken = tokenProvider.generateToken(user, accessTokenExpirationDuration);
            String refreshToken = tokenProvider.generateToken(user, refreshTokenExpirationDuration);
            refreshTokenRepository.save(RefreshToken.builder()
                    .user(user)
                    .refreshToken(refreshToken)
                    .build());
            return AuthLoginResponse.of(user.getId(), accessToken, refreshToken);
        } else {
            // 비밀번호가 일치하지 않는 경우 예외 발생
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Transactional
    public void logout(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public List<String> getEmails(){
        return userRepository.findAllEmails();
    }

    public void updateUserProfile(long id, String fileUrl){
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfile(fileUrl);
            userRepository.save(user);
            System.out.println("User profile updated successfully");
        } else {
            System.out.println("User not found with ID: " + id);
            // 사용자를 찾을 수 없는 경우 예외 처리 또는 다른 처리를 수행할 수 있습니다.
        }
    }

    public void changeNickname(long id, String nickname){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNickname(nickname);
            userRepository.save(user);
        }
    }

    public String ChangePassword(long id, String current, String newPassword){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(bCryptPasswordEncoder.matches(current, user.getPassword())){
                user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                userRepository.save(user);
                return bCryptPasswordEncoder.encode(newPassword);
            }else
                return "Wrong password";
        }
        return "No such user";
    }

    public String ResetProfile(long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setProfile("");
            userRepository.save(user);
            return "Ok";
        }
        return "No such user";
    }

}
