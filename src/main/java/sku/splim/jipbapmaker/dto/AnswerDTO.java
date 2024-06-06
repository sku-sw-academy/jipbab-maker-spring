package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Answer;
import java.sql.Timestamp;

@Getter
@Setter
public class AnswerDTO {
    private Long id;
    private UserDTO user;
    private AdminDTO admin;
    private QuestionDTO question;
    private String content;
    private Timestamp createDate;
    private Timestamp modifyDate;

    public AnswerDTO(){}

    public AnswerDTO(Long id, UserDTO userDTO, AdminDTO adminDTO, QuestionDTO questionDTO, String content){
        this.id = id;
        this.user = userDTO;
        this.admin = adminDTO;
        this.question = questionDTO;
        this.content = content;
    }

    public AnswerDTO convertToDTO(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(answer.getId());
        answerDTO.setContent(answer.getContent());
        answerDTO.setModifyDate(answer.getModifyDate());
        UserDTO userDTO1 = new UserDTO();
        AdminDTO adminDTO = new AdminDTO();
        QuestionDTO questionDTO = new QuestionDTO();
        answerDTO.setUser(userDTO1.convertToDTO(answer.getUser()));
        answerDTO.setQuestion(questionDTO.convertToDTO(answer.getQuestion()));
        answerDTO.setAdmin(adminDTO.convertToDTO(answer.getAdmin()));

        return answerDTO;
    }
}
