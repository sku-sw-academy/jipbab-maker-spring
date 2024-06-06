package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.QuestionDTO;
import sku.splim.jipbapmaker.dto.UserDTO;

import java.sql.Timestamp;

@Table(name = "question")
@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    public Question(){

    }

    public Question convertToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setId(question.getId());
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());
        question.setModifyDate(questionDTO.getModifyDate());
        User user = new User();
        question.setUser(user.convertToEntity(questionDTO.getUserDTO()));

        return question;
    }

}
