package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.repository.UserRepository;
import sku.splim.jipbapmaker.service.EmailService;

import java.util.Optional;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/auth")
    public String sendAuthEmail(@RequestParam("to") String to) {
        String verificationCode = emailService.generateVerificationCode(6);
        emailService.sendAuthMessage(to, verificationCode);
        // 전송된 인증번호를 클라이언트에게 반환
        return verificationCode;
    }

    @PostMapping("/password")
    public String sendPassWord(@RequestParam("to") String to, @RequestParam("nickname") String nickname) {
        // 사용자가 존재하는지 확인
        Optional<User> userOptional = userRepository.findByEmailAndNickname(to, nickname);

        if (userOptional.isPresent()) {
            // 사용자가 존재하는 경우에만 비밀번호 발송
            User user = userOptional.get();
            String verificationCode = emailService.generatePassword(8);
            user.setPassword(bCryptPasswordEncoder.encode(verificationCode));
            userRepository.save(user);
            emailService.sendPassword(to, verificationCode);
            // 전송된 인증번호를 클라이언트에게 반환
            return "Ok";
        } else {
            // 사용자가 존재하지 않는 경우 메시지 반환
            return "No";
        }
    }
}
