package sku.splim.jipbapmaker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sku.splim.jipbapmaker.dto.NotificationListDTO;
import sku.splim.jipbapmaker.dto.UserDTO;

import java.sql.Timestamp;

@Table(name = "NotificationList")
@Getter
@Setter
@Entity
public class NotificationList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "created_at", updatable = false)
    private Timestamp createDate = new Timestamp(System.currentTimeMillis());

    @Column(name = "updated_at", updatable = true)
    private Timestamp modifyDate = new Timestamp(System.currentTimeMillis());

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createDate = now;
        modifyDate = now;
    }

    @PreUpdate
    protected void onUpdate() {
        modifyDate = new Timestamp(System.currentTimeMillis());
    }

    public NotificationList(){

    }

    public NotificationList convertToEntity(NotificationListDTO notificationListDTO) {
        NotificationList notificationList = new NotificationList();
        notificationList.setId(notificationListDTO.getId());
        notificationList.setTitle(notificationListDTO.getTitle());
        notificationList.setBody(notificationListDTO.getBody());
        notificationList.setModifyDate(notificationListDTO.getModifyDate());
        User user = new User();
        notificationList.setUser(user.convertToEntity(notificationListDTO.getUserDTO()));
        return notificationList;
    }
}
