package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logs {
    private static final Logger LOG = LogManager.getLogger("AUTOMATION");

    public static void trace(String message) {
        LOG.trace(message);
    }

    public static void debug(String message) {
        LOG.debug(message);
    }

    public static void info(String message) {
        LOG.info(message);
    }

    public static void error(String message) {
        LOG.error(message);
    }

    public static void warning(String message) {
        LOG.warn(message);
    }

    public static void fatal(String message) {
        LOG.fatal(message);
    }

    public static void trace(String format, Object... args) {
        LOG.trace(String.format(format, args));
    }

    public static void debug(String format, Object... args) {
        LOG.debug(String.format(format, args));
    }

    public static void info(String format, Object... args) {
        LOG.info(String.format(format, args));
    }

    public static void warning(String format, Object... args) {
        LOG.warn(String.format(format, args));
    }

    public static void error(String format, Object... args) {
        LOG.error(String.format(format, args));
    }

    public static void fatal(String format, Object... args) {
        LOG.fatal(String.format(format, args));
    }
}
