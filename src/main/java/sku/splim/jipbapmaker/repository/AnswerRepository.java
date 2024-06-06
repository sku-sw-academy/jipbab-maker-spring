package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Answer;
import sku.splim.jipbapmaker.domain.Price;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
