package restoreInterrupt;

/**
 * 中断线程后, 恢复中断状态
 * https://blog.csdn.net/VincentGTX_huang/article/details/106410889
 */
public class RestoreInterrupt1 implements Runnable {
    @Override
    public void run() {
        //如果中断了,则跳出执行
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("收到中断停止执行");
                break;
            }
            this.reInterrupt();
        }
    }

    private void reInterrupt() {
        //模拟内部中断
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("当前线程是否中断: " + Thread.currentThread().isInterrupted());
            e.printStackTrace();
            // FIXME 外层方法无法感知中断
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RestoreInterrupt1());
        thread.start();
        try {
            //先休眠,然后再让线程中断
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

}
