package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.FAQ;
import sku.splim.jipbapmaker.domain.Question;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
}
