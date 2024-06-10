package sku.splim.jipbapmaker.dto;

import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.domain.Recipe;

import java.sql.Timestamp;

@Getter
@Setter
public class RecipeDTO {

    private Long id;
    private UserDTO userDTO;
    private String title;
    private String content;
    private String comment;
    private String image;
    private boolean status;
    private boolean deletedAt;
    private Timestamp createDate;
    private Timestamp modifyDate;

    public RecipeDTO(){}

    public RecipeDTO(Long id, UserDTO userDTO, String title, String content, String comment, boolean status, String image, boolean deleted_at){
        this.id = id;
        this.userDTO = userDTO;
        this.title = title;
        this.content = content;
        this.comment = comment;
        this.status = status;
        this.image = image;
        this.deletedAt = deleted_at;
    }

    public RecipeDTO convertToDTO(Recipe recipe) {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setId(recipe.getId());
        recipeDTO.setTitle(recipe.getTitle());
        recipeDTO.setContent(recipe.getContent());
        recipeDTO.setModifyDate(recipe.getModifyDate());
        recipeDTO.setStatus(recipe.isStatus());
        recipeDTO.setComment(recipe.getComment());
        recipeDTO.setImage(recipe.getImage());
        recipeDTO.setDeletedAt(recipe.isDeletedAt());
        UserDTO userDTO1 = new UserDTO();
        recipeDTO.setUserDTO(userDTO1.convertToDTO(recipe.getUser()));
        return recipeDTO;
    }
}
