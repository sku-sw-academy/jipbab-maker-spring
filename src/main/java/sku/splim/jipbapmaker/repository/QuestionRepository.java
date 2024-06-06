package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Price;
import sku.splim.jipbapmaker.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
