package sku.splim.jipbapmaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Addition;
import sku.splim.jipbapmaker.domain.Recipe;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.service.AddService;
import sku.splim.jipbapmaker.service.RecipeService;
import sku.splim.jipbapmaker.service.UserService;

@RequiredArgsConstructor
@RequestMapping("/add")
@RestController
public class AddController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final AddService addService;

    @PostMapping("/save")
    public ResponseEntity<String> addSave(@RequestParam("userId") long id, @RequestParam("recipeId") long recipeId){
        Recipe recipe = recipeService.findRecipeById(recipeId);
        User user = userService.findById(id);
        Addition add = new Addition();
        add.setUser(user);
        add.setRecipe(recipe);
        addService.save(add);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/exist")
    public ResponseEntity<String> exist(@RequestParam("userId") long id, @RequestParam("recipeId") long recipeId){
        Addition add = addService.findByUserIdAndRecipeId(id, recipeId);

        if(add == null){
            return ResponseEntity.ok("No");
        }else{
            return ResponseEntity.ok("Yes");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAdd(@PathVariable("id") long id) {
        try {
            addService.deleteAdd(id);
            return ResponseEntity.ok("Selected additional recipe deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete selected additional recipe: " + e.getMessage());
        }
    }


}
