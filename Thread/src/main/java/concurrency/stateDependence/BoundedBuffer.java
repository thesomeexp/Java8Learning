package concurrency.stateDependence;

import net.jcip.annotations.ThreadSafe;

/**
 * 使用 内置条件队列 实现的有界缓存 (ConditionBoundedBuffer 还好更好, 它能使用单一通知方法而不是 notifyAll, 效率更高)
 * 与内置锁相同, 内置条件队列并不提供公平的排队操作, 而在显示的 Condition 却可以提供公平或非公平的排队操作
 *
 * @author lzr
 * @date 6/21/2022
 */

@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }

    public BoundedBuffer(int size) {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull())
            wait();
        doPut(v);
        notifyAll();
    }

    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        while (isEmpty())
            wait();
        V v = doTake();
        notifyAll();
        return v;
    }

    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull())
            wait();
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
}