package com.intruder.gdalexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GdalexampleApplication {

    public static void main(String[] args) {
        GdalSetting.setPath();
        SpringApplication.run(GdalexampleApplication.class, args);
    }

}
