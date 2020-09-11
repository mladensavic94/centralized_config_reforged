package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class MessageController {

    @Value("${message}")
    private String message;
    @Value("${randomNumber}")
    private Integer anInt;
    @Autowired
    TestRepo repo;

    @RequestMapping("/message")
    String getMessage() {
        return String.format(message, anInt);
    }

    @RequestMapping("/message2")
    String getMessage2() {
        return repo.findById(1).get().toString();
    }
}
