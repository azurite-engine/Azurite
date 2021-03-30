package util;

public class Logger {

    /**
     * Logs fatal errors in format: {@code [FATAL] [{Class calling the method}] {error}} <br>
     * Then, exits with exit value: {@code -1}
     *
     * @param error The fatal error message passed to log.
     */
    public static void logFatal(String error) {
        String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("\u001b[31m[FATAL] [" + clazz + "] " + error + "\u001b[0m");
        System.exit(-1);
    }

    /**
     * Logs asset errors in format: {@code [ASSET ERROR] [{Class calling the method}] {error}} <br>
     * Then, exits with exit value: {@code -1}
     *
     * @param error The asset error message passed to log.
     */
    public static void logAssetError(String error) {
        String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("\u001b[33m[ASSET ERROR] " + clazz + "] " + error + "\u001b[0m");
        System.exit(-1);
    }

    /**
     * Logs information in format: {@code [INFO] [{Class calling the method}] {info}}
     *
     * @param info The string passed to log.
     */
    public static void logInfo(String info) {
        String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("\u001b[32m[INFO] [" + clazz + "] " + info + "\u001b[0m");
    }

    public static void logSystemCompatibilityError() {
        System.out.println("System NOT COMPATIBLE, Might be driver or software issue.\n\n\tTry reinstalling program");
        System.exit(0);
    }

    public static void logEngineError() {
        System.out.println("We Encountered an Engine error it will be fixed soon. Thank you");
        System.exit(-1);
    }

    /**
     * Logs debug information in format: {@code [{Class calling the method}] {info}}
     *
     * @param log The string passed to log.
     */
    public static void debugLog(String log) {
        String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("\u001b[36m[DEBUG] [" + clazz + "] " + log + "\u001b[0m");
    }
}