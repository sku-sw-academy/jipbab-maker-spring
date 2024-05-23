package sku.splim.jipbapmaker.service;

import sku.splim.jipbapmaker.domain.Category;
import sku.splim.jipbapmaker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public void saveCategories(List<Category> categories) {
        categoryRepository.saveAll(categories);
    }

    public Category findByCategoryCode(int categoryCode) {
        return categoryRepository.findByCategoryCode(categoryCode);
    }
}
