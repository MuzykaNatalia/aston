package ru.aston;

import java.io.File;
import org.apache.catalina.startup.Tomcat;
import ru.aston.config.ConfigLoader;

public class AstonApp {
    public static void main(String[] args) {
        try {
            ConfigLoader.loadProperties();
            ConfigLoader.createDatabaseSchema();
            startTomcat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startTomcat() throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        String webappDirLocation = "src/main/webapp/";
        tomcat.addWebapp("/users", new File(webappDirLocation).getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }
}