package waitAndNotify;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lzr
 * @date 6/8/2022
 */

public class ConcurrentHashMapLockTest {
    public static void main(String[] args) {
        Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
        ReentrantLock lock1 = lockMap.computeIfAbsent("key-name1", l -> new ReentrantLock());
        ReentrantLock lock2 = lockMap.computeIfAbsent("key-name1", l -> new ReentrantLock());
        ReentrantLock lock3 = lockMap.computeIfAbsent("key-name2", l -> new ReentrantLock());
        ReentrantLock lock4 = lockMap.computeIfAbsent("key-name2", l -> new ReentrantLock());
    }
}