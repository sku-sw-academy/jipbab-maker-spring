package com.example.demo.config;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.dto.ItemDTO;
import com.example.demo.dto.PriceDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import com.example.demo.entity.Price;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ItemService;
import com.example.demo.service.PriceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CategoryDTO categoryDTO() {
        return new CategoryDTO();
    }

    @Bean
    public ItemDTO itemDTO() {
        return new ItemDTO();
    }

    @Bean
    public PriceDTO priceDTO() {
        return new PriceDTO();
    }

    @Bean
    public Category category() {
        return new Category();
    }

    @Bean
    public Item item() {
        return new Item();
    }

    @Bean
    public Price price(){
        return new Price();
    }

    @Bean
    public PriceService priceService() {
        return new PriceService();
    }

    @Bean
    public ItemService itemService() {
        return new ItemService();
    }

    @Bean
    public CategoryService categoryService() {
        return new CategoryService();
    }
}
