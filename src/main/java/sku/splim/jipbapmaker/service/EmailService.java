package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 6;
    private SecureRandom random = new SecureRandom();

    public String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public void sendAuthMessage(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("이메일 인증입니다.");
        message.setText("인증번호: " + verificationCode);
        mailSender.send(message);
    }
}
