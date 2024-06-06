package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Admin;
import sku.splim.jipbapmaker.domain.RefreshToken_admin;

import java.util.Optional;

public interface RefreshToken_AdminRepository extends JpaRepository<RefreshToken_admin, Long> {
    Optional<RefreshToken_admin> findByAdmin(Admin admin);
    Optional<RefreshToken_admin> findByRefreshToken(String refreshToken);
    void deleteByAdmin(Admin admin);
}
