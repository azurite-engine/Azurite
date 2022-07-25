package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class LoggingThread extends Thread {

    public static String LOG_NAME = "Azurite";

    public static int CYCLE = 60; // 60 seconds

    private final File path;

    private final File target;

    private final Queue<String> logQueue;

    private int logLevel;

    public LoggingThread(File path, int logLevel) {
        this.path = path;
        String name = LOG_NAME + " " + new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date()) + ".log";
        this.target = new File(path, name);
        this.logLevel = Math.abs(logLevel) % 4;
        this.logQueue = new ConcurrentLinkedQueue<>();
    }

    public boolean init() throws IOException {
        if (!path.exists()) {
            if (path.mkdirs())
                return target.createNewFile();
        }
        else return target.createNewFile();
        return false;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public void log(String log, int logLevel) {
        if (this.logLevel < logLevel) return;
        logQueue.offer(log);
    }

    protected void save() {
        //determine size of writing block and if writing is necessary
        int size = logQueue.size();
        if (size > 0) {
            try (FileWriter fw = new FileWriter(target, true)) {
                //write the set amount of lines
                for (int i = 0; i < size; i++) {
                    fw.write(logQueue.poll() + "\n");
                }
                fw.flush();
            } catch (IOException e) {
                Log.fatal("Exception in the logger... we are sorry, no logs for you.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        int c;
        while (Engine.isRunning()) {
            c = 0;
            while (Engine.isRunning() && c < CYCLE) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
                c++;
            }
            save();
        }
    }

}

