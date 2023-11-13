package concurrency;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lzr
 * @date 6/1/2022
 */

public class PutTakeTest extends TestCase {
    protected static final ExecutorService pool = Executors.newCachedThreadPool();
    protected CyclicBarrier barrier;
    protected final SemaphoreBoundedBuffer<Integer> bb;
    protected final int nTrials, nPairs;
    protected final AtomicInteger putSum = new AtomicInteger(0);
    protected final AtomicInteger takeSum = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        new PutTakeTest(10, 10, 100000).test(); // sample parameters
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }

    public PutTakeTest(int capacity, int npairs, int ntrials) {
        this.bb = new SemaphoreBoundedBuffer<Integer>(capacity);
        this.nTrials = ntrials;
        this.nPairs = npairs;
        this.barrier = new CyclicBarrier(npairs * 2 + 1);
    }

    void test() {
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer(i));
                pool.execute(new Consumer(i));
            }
            System.out.println("等待生产者消费者们准备完");
            barrier.await(); // wait for all threads to be ready
            System.out.println("开始测试");
            barrier.await(); // wait for all threads to finish
            System.out.println("都测试完了");
            assertEquals(putSum.get(), takeSum.get());
            System.out.println("总数校验通过");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    class Producer implements Runnable {

        int round;

        public Producer(int round) {
            this.round = round;
        }

        @Override
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int) System.nanoTime());
                int sum = 0;
//                System.out.println("第" + round + "个, t: " + Thread.currentThread().getName() + ", 生产者准备好了");
                barrier.await();
                for (int i = nTrials; i > 0; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
//                System.out.println("第" + round + "个, t: " + Thread.currentThread().getName() + ", 生产者处理完了");
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {

        int round;

        public Consumer(int round) {
            this.round = round;
        }

        @Override
        public void run() {
            try {
//                System.out.println("第" + round + "个, t: " + Thread.currentThread().getName() + ", 消费者准备好了");
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
//                System.out.println("第" + round + "个, t: " + Thread.currentThread().getName() + ", 消费者处理完了");
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}