package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Notice;
import sku.splim.jipbapmaker.domain.Question;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
