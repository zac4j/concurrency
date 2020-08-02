package concurrency;

import java.text.SimpleDateFormat;
import java.util.Date;

class SimpleThread {

    private static String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
            "A kid will eat ivy too" };

    public static void main(String[] args) throws InterruptedException {
        // Delay, in milliseconds before we interrupt MessageLoop thread.
        long patience = 1000 * 60 * 60;

        // If command line argument present, gives patience in seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000L;
            } catch (Exception e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }

        threadMessage("Starting MsgLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MsgLoop());
        t.start();

        threadMessage("Waiting for MsgLoop thread to finish");
        // Loop until MsgLoop thread exits
        while (t.isAlive()) {
            threadMessage("Still waiting..." + getFormattedTime());
            // Waiting maximum of 1 second for MsgLoop thead to finish.
            t.join(1000L);
            if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                // Shouldn't be long now wait indefinitely.
                t.join();
            }
        }

        threadMessage("Finished!");
    }

    // Display a message, preceded by the name of the current thread
    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }

    static String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");

        String time = formatter.format(new Date());

        return time;
    }

    /**
     * Message Loop
     */
    private static class MsgLoop implements Runnable {

        @Override
        public void run() {
            int size = importantInfo.length;
            try {
                for (int i = 0; i < size; i++) {
                    // Pause for 4 seconds.
                    Thread.sleep(2000L);
                    // Print a message
                    threadMessage("After sleep >> " + getFormattedTime() + " >> " + importantInfo[i]);
                }
            } catch (Exception e) {
                threadMessage("I wasn't done!");
            }
        }
    }
}