package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.QuestionDTO;
import java.util.List;
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

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createDate = now;
        modifyDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = new Timestamp(System.currentTimeMillis());
    }

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    public Question(){

    }

    public Question convertToEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setId(question.getId());
        question.setTitle(questionDTO.getTitle());
        question.setContent(questionDTO.getContent());
        question.setModifyDate(questionDTO.getModifyDate());
        question.setCreateDate(questionDTO.getCreateDate());
        question.setStatus(questionDTO.isStatus());
        question.setDeletedAt(questionDTO.getDeletedAt());
        User user = new User();
        question.setUser(user.convertToEntity(questionDTO.getUserDTO()));

        return question;
    }

}
