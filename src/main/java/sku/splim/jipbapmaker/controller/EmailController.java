package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/auth")
    public String sendAuthEmail(@RequestParam("to") String to) {
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendAuthMessage(to, verificationCode);
        // 전송된 인증번호를 클라이언트에게 반환
        return verificationCode;
    }
}
