package concurrency;

/**
 * @author lzr
 * @date 6/1/2022
 */

public class BarrierTimer implements Runnable {
    private boolean started;
    private long startTime, endTime;

    @Override
    public void run() {
        long t = System.nanoTime();
        if (!started) {
            started = true;
            startTime = t;
        } else {
            endTime = t;
        }
    }

    public synchronized void clear() {
        started = false;
    }

    public synchronized long getTime() {
        return endTime - startTime;
    }
}