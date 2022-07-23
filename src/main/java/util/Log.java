package util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Logging utility to print colored and labeled errors with class references.
 * Always call the methods of this class directly and do not nest it inside other functions,
 * because it will mess up the stacktrace and would therefore display a wrong source.<br>
 * <u>Logging modes:</u> <br>
 * - FATAL -> only fatal errors and major problems are reported; this is the minimal setting <br>
 * - WARNINGS -> includes fatal errors and minor warnings; useful for detecting common issues <br>
 * - NO_DEBUG -> displays all information except for hard debug logs; for integrity checks <br>
 * - ALL -> shows all logs including hard debug for a detailed overview over vital systems <br>
 */
public class Log {

    public static final int FATAL_ONLY = 0; //fatal
    public static final int WARNINGS = 1; //fatal, warn
    public static final int NO_DEBUG = 2; //fatal, warn, info
    public static final int ALL = 3; //fatal, warn, info, debug

    public static final String CL_RED = "\u001b[31m";
    public static final String CL_GREEN = "\u001b[32m";
    public static final String CL_ORANGE = "\u001b[33m";
    public static final String CL_BLUE = "\u001b[34m";
    public static final String CL_PURPLE = "\u001b[35m";
    public static final String CL_CYAN = "\u001b[36m";
    public static final String CL_GRAY = "\u001b[37m";

    private static int logLevel = FATAL_ONLY;

    private static LoggingThread loggingThread;

    private static StackTraceElement source() {
        return Thread.currentThread().getStackTrace()[3];
    }

    private static StackTraceElement source(int stepUp) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= stepUp + 3) return stackTrace[stackTrace.length - 1];
        return stackTrace[3 + stepUp];
    }

    /**
     * Starts a thread for saving the log into a file. the saved log may have a different logging level than the printed one.
     *
     * @param path     the folder path for the log file to be saved to
     * @param logLevel the logging level for the saved file
     * @throws IOException if any issue happens during the creation and saving of the log file
     * @see Engine#enableLogFiles(File, int)
     */
    public synchronized static void startLogging(File path, int logLevel) throws IOException {
        if (loggingThread != null) return;
        loggingThread = new LoggingThread(path, logLevel);
        if (!loggingThread.init()) {
            Log.fatal("There is some fatal issue with the path \"" + path.toString() + "\"", 2);
            return;
        }
        loggingThread.start();
    }

    /**
     * Set the log level to filter the amount of logs printed to the terminal
     *
     * @param logLevel {@link Log#FATAL_ONLY}, {@link Log#WARNINGS}, {@link Log#NO_DEBUG}, {@link Log#ALL}
     */
    public static void setLogLevel(int logLevel) {
        Log.logLevel = Math.abs(logLevel) % 4;
    }

    /**
     * Printed on level {@link Log#ALL} only. Use this method to print a detailed debug message about vital information in the system.
     * Includes the source class of the method call in the log.
     *
     * @param log the information to be logged
     */
    public static void debug(String log) {
        StackTraceElement source = source();
        String line = "[DEBUG] {" + source.getClassName() + "} " + log;
        println(ALL, CL_CYAN + line);
    }

    /**
     * Printed on level {@link Log#ALL} only. Use this method to print a detailed debug message about vital information in the system.
     * Includes the source class of the method call in the log as well as a second source class further down the stacktrace.
     *
     * @param log    the information to be logged
     * @param stepUp the amount of steps to take starting at the default calling class to reach the second class to log;
     *               e.g. if the stacktrace is <code>A -> B -> C -> D -> debug</code>
     *               then D would be the default calling class and a stepUp of 2 would specify B as the second one to log.
     */
    public static void debug(String log, int stepUp) {
        StackTraceElement source = source(stepUp);
        StackTraceElement source2 = source();
        String line = "[DEBUG] {" + source.getClassName() + " -> " + source2.getClassName() + "} " + log;
        println(ALL, CL_CYAN + line);
    }

    /**
     * Printed on level {@link Log#ALL} only. Use this method to print a detailed debug message about vital information in the system.
     * May be used to exclude the source class of the method call from the log.
     *
     * @param log         the information to be logged
     * @param printSource if set to false, the source class is excluded; if set to true, the call is identical to {@link Log#debug(String)}
     */
    public static void debug(String log, boolean printSource) {
        StackTraceElement source = source();
        String line = "[DEBUG]" + (printSource ? " {" + source.getClassName() + "} " : " ") + log;
        println(ALL, CL_CYAN + line);
    }

    /**
     * Printed on levels {@link Log#ALL} and {@link Log#NO_DEBUG} only. Use this method to notify of regular system events like successful loading operations for example.
     * Includes the source class of the method call in the log.
     *
     * @param info the information to be logged
     */
    public static void info(String info) {
        StackTraceElement source = source();
        String line = "[INFO] {" + source.getClassName() + "} " + info;
        println(NO_DEBUG, CL_GREEN + line);
    }

    /**
     * Printed on levels {@link Log#ALL} and {@link Log#NO_DEBUG} only. Use this method to notify of regular system events like successful loading operations for example.
     * Includes the source class of the method call in the log as well as a second source class further down the stacktrace.
     *
     * @param info   the information to be logged
     * @param stepUp the amount of steps to take starting at the default calling class to reach the second class to log;
     *               e.g. if the stacktrace is <code>A -> B -> C -> D -> debug</code>
     *               then D would be the default calling class and a stepUp of 2 would specify B as the second one to log.
     */
    public static void info(String info, int stepUp) {
        StackTraceElement source = source(stepUp);
        StackTraceElement source2 = source();
        String line = "[INFO] {" + source.getClassName() + " -> " + source2.getClassName() + "} " + info;
        println(NO_DEBUG, CL_GREEN + line);
    }

    /**
     * Printed on levels {@link Log#ALL} and {@link Log#NO_DEBUG} only. Use this method to notify of regular system events like successful loading operations for example.
     * May be used to exclude the source class of the method call from the log.
     *
     * @param info        the information to be logged
     * @param printSource if set to false, the source class is excluded; if set to true, the call is identical to {@link Log#info(String)}
     */
    public static void info(String info, boolean printSource) {
        StackTraceElement source = source();
        String line = "[INFO]" + (printSource ? " {" + source.getClassName() + "} " : " ") + info;
        println(NO_DEBUG, CL_GREEN + line);
    }

    /**
     * Printed on levels {@link Log#ALL}, {@link Log#NO_DEBUG} and {@link Log#WARNINGS}. Use this method to warn about unintended behaviour that might lead to bugs that aren't spotted yet.
     * Includes the source class of the method call in the log.
     *
     * @param warn the warning to be logged
     */
    public static void warn(String warn) {
        StackTraceElement source = source();
        String line = "[WARN] {" + source + "} " + warn;
        println(WARNINGS, CL_ORANGE + line);
    }

    /**
     * Printed on levels {@link Log#ALL}, {@link Log#NO_DEBUG} and {@link Log#WARNINGS}. Use this method to warn about unintended behaviour that might lead to bugs that aren't spotted yet.
     * Includes the source class of the method call in the log as well as a second source class further down the stacktrace.
     *
     * @param warn   the warning to be logged
     * @param stepUp the amount of steps to take starting at the default calling class to reach the second class to log;
     *               e.g. if the stacktrace is <code>A -> B -> C -> D -> debug</code>
     *               then D would be the default calling class and a stepUp of 2 would specify B as the second one to log.
     */
    public static void warn(String warn, int stepUp) {
        StackTraceElement source = source(stepUp);
        StackTraceElement source2 = source();
        String line = "[WARN] {" + source + " -> " + source2 + "} " + warn;
        println(WARNINGS, CL_ORANGE + line);
    }

    /**
     * Printed on levels {@link Log#ALL}, {@link Log#NO_DEBUG} and {@link Log#WARNINGS}. Use this method to warn about unintended behaviour that might lead to bugs that aren't spotted yet.
     * May be used to exclude the source class of the method call from the log.
     *
     * @param warn        the warning to be logged
     * @param printSource if set to false, the source class is excluded; if set to true, the call is identical to {@link Log#warn(String)}
     */
    public static void warn(String warn, boolean printSource) {
        StackTraceElement source = source();
        String line = "[WARN]" + (printSource ? " {" + source + "} " : " ") + warn;
        println(WARNINGS, CL_ORANGE + line);
    }

    /**
     * Printed on all four levels {@link Log#ALL}, {@link Log#NO_DEBUG}, {@link Log#WARNINGS} and {@link Log#FATAL_ONLY}. Use this method to document fatal failures in vital functions.
     * Includes the source class of the method call in the log.
     *
     * @param error the error to be logged
     */
    public static void fatal(String error) {
        StackTraceElement source = source();
        String line = "[FATAL] {" + source + "} " + error;
        println(FATAL_ONLY, CL_RED + line);
    }

    /**
     * Printed on all four levels {@link Log#ALL}, {@link Log#NO_DEBUG}, {@link Log#WARNINGS} and {@link Log#FATAL_ONLY}. Use this method to document fatal failures in vital functions.
     * Includes the source class of the method call in the log as well as a second source class further down the stacktrace.
     *
     * @param error  the error to be logged
     * @param stepUp the amount of steps to take starting at the default calling class to reach the second class to log;
     *               e.g. if the stacktrace is <code>A -> B -> C -> D -> debug</code>
     *               then D would be the default calling class and a stepUp of 2 would specify B as the second one to log.
     */
    public static void fatal(String error, int stepUp) {
        StackTraceElement source = source(stepUp);
        StackTraceElement source2 = source();
        String line = "[FATAL] {" + source + " -> " + source2 + "} " + error;
        println(FATAL_ONLY, CL_RED + line);
    }

    /**
     * Printed on all four levels {@link Log#ALL}, {@link Log#NO_DEBUG}, {@link Log#WARNINGS} and {@link Log#FATAL_ONLY}. Use this method to document fatal failures in vital functions.
     * May be used to exclude the source class of the method call from the log.
     *
     * @param error       the error to be logged
     * @param printSource if set to false, the source class is excluded; if set to true, the call is identical to {@link Log#fatal(String)}
     */
    public static void fatal(String error, boolean printSource) {
        StackTraceElement source = source();
        String line = "[FATAL]" + (printSource ? " {" + source + "} " : " ") + error;
        println(FATAL_ONLY, CL_RED + line);
    }

    /**
     * Always printed. Use this method to make temporary manual debugging messages, that should get removed before any major update or release.
     * Includes the source class of the method call in the log.
     *
     * @param text the debug to be logged
     */
    public static void p(String text) {
        StackTraceElement source = source();
        String line = "[MANTEST] {" + source + "} " + text;
        println(-1, CL_PURPLE + line);
    }

    /**
     * Always printed. Use this method to make temporary manual debugging messages, that should get removed before any major update or release.
     * Includes the source class of the method call in the log as well as a second source class further down the stacktrace.
     *
     * @param text   the debug to be logged
     * @param stepUp the amount of steps to take starting at the default calling class to reach the second class to log;
     *               e.g. if the stacktrace is <code>A -> B -> C -> D -> debug</code>
     *               then D would be the default calling class and a stepUp of 2 would specify B as the second one to log.
     */
    public static void p(String text, int stepUp) {
        StackTraceElement source = source(stepUp);
        StackTraceElement source2 = source();
        String line = "[MANTEST] {" + source + " -> " + source2 + "} " + text;
        println(-1, CL_PURPLE + line);
    }

    /**
     * Report a crash of the engine and log the actual exception.
     *
     * @param e the exception causing the crash
     */
    public static void crash(Exception e) {
        String line = "[CRASH] {" + e.getStackTrace()[0] + "} " + e.getMessage();
        if (e.getStackTrace().length >= 2)
            line = "[CRASH] {" + e.getStackTrace()[e.getStackTrace().length - 1] + " -> " + e.getStackTrace()[0] + "} " + e.getMessage();
        println(-1, CL_RED + line);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        e.printStackTrace(ps);
        loggingThread.log(bos.toString(), -1);
        loggingThread.save();
        e.printStackTrace();
    }

    private static void println(int level, String line) {
        if (logLevel >= level) System.out.println(line);
        if (loggingThread != null) loggingThread.log(line.substring(CL_RED.length()), level);
    }

}