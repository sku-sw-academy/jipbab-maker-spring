package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Comment;
import java.util.*;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRecipeIdOrderByModifyDate(Long id);
}
