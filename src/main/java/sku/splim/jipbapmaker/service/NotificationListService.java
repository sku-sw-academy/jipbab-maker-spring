package sku.splim.jipbapmaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.domain.NotificationList;
import sku.splim.jipbapmaker.domain.User;
import sku.splim.jipbapmaker.repository.NotificationListRepository;

import java.util.List;

@Service
public class NotificationListService {
    @Autowired
    private NotificationListRepository notificationListRepository;

    public List<NotificationList> findAllByUserIdOrderByModifyDateDesc(long id) {
        return notificationListRepository.findAllByUserIdOrderByModifyDateDesc(id);
    }

    public void save(User user, String title, String body){
        NotificationList notificationList = new NotificationList();
        notificationList.setTitle(title);
        notificationList.setBody(body);
        notificationList.setUser(user);
        notificationListRepository.save(notificationList);
    }
}
