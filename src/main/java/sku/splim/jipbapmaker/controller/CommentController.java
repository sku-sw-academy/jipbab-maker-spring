package sku.splim.jipbapmaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Comment;
import sku.splim.jipbapmaker.domain.Recipe;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.CommentDTO;
import sku.splim.jipbapmaker.dto.RecipeDTO;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.service.CommentService;
import sku.splim.jipbapmaker.service.RecipeService;
import sku.splim.jipbapmaker.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final RecipeService recipeService;

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestParam("userId") Long userId, @RequestParam("recipeId") Long recipeId, @RequestParam("content") String content) {
        try {
            User user = userService.findById(userId);
            Recipe recipe = recipeService.findRecipeById(recipeId);
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUser(user);
            comment.setRecipe(recipe);
            comment.setUpdatedFlag(false);
            commentService.save(comment);
            return ResponseEntity.ok("Successfully sent comment");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending comment: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>> getRecipeComment(@PathVariable("id") Long id) {
        try {
            List<Comment> comments = commentService.recipeComment(id);
            List<CommentDTO> commentDTOS = new ArrayList<>();

            for (Comment comment : comments) {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setId(comment.getId());
                commentDTO.setContent(comment.getContent());
                commentDTO.setUpdateFlag(comment.isUpdatedFlag());
                commentDTO.setUserDTO(UserDTO.convertToDTO(comment.getUser()));
                commentDTO.setRecipeDTO(RecipeDTO.convertToDTO(comment.getRecipe()));
                commentDTO.setModifyDate(comment.getModifyDate());
                commentDTOS.add(commentDTO);
            }

            return ResponseEntity.ok(commentDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestParam("id") Long id, @RequestParam("content") String content) {
        try {
            commentService.update(id, content);
            return ResponseEntity.ok("Successfully updated comment");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating comment: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Long id) {
        try {
            commentService.delete(id);
            return ResponseEntity.ok("Successfully deleted comment");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting comment: " + e.getMessage());
        }
    }
}
