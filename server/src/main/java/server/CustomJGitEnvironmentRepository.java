package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.config.server.environment.JGitEnvironmentProperties;
import org.springframework.cloud.config.server.environment.JGitEnvironmentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.util.Arrays;

@Configuration
public class CustomJGitEnvironmentRepository extends JGitEnvironmentRepository {

    public static final Logger log = LoggerFactory.getLogger(CustomJGitEnvironmentRepository.class);

    public CustomJGitEnvironmentRepository(ConfigurableEnvironment environment, JGitEnvironmentProperties properties) {
        super(environment, properties);
    }

    @Override
    protected String[] getSearchLocations(File dir, String application, String profile, String label) {
        File file = findOneByFilter(dir, application, profile, label);
        log.info(file.getAbsolutePath());
        return super.getSearchLocations(file, application, profile, label);
    }

    private File findOneByFilter(File root, String application, String profile, String label) {
        log.info("Root {} File name {} Product and component {} Env {}", root.getPath(), application, profile, label);
        String[] searchPaths = getSearchPaths();
        String[] split = profile.split("-");
        for (String searchPath : searchPaths) {
            File[] searchPathsFiles = root.listFiles((dir, name) -> name.equals(searchPath));
            if (searchPathsFiles != null && searchPathsFiles.length == 1) {
                File[] productDir = searchPathsFiles[0].listFiles((dir, name) -> name.equals(split[0]));
                if (productDir != null && productDir.length == 1) {
                    File[] componentDir = productDir[0].listFiles((dir, name) -> name.equals(split[1]));
                    if (componentDir != null && componentDir.length == 1) {
                        return componentDir[0];
                    }
                }
            }
        }

        return null;
    }
}
