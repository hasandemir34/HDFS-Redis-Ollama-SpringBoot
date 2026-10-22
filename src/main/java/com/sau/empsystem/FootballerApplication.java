package com.sau.empsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching //redis cahce sistemini tüm projede aktif
public class FootballerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballerApplication.class, args);
    }

}