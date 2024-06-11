package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.RecipeDTO;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "Recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "deleted_at", nullable = false)
    private boolean deletedAt;

    @Column(name = "image")
    private String image;

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

    @PreUpdate
    protected void onUpdate() {
        modifyDate = new Timestamp(System.currentTimeMillis());
    }

    public Recipe(){}


    public Recipe convertToDTO(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setId(recipeDTO.getId());
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setContent(recipeDTO.getContent());
        recipe.setModifyDate(recipeDTO.getModifyDate());
        recipe.setStatus(recipeDTO.isStatus());
        recipe.setImage(recipeDTO.getImage());
        recipe.setComment(recipeDTO.getComment());
        recipe.setDeletedAt(recipeDTO.isDeletedAt());
        User user = new User();
        recipe.setUser(user.convertToEntity(recipeDTO.getUserDTO()));
        return recipe;
    }

}
