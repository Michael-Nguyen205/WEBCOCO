package spring.boot.webcococo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class WebcococoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebcococoApplication.class, args);
    }

}
