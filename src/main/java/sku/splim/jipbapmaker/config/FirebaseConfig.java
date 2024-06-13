package sku.splim.jipbapmaker.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {     ///home/centos  src/main/resources src/main/resources/
        FileInputStream serviceAccount = new FileInputStream("/home/centos/fcmtest-78202-firebase-adminsdk-xryu4-8e93d79c3a.json");

        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

        return FirebaseApp.initializeApp(options);
    }
}
