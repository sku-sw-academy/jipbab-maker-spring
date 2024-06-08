package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.Answer;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.service.AdminService;
import sku.splim.jipbapmaker.service.AnswerService;
import sku.splim.jipbapmaker.service.QuestionService;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AdminService adminService;


    @PostMapping("/send")
    public ResponseEntity<String> sendAnswer(@RequestParam("id") long id, @RequestParam("adminId") long adminId, @RequestParam("content") String content) {
        try {
            // Answer 객체 생성
            Question question = questionService.findById(id);
            Admin admin = adminService.findById(adminId);
            Answer answer = new Answer();
            answer.setUser(question.getUser());
            answer.setAdmin(admin);
            answer.setContent(content);
            answer.setQuestion(question);
            answerService.save(answer);
            // 성공적으로 전송된 메시지 반환
            return ResponseEntity.ok("Answer successfully sent.");
        } catch (Exception e) {
            // 오류가 발생한 경우
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send answer.");
        }
    }
}
