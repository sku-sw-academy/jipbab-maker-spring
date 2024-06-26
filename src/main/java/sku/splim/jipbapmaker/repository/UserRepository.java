package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndNickname(String email, String nickname);
    Optional<User> findById(long id);

    @Query("SELECT u.email FROM User u")
    List<String> findAllEmails();

    @Query(value = "SELECT * FROM user WHERE push = true", nativeQuery = true)
    List<User> findAllByIsPush();
}
