package sku.splim.jipbapmaker.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.dto.CategoryDTO;
import sku.splim.jipbapmaker.dto.ItemDTO;
import sku.splim.jipbapmaker.repository.ItemRepository;
import sku.splim.jipbapmaker.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/names")
    public List<String> getAllItemNames(){
        return itemService.getAllItemNames();
    }

    @GetMapping("/{code}")
    public List<String> getCategoryItem(@PathVariable("code") int code){
        return itemService.getCategoryItem(code);
    }

    @PostMapping("/increment/{itemName}")
    public ItemDTO incrementItemCount(@PathVariable("itemName") String itemName) {
        Item item = itemService.findByItemName(itemName);
        if (item == null) {
            throw new IllegalArgumentException("Invalid item name: " + itemName);
        }

        item.incrementCount();
        item = itemService.save(item);

        ItemDTO itemDto = new ItemDTO();
        CategoryDTO categoryDTO = new CategoryDTO();
        itemDto.setItem_code(item.getItemCode());
        itemDto.setItem_name(item.getItemName());
        itemDto.setCount(item.getCount());
        itemDto.setCategory(categoryDTO.convertToDTO(item.getCategoryCode()));

        return itemDto;
    }

    @GetMapping("/all")
    public List<ItemDTO> getAllItems(){
        List<ItemDTO> itemDTOS = new ArrayList<>();
        List<Item> items = itemService.getfindAll();

        for(Item item : items){
            ItemDTO itemDto = new ItemDTO();
            CategoryDTO categoryDTO = new CategoryDTO();

            itemDto.setItem_name(item.getItemName());
            itemDto.setItem_code(item.getItemCode());
            itemDto.setCount(item.getCount());
            itemDto.setCategory(categoryDTO.convertToDTO(item.getCategoryCode()));
            itemDTOS.add(itemDto);
        }

        return itemDTOS;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("item_code") int code, @RequestParam("image") MultipartFile imageFile) {
        // 유효성 검사: 이미지 파일이 제공되었는지 확인
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please provide an image file");
        }

        String uploadDir = "/home/centos/app/assets/ingredient/";

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

            // 사용자 프로필 업데이트
            itemService.uploadImage(code, fileName);
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
        try {
            Path filePath = Paths.get("/home/centos/app/assets/ingredient/").resolve(filename).normalize();
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

    @GetMapping("/list")
    public ResponseEntity<List<ItemDTO>> getList(){
        List<Item> items = itemService.findTopItemsByCountDesc();
        List<ItemDTO> itemDTOS = new ArrayList<>();

        for(Item item : items){
            ItemDTO itemDto = new ItemDTO();
            CategoryDTO categoryDTO = new CategoryDTO();
            itemDto.setItem_code(item.getItemCode());
            itemDto.setItem_name(item.getItemName());
            itemDto.setCount(item.getCount());
            itemDto.setCategory(categoryDTO.convertToDTO(item.getCategoryCode()));
            itemDTOS.add(itemDto);
        }

        return ResponseEntity.ok(itemDTOS);
    }

}
