package sku.splim.jipbapmaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sku.splim.jipbapmaker.domain.Addition;
import sku.splim.jipbapmaker.domain.Recipe;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.dto.RecipeDTO;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.service.AddService;
import sku.splim.jipbapmaker.service.RecipeService;
import sku.splim.jipbapmaker.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/recipe")
@RestController
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final AddService addService;

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

        List<Addition> add = addService.findAllByUserIdOrderByModifyDate(id);

        if(add != null) {
            for(Addition addItem : add) {
                Recipe recipe = addItem.getRecipe();
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

    @PutMapping("/deleteAt/{id}")
    public ResponseEntity<?> updateRecipeStatus(@PathVariable("id") Long id) {
        recipeService.updateStatus(id); // RecipeService의 updateStatus 메서드 호출
        return ResponseEntity.ok("Recipe status updated successfully.");
    }

    @PostMapping("/review")
    public ResponseEntity<String> review(@RequestParam("id") long id, @RequestParam("comment") String comment) {
        Recipe recipe = recipeService.findRecipeById(id);
        recipe.setComment(comment);
        recipe.setStatus(true);
        recipeService.saveRecipe(recipe);
        return ResponseEntity.ok("ok");
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("id") long id, @RequestParam("comment") String comment, @RequestParam("image") MultipartFile imageFile) {
        // 유효성 검사: 이미지 파일이 제공되었는지 확인
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please provide an image file");
        }
        // /home/centos/app/assets/recipe/
        String uploadDir = "src/main/resources/static/assets/images/";

        try {
            // 파일 저장
            String originalFileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);

            // 디렉토리가 존재하지 않으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(originalFileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 생성
            String fileName = filePath.getFileName().toString();
            Recipe recipe = recipeService.findRecipeById(id);
            recipe.setComment(comment);
            recipe.setImage(fileName);
            recipe.setStatus(true);
            recipeService.saveRecipe(recipe);
            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) {
        try { ///home/centos/app/assets/recipe/
            Path filePath = Paths.get("src/main/resources/static/assets/images/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/share")
    public ResponseEntity<List<RecipeDTO>> findShareRecipe() {
        List<Recipe> recipes = recipeService.findAllByStatusOrderByModifyDateDesc();
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

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateRecipeShareNot(@PathVariable("id") Long id) {
        recipeService.updateNotShare(id);// RecipeService의 updateStatus 메서드 호출
        return ResponseEntity.ok("Recipe status updated successfully.");
    }

}
