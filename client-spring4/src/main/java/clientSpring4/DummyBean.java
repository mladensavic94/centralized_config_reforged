package clientSpring4;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class DummyBean {

    @Value("message")
    private String test;

    public DummyBean() {
        System.out.println("2"+this.test);
    }

    public DummyBean(String test) {
        this.test = test;
        System.out.println("1"+this.test);
        System.out.println(System.getProperty("message"));
    }


}
