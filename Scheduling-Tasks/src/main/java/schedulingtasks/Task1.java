package schedulingtasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lzr
 * @date 2022-04-22
 */
public class Task1 implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Task1.class);

    @Override
    public void run() {
        log.info("ThreadName: {}, 1 start", Thread.currentThread().getName());
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            log.info("ThreadName: {}, 1 exception", Thread.currentThread().getName());
            throw e;
        }
        log.info("ThreadName: {}, 1 end", Thread.currentThread().getName());
    }
}
