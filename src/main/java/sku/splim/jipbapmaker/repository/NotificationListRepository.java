package sku.splim.jipbapmaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sku.splim.jipbapmaker.domain.Answer;
import sku.splim.jipbapmaker.domain.NotificationList;

import java.util.List;

public interface NotificationListRepository extends JpaRepository<NotificationList, Long> {
    List<NotificationList> findAllByUserIdOrderByModifyDateDesc(long id);
}
