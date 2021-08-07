package util;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AssetWatcher implements Runnable {

    /** The list of files present in given directory upon creation of this object */
    private final Map<File, Long> watched;
    /** The directory to be watched */
    private final File directory;
    /** The executor which will take care of running the watcher in a background thread */
    private final ScheduledExecutorService executor;
    /** The files that have been modified between two updates
     * As OpenGL calls only work within the main thread, modifications cannot be made from the run method, which belongs to another thread.
     * Trying to do so would result in a crash.
     * This synchronized list acts as a bridge between the two threads.
     * */
    private final List<File> modified;
    /** A consumer which will be called for every modified task (cheap event system) */
    private final Consumer<File> onModification;

    /**
     * @param directory the directory to be watched
     * @param delayInSeconds the delay, in seconds, between two updates of the watcher
     * @param onModification consumer used as a callback
     */
    public AssetWatcher(String directory, int delayInSeconds, Consumer<File> onModification) {
        this.watched = new HashMap<>();
        this.directory = new File(directory);
        this.executor = Executors.newSingleThreadScheduledExecutor();
        // This list must be synchronized, to allow both main thread and watcher thread to access its contents
        this.modified = Collections.synchronizedList(new ArrayList<>());
        this.onModification = onModification;

        for (File file : getFiles()) {
            // Put each file in map, paired with its last modification (which will be later used to check for new modifications)
            watched.put(file, file.lastModified());
        }

        // The run method will be executed following given time, until the executor is shutdown.
        executor.scheduleAtFixedRate(this, 0, delayInSeconds, TimeUnit.SECONDS);
    }

    /**
     * @return the files currently present in the directory
     */
    private List<File> getFiles() {
        return Utils.getFilesInDirectory(directory);
    }

    /**
     * For each file watched, compare its last modification with the last value inside the hashmap
     * Updates the watched map, removing files if they were deleted
     */
    @Override
    public void run() {
        // Gets the list of all the files inside the directory
        List<File> temp = getFiles();

        Iterator<File> iterator = watched.keySet().iterator();
        while (iterator.hasNext()) {
            File file = iterator.next();

            if (!temp.contains(file)) {
                // File was removed between two updates!
                iterator.remove();
            } else if (!watched.get(file).equals(file.lastModified())) {
                // If the two values do not match, a modification has been made
                modified.add(file);
                // Add the file to the list of modified files
                watched.replace(file, file.lastModified());
                // Reset its last modification
            }
        }
    }

    /**
     * (Main thread) iterate through modified files and accept consumer, then clear the list for the next update
     */
    public void update() {
        synchronized (modified) {
            modified.forEach(onModification);
            modified.clear();
        }
    }

    /**
     * Shutdown the executor
     */
    public void stop() {
        executor.shutdown();
    }

}