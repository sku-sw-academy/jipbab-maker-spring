package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.FAQ;
import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    List<FAQ> findAllByOrderByModifyDateDesc();
}
