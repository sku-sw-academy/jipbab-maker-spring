package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Addition;

import java.util.List;

public interface AddRepository extends JpaRepository<Addition, Long> {
    List<Addition> findAllByUserIdOrderByModifyDate(Long userId);
    Addition findByUserIdAndRecipeId(Long userId, Long recipeId);
}
