package com.slavikjunior.config;

import com.slavikjunior.deorm.db_manager.DbConnectionManager;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.slavikjunior.util.AppLogger;

@WebListener
public class DatabaseInitializer implements ServletContextListener {
    private static final Logger log = AppLogger.get(DatabaseInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            log.severe("Failed to load application.properties: " + e.getMessage());
            throw new RuntimeException("Failed to load application.properties from main project", e);
        }

        String host = properties.getProperty("database.host", "localhost");
        String port = properties.getProperty("database.port", "5432");
        String dbName = properties.getProperty("database.name", "mydb");
        String user = properties.getProperty("database.user", "postgres");
        String password = properties.getProperty("database.password", "password");

        DbConnectionManager.INSTANCE.configure(host, port, dbName, user, password);

        log.info("Database configured: " + dbName + " on " + host + ":" + port);
    }
}