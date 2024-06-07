package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Log;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.domain.User;

import java.sql.Timestamp;

@Setter
@Getter
public class QuestionDTO {

    private Long id;
    private UserDTO userDTO;
    private String title;
    private String content;
    private boolean status;
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    public QuestionDTO(){}

    public QuestionDTO(Long id, UserDTO userDTO, String title, String content, boolean status){
        this.id = id;
        this.userDTO = userDTO;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public QuestionDTO convertToDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setTitle(question.getTitle());
        questionDTO.setContent(question.getContent());
        questionDTO.setModifyDate(question.getModifyDate());
        questionDTO.setStatus(question.isStatus());
        UserDTO userDTO1 = new UserDTO();
        questionDTO.setUserDTO(userDTO1.convertToDTO(question.getUser()));
        return questionDTO;
    }
}
