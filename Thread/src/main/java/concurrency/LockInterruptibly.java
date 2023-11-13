package concurrency;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lzr
 * @date 6/16/2022
 */

public class LockInterruptibly {
    private static final ReentrantLock lock = new ReentrantLock();

    public boolean sendOnSharedLine(String message) throws InterruptedException {
        // 在等待锁的过程中可以被中断
        lock.lockInterruptibly();
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
        LockInterruptibly test0 = new LockInterruptibly();
        LockInterruptibly test1 = new LockInterruptibly();

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