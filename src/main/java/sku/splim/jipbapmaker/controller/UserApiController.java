package sku.splim.jipbapmaker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;
import sku.splim.jipbapmaker.domain.RefreshToken;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.*;
import sku.splim.jipbapmaker.repository.UserRepository;
import sku.splim.jipbapmaker.service.PreferenceService;
import sku.splim.jipbapmaker.service.RefreshTokenService;
import sku.splim.jipbapmaker.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController  // 변경: @Controller -> @RestController
public class UserApiController {
    private final UserService userService;

    @Autowired
    private final PreferenceService preferenceService;

    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody AddUserRequest request) {
        userService.save(request);
        preferenceService.savePreference(request.getEmail());
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest request) {
        AuthLoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody AuthLogoutRequest request) {
        // 요청에 포함된 사용자 정보를 기반으로 로그아웃 처리
        User user = userService.findById(request.getId());
        if (user != null) {
            userService.logout(user); // 리프레시 토큰 삭제
            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그아웃 실패: 유저 정보를 찾을 수 없음");
        }
    }

    @GetMapping("/emails")
    public List<String> getEmail() {
        // 가짜 데이터 생성 (실제 데이터는 서비스에서 가져와야 함)
        List<String> emails =userService.getEmails();
        return emails;
    }

    @PostMapping("/nickName")
    public ResponseEntity<String> changeNickName(@RequestParam("userId") Long userId, @RequestParam("NickName") String nickName) {
        userService.changeNickname(userId, nickName);
        return ResponseEntity.ok(nickName);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("userId") Long userId, @RequestParam("current") String current, @RequestParam("new") String newPassword) {
        String response = userService.ChangePassword(userId, current, newPassword);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-profile")
    public ResponseEntity<String> uploadImage(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(userService.ResetProfile(userId));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("userId") Long userId, @RequestParam("image") MultipartFile imageFile) {
        // 유효성 검사: 이미지 파일이 제공되었는지 확인
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please provide an image file");
        }

        String uploadDir = "/home/centos/app/assets/profile/";

        try {
            // 파일 저장
            String originalFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);

            // 디렉토리가 존재하지 않으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(originalFileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 생성
            String fileName = filePath.getFileName().toString();

            // 사용자 프로필 업데이트
            userService.updateUserProfile(userId, fileName);

            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        try {
            Path filePath = Paths.get("/home/centos/app/assets/profile/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/userInfo/{refreshToken}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable("refreshToken") String refreshToken) {
        try {
            Optional<RefreshToken> refreshTokenOptional = Optional.ofNullable(refreshTokenService.findByRefreshToken(refreshToken));
            RefreshToken refreshToken1 = refreshTokenOptional.get();
            User user = userService.findById(refreshToken1.getUserId());
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setNickname(user.getNickname());
            userDTO.setProfile(user.getProfile());
            userDTO.setLog(user.isLog());
            userDTO.setEnabled(user.isEnabled());
            userDTO.setPush(user.isPush());
            userDTO.setFcmToken(user.getFcmToken());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setNickname(user.getNickname());
            userDTO.setProfile(user.getProfile());
            userDTO.setLog(user.isLog());
            userDTO.setEnabled(user.isEnabled());
            userDTO.setPush(user.isPush());
            userDTO.setFcmToken(user.getFcmToken());
            userDTOs.add(userDTO);
        }
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }
}
