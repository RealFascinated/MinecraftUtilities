package xyz.mcutils.backend.common;

public class Timer {

    /**
     * Schedules a task to run after a delay.
     *
     * @param runnable the task to run
     * @param delay the delay before the task runs
     */
    public static void scheduleRepeating(Runnable runnable, long delay, long period) {
        new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delay, period);
    }
}
