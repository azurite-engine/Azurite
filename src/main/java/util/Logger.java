package util;

public class Logger {
    public static void logFATAL(String error) {
        System.out.println("[FATAL] " + error);
        System.exit(-1);
    }

    public static void logAssetError(String error) {
        System.out.println("[ASSET ERROR] " + error);
        System.exit(-1);
    }

    public static void logInfo(String info) {
        System.out.println("[INFO] " + info);
    }

    public static void logSystemCompatibilityError() {
        System.out.println("System NOT COMPATIBLE, Might be driver or software issue.\n\n\tTry reinstalling program");
        System.exit(0);
    }

    public static void logEngineError() {
        System.out.println("We Encountered an Engine error it will be fixed soon. Thank you");
        System.exit(-1);
    }

    public static void log(String log) {
        System.out.println(log);
    }
}