package sku.splim.jipbapmaker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.Recipe;
import sku.splim.jipbapmaker.repository.RecipeRepository;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.findAllByOrderByModifyDateDesc();
    }

    public void saveRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public List<Recipe> getRecipesByUserId(Long id) {
        return recipeRepository.findAllByUserIdAndDeletedAtFalseOrderByModifyDateDesc(id);
    }

    public void updateStatus(Long id){
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if(recipe.isPresent()){
            Recipe updatedRecipe = recipe.get();
            updatedRecipe.setDeletedAt(true);
            recipeRepository.save(updatedRecipe);
        }
    }

    public Recipe findRecipeById(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        return optionalRecipe.orElse(null); // ID로 레시피 찾기
    }

    public List<Recipe> findAllByStatusOrderByModifyDateDesc() {
        return recipeRepository.findAllByStatusOrderByModifyDateDesc(true);
    }

}
