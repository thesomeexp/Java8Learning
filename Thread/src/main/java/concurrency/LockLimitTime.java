package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lzr
 * @date 6/16/2022
 */

public class LockLimitTime {
    private static final ReentrantLock lock = new ReentrantLock();

    public boolean trySendOnSharedLine(String message, long timeout, TimeUnit unit) throws InterruptedException {
        // 等待一定时间获取锁, 一定时间内获取不到返回 false
        if (!lock.tryLock(timeout, unit)) {
            return false;
        }
        try {
            return sendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

    private boolean sendOnSharedLine(String message) {
        System.out.println(message);
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                new LockLimitTime().trySendOnSharedLine("test", 3L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                new LockLimitTime().trySendOnSharedLine("test", 3L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000_000_000L);
    }
}