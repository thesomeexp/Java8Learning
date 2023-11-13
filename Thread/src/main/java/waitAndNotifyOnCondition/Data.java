package waitAndNotifyOnCondition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lzr
 * @date 6/8/2022
 */

public class Data {
    private String packet;

    // True if receiver should wait
    // False if sender should wait
    private boolean transfer = true;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public String receive() {
        String curPacket = null;
        lock.lock();
        try {
            while (transfer) {
                condition.await();
            }
            transfer = true;
            curPacket = packet;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return curPacket;
    }

    public void send(String packet) {
        lock.lock();
        try {
            while (!transfer) {
                condition.await();
            }
            transfer = false;

            this.packet = packet;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}