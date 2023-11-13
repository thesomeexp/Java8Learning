package concurrency;

/**
 * @author liangzhirong
 * @date 2/23/2022
 */
public class TaskRunnable implements Runnable {

    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) { // #1 该行代码检测当前线程是否被中断
                System.out.println(Thread.currentThread().getName() + "检测到中断, 停止执行");
                break;
            }
            try {
                // 模拟处理任务
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "抛出 InterruptedException");
                // #2 恢复被中断的状态, 以至于上面的 #1 代码可以看到引发了中断
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new TaskRunnable());
        thread.start();
        try {
            // #3 先休眠, 然后再让线程中断
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "开始中断" + thread.getName());
        thread.interrupt();
    }
}
