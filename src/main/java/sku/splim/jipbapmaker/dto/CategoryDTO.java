package sku.splim.jipbapmaker.dto;

import sku.splim.jipbapmaker.domain.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    String categoryName;
    int categoryCode;

    public CategoryDTO() {}

    public CategoryDTO(int categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    public CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryCode(category.getCategoryCode());
        categoryDTO.setCategoryName(category.getCategoryName());
        return categoryDTO;
    }
}
