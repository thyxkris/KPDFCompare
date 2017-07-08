import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by makri on 16/05/2017.
 */


public class ConfigHelper {

    private static ConfigHelper instance;
    private Config conf;
    private static ClassLoader classLoader = ConfigHelper.class.getClassLoader();

    public ConfigHelper() {

        conf = ConfigFactory.load(classLoader, System
                .getProperty("project.properties", "project.properties"));
    }

    public static synchronized ConfigHelper getInstance() {
        if (instance == null) {
            instance = new ConfigHelper();
        }
        return instance;
    }

    public static String getString(String key) {
        return ConfigHelper.getInstance().conf.getString(key);
    }

    public static int getInt(String key) {
        return ConfigHelper.getInstance().conf.getInt(key);
    }

    public static boolean getBoolean(String key) {
        return ConfigHelper.getInstance().conf.getBoolean(key);
    }

    public static URL getUrl(String key) throws MalformedURLException {
        return new URL(ConfigHelper.getInstance().conf.getString(key));
    }

    public static String getTestResourcesFolderPath()
    {

        File file = new File(classLoader.getResource("").getFile());
        return file.getAbsolutePath();
    }
    public static String getCurrentWorkingDir()    {

        Path currentRelativePath = Paths.get("");
        return currentRelativePath.toAbsolutePath().toString();

    }
    public static String getUserDir(){
        return System.getProperty("user.dir");
    }

    public static String getTemplateDir(){
        return ConfigHelper.getTestResourcesFolderPath() + File.separator + "templates" + File.separator;

    }

}