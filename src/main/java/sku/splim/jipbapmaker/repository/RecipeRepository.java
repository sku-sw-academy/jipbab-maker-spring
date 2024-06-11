package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Question;
import sku.splim.jipbapmaker.domain.Recipe;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByOrderByModifyDateDesc();
    List<Recipe> findAllByUserIdAndDeletedAtFalseOrderByModifyDateDesc(Long userId);
    List<Recipe> findAllByStatusOrderByModifyDateDesc(boolean status);
}
