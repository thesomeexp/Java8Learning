package restoreInterrupt;

public class RestoreInterrupt2 implements Runnable {

    @Override
    public void run() {
        //如果中断了,则跳出执行
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName() + "收到中断停止执行");
                break;
            }
            reInterrupt();
        }
    }

    private void reInterrupt() {
        //模拟内部中断
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + Thread.currentThread().isInterrupted());
            e.printStackTrace();
            //检测中断状态
            System.out.println(Thread.currentThread().getName() + Thread.currentThread().isInterrupted());
            //添加此行代码,因为虽然中断了,线程中断被捕获,然后中断消息也会被清除,
            //所以加入Thread.currentThread.interrupt();
            //手动恢复中断状态
            Thread.currentThread().interrupt();
            //检测中断状态是否恢复
            System.out.println(Thread.currentThread().getName() + Thread.currentThread().isInterrupted());
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RestoreInterrupt2());
        thread.start();
        try {
            //先休眠,然后再让线程中断
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "异常");
            e.printStackTrace();
        }
        thread.interrupt();
    }
}
