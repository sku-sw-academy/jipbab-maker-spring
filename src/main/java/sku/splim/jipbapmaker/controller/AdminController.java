package sku.splim.jipbapmaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.dto.AuthLoginRequest;
import sku.splim.jipbapmaker.dto.AuthLoginResponse;
import sku.splim.jipbapmaker.dto.AuthLogoutRequest;
import sku.splim.jipbapmaker.service.AdminService;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("password") String password) {
        adminService.save(email, name, password);

        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest request) {
        AuthLoginResponse response = adminService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody AuthLogoutRequest request) {
        // 요청에 포함된 사용자 정보를 기반으로 로그아웃 처리
        Admin admin = adminService.findById(request.getId());
        if (admin != null) {
            adminService.logout(admin); // 리프레시 토큰 삭제
            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그아웃 실패: 유저 정보를 찾을 수 없음");
        }
    }

}
