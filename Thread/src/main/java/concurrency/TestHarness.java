package concurrency;

import java.util.concurrent.CountDownLatch;

/**
 * 第 5 章, 闭锁
 * 在计时测试中使用 CountDownLatch 来启动和停止线程
 *
 * @author lzr
 * @date 6/21/2022
 */

public class TestHarness {
    public long timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);

        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException ignored) {
                    ignored.printStackTrace();
                }
            });
            t.start();
        }

        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }

    public static void main(String[] args) throws InterruptedException {
        TestHarness testHarness = new TestHarness();
        testHarness.timeTasks(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("processing...");
            }
        });
    }
}