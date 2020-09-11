package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.server.environment.EnvironmentCleaner;
import org.springframework.cloud.config.server.environment.JGitEnvironmentProperties;
import org.springframework.cloud.config.server.environment.JGitEnvironmentRepository;
import org.springframework.cloud.config.server.environment.NativeEnvironmentProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;

@Configuration
@Profile("git")
public class CustomJGitEnvironmentRepository extends JGitEnvironmentRepository {

    public static final Logger log = LoggerFactory.getLogger(CustomJGitEnvironmentRepository.class);

    private final EnvironmentCleaner cleaner = new EnvironmentCleaner();

    public CustomJGitEnvironmentRepository(ConfigurableEnvironment environment, JGitEnvironmentProperties properties) {
        super(environment, properties);
    }

    @Override
    public synchronized Environment findOne(String application, String profile, String label, boolean includeOrigin) {
        CustomNativeEnvironmentRepository delegate = new CustomNativeEnvironmentRepository(
                getEnvironment(), new NativeEnvironmentProperties());
        Locations locations = getLocations(application, profile, label);
        delegate.setSearchLocations(locations.getLocations());
        Environment result = delegate.findOne(application, profile, "", includeOrigin);
        result.setVersion(locations.getVersion());
        result.setLabel(label);
        return this.cleaner.clean(result, getWorkingDirectory().toURI().toString(),
                getUri());
    }
}
