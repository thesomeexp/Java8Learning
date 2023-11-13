package concurrency.test;

import java.util.concurrent.Semaphore;

/**
 * @author lzr
 * @date 6/24/2022
 */

public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);
        Thread thread0 = new Thread(() -> {
            try {
                semaphore.acquire();
                System.out.println("成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread1 = new Thread(() -> {
            try {
                semaphore.acquire();
                System.out.println("成功");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread0.start();
        thread1.start();
        try {
            Thread.sleep(1000_000_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}