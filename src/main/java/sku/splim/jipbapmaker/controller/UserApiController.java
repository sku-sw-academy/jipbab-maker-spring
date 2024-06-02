package sku.splim.jipbapmaker.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.AddUserRequest;
import sku.splim.jipbapmaker.dto.AuthLoginRequest;
import sku.splim.jipbapmaker.dto.AuthLoginResponse;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.repository.UserRepository;
import sku.splim.jipbapmaker.service.ItemService;
import sku.splim.jipbapmaker.service.PreferenceService;
import sku.splim.jipbapmaker.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController  // 변경: @Controller -> @RestController
public class UserApiController {
    private final UserService userService;

    @Autowired
    private final PreferenceService preferenceService;

    private final UserRepository userRepository;

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

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/user")
    public UserDTO getUser() {
        // 가짜 데이터 생성 (실제 데이터는 서비스에서 가져와야 함)
        Optional<User> optionalUser = userRepository.findByEmail("limjh070@naver.com");
        User user = optionalUser.get();
        return UserDTO.convertToDTO(user);
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


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("userId") Long userId, @RequestParam("image") MultipartFile imageFile) {
        String uploadDir = "src/main/resources/static/assets/profile/";

        try {
            // 파일 저장
            Path filePath = Paths.get(uploadDir, imageFile.getOriginalFilename());
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 URL 생성
            String fileName = imageFile.getOriginalFilename();

            // 사용자 프로필 업데이트
            userService.updateUserProfile(userId, fileName);

            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        try {
            Path filePath = Paths.get("src/main/resources/static/assets/profile/").resolve(filename).normalize();
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
}
