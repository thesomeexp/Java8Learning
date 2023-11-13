package concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lzr
 * @date 6/17/2022
 */

public class LockFairness {
    // 公平锁, 按照发出请求的顺序获取锁
    private static final ReentrantLock lock = new ReentrantLock(true);

    public boolean sendOnSharedLine(String message) throws InterruptedException {
        lock.lock();
        try {
            System.out.println(message);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Task task = new Task("test" + i);
            Thread thread = new Thread(task);
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        System.out.println("check fairness");

    }

    public static class Task implements Runnable {
        private String message;

        public Task(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                new LockFairness().sendOnSharedLine(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}