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
    private SecureRandom random = new SecureRandom();
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_+=<>?/";

    public String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // 소문자, 대문자, 특수문자 포함하여 랜덤하게 생성
        for (int i = 0; i < length; i++) {
            int type = random.nextInt(4); // 0: 소문자, 1: 대문자, 2: 특수문자

            switch (type) {
                case 0:
                    password.append(LOWERCASE_CHARS.charAt(random.nextInt(LOWERCASE_CHARS.length())));
                    break;
                case 1:
                    password.append(UPPERCASE_CHARS.charAt(random.nextInt(UPPERCASE_CHARS.length())));
                    break;
                case 2:
                    password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
                    break;
                case 3:
                    password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
                    break;
            }
        }

        return password.toString();
    }


    public String generateVerificationCode(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
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

    public void sendPassword(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("임시 비밀번호입니다.");
        message.setText("인증번호: " + verificationCode);
        mailSender.send(message);
    }
}
