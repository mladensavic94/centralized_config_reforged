package clientSpring4;

import org.springframework.context.support.GenericXmlApplicationContext;

public class ClientStarterClass {

    public static void main(String[] args) {
        ConfigAppContextInitializer initializer = new ConfigAppContextInitializer();
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        initializer.initialize(context);
        context.load("start.xml");
        context.refresh();
        System.out.println(context.getEnvironment().getProperty("message"));
    }
}
