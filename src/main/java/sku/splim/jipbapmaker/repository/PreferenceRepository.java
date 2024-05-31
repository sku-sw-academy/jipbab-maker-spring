package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

}
