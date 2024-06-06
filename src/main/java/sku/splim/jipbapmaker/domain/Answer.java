package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.AnswerDTO;
import sku.splim.jipbapmaker.dto.QuestionDTO;

import java.sql.Timestamp;

@Table(name = "answer")
@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    public Answer(){

    }

    public Answer convertToEntity(AnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setId(answerDTO.getId());
        answer.setContent(answerDTO.getContent());
        answer.setModifyDate(answerDTO.getModifyDate());

        User user = new User();
        Question question1 = new Question();
        Admin admin1 = new Admin();

        answer.setUser(user.convertToEntity(answerDTO.getUser()));
        answer.setQuestion(question1.convertToEntity(answerDTO.getQuestion()));
        answer.setAdmin(admin1.convertToEntity(answerDTO.getAdmin()));

        return answer;
    }
}
