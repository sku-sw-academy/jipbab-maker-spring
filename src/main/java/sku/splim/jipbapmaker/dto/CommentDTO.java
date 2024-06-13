package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Comment;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String content;
    private UserDTO userDTO;
    private RecipeDTO recipeDTO;
    private boolean updateFlag;
    private Timestamp modifyDate;

    public CommentDTO() {}

    public CommentDTO(Long id, String content, UserDTO userDTO, RecipeDTO recipeDTO, boolean updateFlag) {
        this.id = id;
        this.content = content;
        this.userDTO = userDTO;
        this.recipeDTO = recipeDTO;
        this.updateFlag = updateFlag;
    }

    public CommentDTO convertToDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setUpdateFlag(comment.isUpdatedFlag());
        commentDTO.setModifyDate(comment.getModifyDate());
        UserDTO userDTO = new UserDTO();
        commentDTO.setUserDTO(userDTO.convertToDTO(comment.getUser()));
        RecipeDTO recipeDTO = new RecipeDTO();
        commentDTO.setRecipeDTO(recipeDTO.convertToDTO(comment.getRecipe()));
        return commentDTO;
    }
}
