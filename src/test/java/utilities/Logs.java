package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.nio.file.Path;

public class Logs {
    private static final Logger LOG = LogManager.getLogger("AUTOMATION");

    /**
     * Clave del ThreadContext que usa log4j2.xml para separar el log por escenario.
     */
    private static final String SCENARIO_KEY = "scenario";

    /**
     * A partir de aqui, los logs de este hilo van tambien al log del escenario indicado.
     */
    public static void bindScenario(String scenarioKey) {
        ThreadContext.put(SCENARIO_KEY, scenarioKey);
    }

    public static void unbindScenario() {
        ThreadContext.remove(SCENARIO_KEY);
    }

    /**
     * Ruta del log individual de un escenario (ver log4j2.xml).
     */
    public static Path scenarioLogFile(String scenarioKey) {
        return Path.of(Config.get("log.dir"), "scenarios", scenarioKey + ".log");
    }

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

    /**
     * Registra el error junto con el stack trace completo de la excepcion.
     */
    public static void error(String message, Throwable throwable) {
        LOG.error(message, throwable);
    }

    public static void warning(String message, Throwable throwable) {
        LOG.warn(message, throwable);
    }
}
