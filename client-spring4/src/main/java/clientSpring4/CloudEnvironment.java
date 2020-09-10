package clientSpring4;

import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class CloudEnvironment extends StandardEnvironment {


    @Override
    protected void customizePropertySources(MutablePropertySources propertySources) {
        super.customizePropertySources(propertySources);
        try {
            System.setProperty("spring.application.name", "gns2");
            PropertySource<?> source = initConfigServicePropertySourceLocator(this);
            propertySources.addFirst(source);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private PropertySource<?> initConfigServicePropertySourceLocator(Environment environment) {
        ConfigClientProperties configClientProperties = new ConfigClientProperties(environment);
        configClientProperties.setName("gns2");
        configClientProperties.setLabel("master");
        System.out.println("##################### will load the client configuration");
        System.out.println(configClientProperties);

        ConfigServicePropertySourceLocator configServicePropertySourceLocator =
                new ConfigServicePropertySourceLocator(configClientProperties);

        PropertySource<?> locate = configServicePropertySourceLocator.locate(environment);
        System.out.println("##################### " + locate.getProperty("message"));
        return locate;
    }

}
