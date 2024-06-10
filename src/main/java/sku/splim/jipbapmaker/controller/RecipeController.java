package sku.splim.jipbapmaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Recipe;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.RecipeDTO;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.service.RecipeService;
import sku.splim.jipbapmaker.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/recipe")
@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam("userId") long id, @RequestParam("title") String title, @RequestParam("content") String content) {
        User user = userService.findById(id);
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setContent(content);
        recipe.setUser(user);
        recipe.setComment("");
        recipe.setStatus(false);
        recipe.setDeletedAt(false);
        recipeService.saveRecipe(recipe);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<RecipeDTO>> getUserRecipe(@PathVariable("userId") Long id) {
        List<Recipe> recipes = recipeService.getRecipesByUserId(id);
        List<RecipeDTO> recipeDTOS = new ArrayList<>();

        for(Recipe recipe : recipes) {
            RecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setId(recipe.getId());
            recipeDTO.setTitle(recipe.getTitle());
            recipeDTO.setContent(recipe.getContent());
            recipeDTO.setComment(recipe.getComment());
            UserDTO userDTO = new UserDTO();
            recipeDTO.setUserDTO(userDTO.convertToDTO(recipe.getUser()));
            recipeDTO.setImage(recipe.getImage());
            recipeDTO.setStatus(recipe.isStatus());
            recipeDTO.setDeletedAt(recipe.isDeletedAt());
            recipeDTO.setModifyDate(recipe.getModifyDate());
            recipeDTOS.add(recipeDTO);
        }

        return ResponseEntity.ok(recipeDTOS);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecipeDTO>> findAll() {
        List<Recipe> recipes = recipeService.getRecipes();
        List<RecipeDTO> recipeDTOS = new ArrayList<>();

        for(Recipe recipe : recipes) {
            RecipeDTO recipeDTO = new RecipeDTO();
            recipeDTO.setId(recipe.getId());
            recipeDTO.setTitle(recipe.getTitle());
            recipeDTO.setContent(recipe.getContent());
            recipeDTO.setComment(recipe.getComment());
            UserDTO userDTO = new UserDTO();
            recipeDTO.setUserDTO(userDTO.convertToDTO(recipe.getUser()));
            recipeDTO.setImage(recipe.getImage());
            recipeDTO.setStatus(recipe.isStatus());
            recipeDTO.setDeletedAt(recipe.isDeletedAt());
            recipeDTO.setModifyDate(recipe.getModifyDate());
            recipeDTOS.add(recipeDTO);
        }

        return ResponseEntity.ok(recipeDTOS);
    }

}
