package sku.splim.jipbapmaker.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.domain.NotificationList;
import sku.splim.jipbapmaker.domain.User;

import java.sql.Timestamp;

@Getter
@Setter
public class NotificationListDTO {
    private Long id;
    private UserDTO userDTO;
    private String title;
    private String body;
    private Timestamp modifyDate;

    public NotificationListDTO() {

    }

    public NotificationListDTO(Long id, UserDTO userDTO, String title, String body) {
        this.id = id;
        this.userDTO = userDTO;
        this.title = title;
        this.body = body;
    }

    public NotificationListDTO convertToDTO(NotificationList notificationList) {
        NotificationListDTO notificationListDTO = new NotificationListDTO();
        notificationListDTO.setId(notificationList.getId());
        notificationListDTO.setTitle(notificationList.getTitle());
        notificationListDTO.setBody(notificationList.getBody());
        notificationListDTO.setModifyDate(notificationList.getModifyDate());
        UserDTO userDTO = new UserDTO();
        notificationListDTO.setUserDTO(userDTO.convertToDTO(notificationList.getUser()));
        return notificationListDTO;
    }
}
