package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.User;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByEmailAndName(String email, String name);
    Optional<Admin> findById(long id);
}
