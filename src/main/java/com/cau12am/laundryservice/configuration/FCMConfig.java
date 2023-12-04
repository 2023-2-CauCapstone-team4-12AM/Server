package com.cau12am.laundryservice.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FCMConfig {
    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("firebase/laundry-628fd-firebase-adminsdk-cusbz-8f0e84b206.json");
        if(FirebaseApp.getApps().isEmpty()){
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(classPathResource.getInputStream()))
                    .build();
            FirebaseApp.initializeApp(firebaseOptions);
        }
        return FirebaseMessaging.getInstance();
    }
}
