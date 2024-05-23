package sku.splim.jipbapmaker.repository;

import sku.splim.jipbapmaker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryCode(int code);
    Category findByCategoryName(String name);
}