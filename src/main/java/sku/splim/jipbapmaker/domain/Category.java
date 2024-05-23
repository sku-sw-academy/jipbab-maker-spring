package sku.splim.jipbapmaker.domain;

import sku.splim.jipbapmaker.dto.CategoryDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "Category")
public class Category {
    @Id
    @Column(name = "category_code")
    int categoryCode;

    @Column(name = "category_name")
    String categoryName;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    public Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryCode(categoryDTO.getCategoryCode());
        category.setCategoryName(categoryDTO.getCategoryName());
        return category;
    }
}
