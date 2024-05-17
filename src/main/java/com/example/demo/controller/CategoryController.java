package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Price;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    Category category;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> fetchDataFromURL() {
        List<Category> categories = new ArrayList<>();
        HashMap<Integer, String> map = new HashMap<>();
        map.put(100, "식량작물");
        map.put(200, "채소류");
        map.put(300, "특용작물");
        map.put(400, "과일류");
        map.put(500, "축산물");
        map.put(600, "수산물");

        for (int i = 100; i <= 600; i += 100) {
            Category category = new Category();
            if(categoryRepository.findByCategoryCode(i) == null){
                category.setCategoryCode(i);
                category.setCategoryName(map.get(i));
                categories.add(category);
            }
        }

        categoryRepository.saveAll(categories);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{CategoryCode}")
    public String findByCategoryName(@PathVariable("CategoryCode") int code){
        category = categoryService.findByCategoryCode(code);
        return category.getCategoryName();
    }
}
