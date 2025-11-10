package com.slavikjunior.util;

import java.util.logging.*;

public class AppLogger {

    private static final Level DEFAULT_LEVEL = Level.INFO;

    static {
        LogManager.getLogManager().reset();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);

        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tT] [%2$s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        lr.getMillis(),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage());
            }
        });

        Logger rootLogger = Logger.getLogger("");
        rootLogger.addHandler(handler);
        rootLogger.setLevel(DEFAULT_LEVEL);
    }

    public static Logger get(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(DEFAULT_LEVEL);
        return logger;
    }
}