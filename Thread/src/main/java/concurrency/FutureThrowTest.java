package concurrency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author lzr
 * @date 2022-04-18
 */
public class FutureThrowTest {

    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("CPU 核心数: " + Runtime.getRuntime().availableProcessors());
//        singleTaskTest();
            System.out.println("------");
            multiTaskTest();
            System.out.println("finish");
        } finally {
            // 不关闭的话, jvm 不会退出
            executor.shutdown();
        }
    }

    private static void singleTaskTest() throws ExecutionException, InterruptedException {
        Callable<String> task1 = new Task("task1", 1000L);
        Callable<String> task2 = new Task("task2", 2000L);
        Callable<String> task3 = new Task("task3", 3000L);
        Callable<String> task4 = new Task("task4", 4000L);
        Callable<String> task5 = new Task("task5", 5000L);


        // 提交任务并获得Future: (提交任务后, 不一定会立即执行)
        Future<String> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);
        Future<String> future3 = executor.submit(task3);
        Future<String> future4 = executor.submit(task4);
        Future<String> future5 = executor.submit(task5);
        // 从Future获取异步执行返回的结果:
        String result = future1.get();
        result = future2.get();
        result = future3.get();
        result = future4.get();
        result = future5.get();
    }

    private static void multiTaskTest() throws InterruptedException, ExecutionException {
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("task1", 1000L));
        taskList.add(new Task("task2", 2000L));
        taskList.add(new Task("task3", 3000L));
        taskList.add(new Task("task4", 4000L));
        taskList.add(new Task("task5", 5000L));

        System.out.println("提交前");
        // 阻塞
//        List<Future<String>> futures = executor.invokeAll(taskList);
        List<Future<String>> futures = executor.invokeAll(taskList, 2L, TimeUnit.SECONDS);
        System.out.println("提交后");
        Iterator<Task> taskIterator = taskList.iterator();
        for (Future<String> future : futures) {
            Task task = taskIterator.next();
            try {
                System.out.println(task.name + " 处理结果: " + future.get(1L, TimeUnit.SECONDS));
            } catch (ExecutionException e) {
                System.out.println(task.name + " 执行异常" + e.getCause());
            } catch (CancellationException e) {
                System.out.println(task.name + " 任务被取消" + e.getCause());
            } catch (TimeoutException e) {
                System.out.println(task.name + " 超时异常" + e.getCause());
            }
        }
    }

    private static class Task implements Callable<String> {

        private String name;

        private long sleepMillis;

        public Task(String name, long sleepMillis) {
            this.name = name;
            this.sleepMillis = sleepMillis;
        }

        @Override
        public String call() throws Exception {
            System.out.println(Thread.currentThread().getName() + " " + name + " do call() start");
            Thread.sleep(sleepMillis);
            System.out.println(Thread.currentThread().getName() + " " + name + " do call() end");
            return name + "result";
        }
    }

}
