package client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CentralizedConfigClient {

    public static void main(String[] args) {
        SpringApplication.run(CentralizedConfigClient.class, args);
    }
}
