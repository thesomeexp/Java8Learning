package concurrency;

import java.util.concurrent.*;

/**
 * 8.1.1 线程饥饿死锁
 *
 * @author lzr
 * @date 2022-04-21
 */
public class ThreadDeadlockTest {
    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public String call() throws Exception {
            // Here's where we would actually read the file
            return "";
        }
    }

    public class RenderPageTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new ThreadDeadlockTest.LoadFileTask("header.html"));
            footer = exec.submit(new ThreadDeadlockTest.LoadFileTask("footer.html"));
            String page = renderBody();
            // Will deadlock -- task waiting for result of subtask
            return header.get() + page + footer.get();
        }

        private String renderBody() {
            // Here's where we would actually render the page
            return "";
        }
    }

    public static void main(String[] args) {
        ThreadDeadlockTest threadDeadlockTest = new ThreadDeadlockTest();
        Future<String> submit = threadDeadlockTest.exec.submit(threadDeadlockTest.new RenderPageTask());
        try {
            String result = submit.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("finish");
    }
}
