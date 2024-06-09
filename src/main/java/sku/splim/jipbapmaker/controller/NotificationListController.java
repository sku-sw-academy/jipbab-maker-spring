package sku.splim.jipbapmaker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sku.splim.jipbapmaker.domain.NotificationList;
import sku.splim.jipbapmaker.dto.NotificationListDTO;
import sku.splim.jipbapmaker.dto.UserDTO;
import sku.splim.jipbapmaker.service.NotificationListService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationListController {
    private final NotificationListService notificationListService;

    public NotificationListController(NotificationListService notificationListService) {
        this.notificationListService = notificationListService;
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<NotificationListDTO>> getCategoryItem(@PathVariable("id") long id){
        List<NotificationList> notificationList = notificationListService.findAllByUserIdOrderByModifyDateDesc(id);
        List<NotificationListDTO> notificationListDTOS = new ArrayList<>();

        for (NotificationList notificationListItem : notificationList) {
            NotificationListDTO notificationListDTO = new NotificationListDTO();
            notificationListDTO.setId(notificationListItem.getId());
            notificationListDTO.setTitle(notificationListItem.getTitle());
            notificationListDTO.setBody(notificationListItem.getBody());
            notificationListDTO.setModifyDate(notificationListItem.getModifyDate());
            UserDTO userDTO = new UserDTO();
            notificationListDTO.setUserDTO(userDTO.convertToDTO(notificationListItem.getUser()));
            notificationListDTOS.add(notificationListDTO);
        }

        return ResponseEntity.ok(notificationListDTOS);
    }
}
