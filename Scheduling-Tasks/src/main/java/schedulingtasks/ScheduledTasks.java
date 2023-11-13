package schedulingtasks;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @author someexp
 * @date 2021/5/31
 */
@Component
public class ScheduledTasks implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final ExecutorService TASK1_EXEC = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1), new ThreadFactoryBuilder().setNameFormat("task01-thread-%d").build());

    /**
     * 使用 SynchronousQueue 避免任务排队
     */
    private static final ExecutorService TASK1_EXEC2 = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new SynchronousQueue<>(), new ThreadFactoryBuilder().setNameFormat("task01-thread-%d").build());

    /**
     * 使用 SynchronousQueue 避免任务排队, 饱和策略 CallerRuns (Caller 会被阻塞)
     */
    private static final ExecutorService TASK1_EXEC3 = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new SynchronousQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("task01-thread-%d").build(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    // cron 每天的 1 : 00 AM 执行一次
    @Scheduled(cron = "0 0 1 * * ? ")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0/1 * * * * ? ")
    public void test01() {
        log.info("ThreadName: {}, 提交任务", Thread.currentThread().getName());
        try {
            TASK1_EXEC3.execute(new Task1());
            log.info("ThreadName: {}, 提交成功", Thread.currentThread().getName());
        } catch (RejectedExecutionException rejectedExecutionException) {
            log.warn("ThreadName: {}, task01被拒绝, message: [{}]", Thread.currentThread().getName(), rejectedExecutionException.getMessage());
        } catch (Exception e) {
            log.error("ThreadName: {}, task01 提交任务异常", Thread.currentThread().getName(), e);
        }
    }

    //    @Scheduled(cron = "0/3 * * * * ? ")
    public void test02() throws InterruptedException {
        log.info("2 start");
        Thread.sleep(4000L);
        log.info("2 end");
    }

    @Override
    public void destroy() throws Exception {
        if (!TASK1_EXEC.isShutdown()) {
            TASK1_EXEC.shutdown();
        }
    }
}
