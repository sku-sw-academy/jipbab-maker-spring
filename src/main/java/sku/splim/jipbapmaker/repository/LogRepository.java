package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
}
