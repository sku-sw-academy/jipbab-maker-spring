package sku.splim.jipbapmaker.service;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;
import org.springframework.stereotype.Service;



@Service
public class FCMService {

    public static void sendFCMMessage(String token, String title, String body) {
        com.google.firebase.messaging.Notification.Builder notificationBuilder = com.google.firebase.messaging.Notification.builder()
                .setTitle(title)
                .setBody(body);

        Message message = Message.builder().setToken(token).setNotification(notificationBuilder.build()).build();

        try {
            FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send message.");
        }
    }

}
