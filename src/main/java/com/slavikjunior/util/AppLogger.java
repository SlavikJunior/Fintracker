package com.slavikjunior.util;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {

    private static final Level DEFAULT_LEVEL = Level.INFO;

    static {
        LogManager.getLogManager().reset();
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        consoleHandler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tT] [%2$s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        lr.getMillis(),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage());
            }
        });

        try {
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file logger", e);
        }

        Logger rootLogger = Logger.getLogger("");
        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(DEFAULT_LEVEL);
    }

    public static Logger get(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(DEFAULT_LEVEL);
        return logger;
    }
}