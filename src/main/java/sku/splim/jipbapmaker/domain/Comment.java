package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.CommentDTO;

import java.sql.Timestamp;

@Table(name = "Comment")
@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "updated_flag", nullable = false)
    private boolean updatedFlag;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate;

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createDate = now;
        modifyDate = now;
    }

    public Comment() {}

    public Comment convertToEntity(CommentDTO commentDTO){
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setUpdatedFlag(commentDTO.isUpdateFlag());
        comment.setModifyDate(commentDTO.getModifyDate());
        User user = new User();
        comment.setUser(user);
        Recipe recipe = new Recipe();
        comment.setRecipe(recipe);
        return comment;
    }

}
