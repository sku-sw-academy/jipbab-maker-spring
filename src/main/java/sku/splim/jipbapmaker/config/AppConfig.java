package sku.splim.jipbapmaker.config;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import sku.splim.jipbapmaker.dto.CategoryDTO;
import sku.splim.jipbapmaker.dto.ItemDTO;
import sku.splim.jipbapmaker.dto.PriceDTO;
import sku.splim.jipbapmaker.domain.Category;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Price;
import sku.splim.jipbapmaker.service.CategoryService;
import sku.splim.jipbapmaker.service.ItemService;
import sku.splim.jipbapmaker.service.PriceService;
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
