package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Answer;
import sku.splim.jipbapmaker.domain.Log;
import sku.splim.jipbapmaker.domain.Price;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByOrderByModifyDateDesc();
    List<Answer> findAllByAdminIdOrderByModifyDateDesc(long adminId);
}
