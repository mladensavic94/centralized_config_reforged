package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.cloud.config.server.environment.NativeEnvironmentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//@Configuration
//@Profile("native")
public class CustomNativeEnvironmentRepository extends NativeEnvironmentRepository {

    public static final Logger log = LoggerFactory.getLogger(CustomNativeEnvironmentRepository.class);

    public static final String KEY_VALUE_DELIMITER = "=";

    public CustomNativeEnvironmentRepository(ConfigurableEnvironment environment, NativeEnvironmentProperties properties) {
        super(environment, properties);
    }

    @Override
    public Environment findOne(String config, String profile, String label, boolean includeOrigin) {
        log.info("spring.application.name {} spring.profiles.active {} spring.cloud.config.label {}", config, profile, label);
        return findOneByFilter(config, profile);
    }

    private Environment findOneByFilter(String application, String profile) {
        Environment environment = new Environment(application);
        File file = FileResolver.findOneByFilter(new File(getSearchLocations()[0]), getSearchLocations(), application, profile, false);
        Map<String, Object> map = convertFileToMap(file);
        environment.getPropertySources().add(new PropertySource(application, map));
        return environment;
    }

    private Map<String, Object> convertFileToMap(File file) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            return in.lines().collect(Collectors.toMap(s -> s.split(KEY_VALUE_DELIMITER)[0], s1 -> tryParse(s1.split(KEY_VALUE_DELIMITER)[1])));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new HashMap<>();
    }

    private Object tryParse(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            log.trace("'{}' not an int", input);
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            log.trace("'{}' not an double", input);
        }
        return input;
    }


}
