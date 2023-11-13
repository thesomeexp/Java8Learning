package concurrency;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lzr
 * @date 6/17/2022
 */

public class LockNonInterruptibly {
    private static final ReentrantLock lock = new ReentrantLock();

    public boolean sendOnSharedLine(String message) throws InterruptedException {
        // 在等待锁的过程中无法被中断
        lock.lock();
        try {
            return cancellableSendOnSharedLine();
        } finally {
            lock.unlock();
        }
    }

    private boolean cancellableSendOnSharedLine() {
        return true;
    }

    public static void main(String[] args) {
        LockNonInterruptibly test0 = new LockNonInterruptibly();
        LockNonInterruptibly test1 = new LockNonInterruptibly();

        Thread thread0 = new Thread(() -> {
            try {
                test0.sendOnSharedLine("test0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread1 = new Thread(() -> {
            try {
                test1.sendOnSharedLine("test1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread0.start();
        thread1.start();
        System.out.println("ready to interrupt");
        thread1.interrupt();
    }
}