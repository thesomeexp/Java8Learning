package concurrency;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lzr
 * @date 2022-04-18
 */
public class PrivateExecutor {

    public static void main(String[] args) throws InterruptedException {
        PrivateExecutor p = new PrivateExecutor();
        Set<String> hosts = new HashSet<>();
        hosts.add("0.0.0.1");
        hosts.add("0.0.0.2");

        p.checkMail(hosts, 2L, TimeUnit.SECONDS);

    }

    /**
     * 7.2.4 只执行一次的任务
     *
     * @param hosts
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for (final String host : hosts) {
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (checkMail(host)) {
                            hasNewMail.set(true);
                        }
                    }
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }
        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        try {
            System.out.println("开始处理" + host);
            Thread.sleep(10000L);
            System.out.println("完成处理" + host);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
