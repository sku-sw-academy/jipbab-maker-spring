package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import sku.splim.jipbapmaker.domain.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderByModifyDateDesc();
    List<Question> findAllByUserIdAndDeletedAtIsNullOrderByCreateDateDesc(Long userId);
    List<Question> findAllByStatusFalseOrderByDeletedAt();

    @Transactional
    @Modifying
    @Query("DELETE FROM Question q WHERE q.id = :id")
    int deleteQuestionById(@Param("id") long id);
}
