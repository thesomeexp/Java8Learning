package concurrency;

import net.jcip.annotations.ThreadSafe;

/**
 * 基于 CAS 实现的非阻塞计算器
 * <p>
 * 实际情况中, 如果仅需要一个计数器或序列生成器, 那么可以直接使用 AtomicInteger 或 AtomicLong
 * 它们能提供原子的递增方法以及其它算术方法
 *
 * @author lzr
 * @date 6/24/2022
 */

@ThreadSafe
public class CasCounter {
    private SimulatedCAS value;

    public int getValue() {
        return value.get();
    }

    public int increment() {
        int v;
        do {
            v = value.get();
        } while (v != value.compareAndSwap(v, v + 1));
        return v + 1;
    }
}