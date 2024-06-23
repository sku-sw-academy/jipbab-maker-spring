package sku.splim.jipbapmaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.*;
import sku.splim.jipbapmaker.dto.AdminDTO;
import sku.splim.jipbapmaker.dto.FAQDTO;
import sku.splim.jipbapmaker.dto.QuestionDTO;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.service.QuestionService;
import sku.splim.jipbapmaker.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/question")
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;

    @GetMapping("/all/{userId}")
    public List<QuestionDTO> getQuestions(@PathVariable("userId") long userId){
        List<Question> questions = questionService.findAllByUserIdOrderByCreateDateDesc(userId);
        List<QuestionDTO> dtos = new ArrayList<>();

        for(Question question : questions){
            QuestionDTO dto = new QuestionDTO();
            dto.setId(question.getId());
            dto.setTitle(question.getTitle());
            dto.setContent(question.getContent());
            dto.setStatus(question.isStatus());
            dto.setModifyDate(question.getModifyDate());
            dto.setCreateDate(question.getCreateDate());
            dto.setDeletedAt(question.getDeletedAt());
            UserDTO userDTO = new UserDTO();
            dto.setUserDTO(userDTO.convertToDTO(question.getUser()));
            dtos.add(dto);
        }

        return dtos;
    }

    @GetMapping("/all/false")
    public List<QuestionDTO> getQuestionsFalse(){
        List<Question> questions = questionService.findAllByStatusFalse();
        List<QuestionDTO> dtos = new ArrayList<>();

        for(Question question : questions){
            QuestionDTO dto = new QuestionDTO();
            dto.setId(question.getId());
            dto.setTitle(question.getTitle());
            dto.setContent(question.getContent());
            dto.setStatus(question.isStatus());
            dto.setModifyDate(question.getModifyDate());
            dto.setDeletedAt(question.getDeletedAt());
            UserDTO userDTO = new UserDTO();
            dto.setUserDTO(userDTO.convertToDTO(question.getUser()));
            dtos.add(dto);
        }
        return dtos;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam("id") long id, @RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            User user = userService.findById(id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
            }

            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setUser(user);
            question.setStatus(false);
            questionService.save(question);

            return ResponseEntity.ok("Ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable("id") long id) {
        try {
            // id에 해당하는 질문을 soft delete 하고 업데이트된 항목의 수를 반환
            boolean isDeleted = questionService.softDeleteQuestionById(id);
            if (!isDeleted) {
                return ResponseEntity.notFound().build();
            }
            // 삭제 성공 시 200 OK 응답 반환
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 서버 오류 시 500 Internal Server Error 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete question");
        }
    }

}
