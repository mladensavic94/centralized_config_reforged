package server;

import java.io.File;

public class FileResolver {

    private static final String COMMA_DELIMITER = ",";
    private static final String DASH_DELIMITER = "-";

    public static File findOneByFilter(File root, String[] searchPaths, String application, String profiles, boolean useSearchPath) {
        for (String searchPath : searchPaths) {
            String rootPath = resolveRootPath(root, useSearchPath, searchPath);
            File rootFolder = new File(rootPath);
            String[] profilesArray = profiles.split(COMMA_DELIMITER);
            for (String profile : profilesArray) {
                String[] prodCompSplit = profile.split(DASH_DELIMITER);
                File[] productDir = rootFolder.listFiles((dir, name) -> name.contains(prodCompSplit[0]));
                if (nullAndSizeCheck(productDir)) {
                    File[] componentDir = productDir[0].listFiles((dir, name) -> name.contains(prodCompSplit[1]));
                    if (nullAndSizeCheck(componentDir)) {
                        File[] file = componentDir[0].listFiles((dir, name) -> name.contains(application));
                        if (nullAndSizeCheck(file))
                            return file[0];
                    }
                }
            }
        }
        throw new RuntimeException("Properties not found for " + application);
    }

    private static boolean nullAndSizeCheck(File[] array) {
        return array != null && array.length == 1;
    }

    private static String resolveRootPath(File root, boolean useSearchPath, String searchPath) {
        String searchFolderPath;
        if (useSearchPath)
            searchFolderPath = root.getAbsolutePath() + "\\" + searchPath;
        else
            searchFolderPath = searchPath;
        return cleanPath(searchFolderPath);
    }

    private static String cleanPath(String searchPath) {
        return searchPath.replace("file:/", "");
    }
}
