package sku.splim.jipbapmaker.service;

import java.util.List;
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
}
