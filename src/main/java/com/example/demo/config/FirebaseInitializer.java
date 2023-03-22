// https://www.couchcoding.kr/blogs/couchcoding/Firebase로 Google 로그인 구현하기 2 (Spring 파트)
// firebase.json 이란 이름으로 설정파일 프로젝트 최상단에 추가
package com.example.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseInitializer {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        log.info("Initializing Firebase. ");
        FileInputStream serviceAccount =
                new FileInputStream("secureFile.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("th-d-shops-be.appspot.com") // storage 주소 입력
                .build();

        FirebaseApp app = FirebaseApp.initializeApp(options);
        log.info("FirebaseApp initialized " + app.getName());
        return app;
    }

    @Bean
    public FirebaseAuth getFirebaseAuth() throws IOException {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp());
        return firebaseAuth;
    }

    @Bean
    public Bucket bucket() throws IOException {
        // Storage Bucket을 Bean으로 등록
        return StorageClient.getInstance(firebaseApp()).bucket();
    }
}
