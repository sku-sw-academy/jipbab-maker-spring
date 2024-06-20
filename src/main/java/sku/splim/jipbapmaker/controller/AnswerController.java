package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.Answer;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.AdminDTO;
import sku.splim.jipbapmaker.dto.AnswerDTO;
import sku.splim.jipbapmaker.dto.QuestionDTO;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/answer")
public class AnswerController {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private FCMService fcmService;
    @Autowired
    private NotificationListService notificationListService;

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

            if(question.getUser().isPush() && question.getUser().isLog()){
                FCMService.sendFCMMessage(question.getUser().getFcmToken(), "문의답변 완료","문의에 답변이 완료되었습니다.");
                // 성공적으로 전송된 메시지 반환
                notificationListService.save(question.getUser(), "문의답변 완료", "문의에 답변이 완료되었습니다.");
            }

            return ResponseEntity.ok("Answer successfully sent.");
        } catch (Exception e) {
            // 오류가 발생한 경우
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send answer.");
        }
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<AnswerDTO>> getAllAnswer(@PathVariable("id") long id) {
        List<Answer> answers = answerService.getAnswers(id);
        List<AnswerDTO> answerDTOS = new ArrayList<>();

        for(Answer answer : answers) {
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setId(answer.getId());
            answerDTO.setContent(answer.getContent());
            answerDTO.setModifyDate(answer.getModifyDate());
            UserDTO userDTO = new UserDTO();
            QuestionDTO questionDTO = new QuestionDTO();
            AdminDTO adminDTO = new AdminDTO();
            answerDTO.setAdmin(adminDTO.convertToDTO(answer.getAdmin()));
            answerDTO.setUser(userDTO.convertToDTO(answer.getUser()));
            answerDTO.setQuestion(questionDTO.convertToDTO(answer.getQuestion()));
            answerDTOS.add(answerDTO);
        }

        return ResponseEntity.ok(answerDTOS);
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<AnswerDTO> getAnswer(@PathVariable("id") long id) {
        Answer answer = answerService.getQuestionAnswer(id);
        if (answer == null) {
            return ResponseEntity.ok().body(null);
        }

        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answer.getId());
        answerDTO.setContent(answer.getContent());
        answerDTO.setModifyDate(answer.getModifyDate());
        UserDTO userDTO = new UserDTO();
        QuestionDTO questionDTO = new QuestionDTO();
        AdminDTO adminDTO = new AdminDTO();
        answerDTO.setAdmin(adminDTO.convertToDTO(answer.getAdmin()));
        answerDTO.setUser(userDTO.convertToDTO(answer.getUser()));
        answerDTO.setQuestion(questionDTO.convertToDTO(answer.getQuestion()));

        return ResponseEntity.ok().body(answerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable("id") Long id) {
        try {
            Answer answer = answerService.findById(id);
            answerService.delete(answer);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete answer");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAnswer(@RequestBody Map<String, Object> payload) {
        Long id = ((Number) payload.get("id")).longValue();
        String content = (String) payload.get("content");

        boolean isUpdated = answerService.updateAnswer(id, content);

        if (isUpdated) {
            return ResponseEntity.ok("답변이 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.status(400).body("답변 수정에 실패했습니다.");
        }
    }
}
