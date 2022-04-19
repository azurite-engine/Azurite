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

    /**
     * Logs System Compatibility error in format: {@code [SYSTEM NOT COMPATIBLE] {error} <br> }
     * Then exits with value 1 IF specified to exit
     *
     * @param error The System Compatibility Error.
     * @param exit  IF true then exit else don't.
     */
    public static void logSystemCompatibilityError(String error, boolean exit) {
        System.out.println("\u001b[31m[SYSTEM NOT COMPATIBLE]" + error + "\u001b[0m");
        if(exit == true) System.exit(1);
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