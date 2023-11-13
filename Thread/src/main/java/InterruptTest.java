/**
 * https://juejin.cn/post/7020231777721516040
 */
public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Interrupt interrupt = new Interrupt();
        Thread thread1 = new Thread(interrupt, "thread1");
        Thread thread2 = new Thread(interrupt, "thread2");
        thread1.start();
        //主线程睡眠一秒，等待thread1获得锁,再开启thread2
        Thread.currentThread().sleep(1000);
        thread2.start();
        //主线程再次睡眠一秒，等待thread2由于获取不到锁而处于BLOCKED阻塞态（阻塞在synchronized处）
        Thread.currentThread().sleep(1000);
        //获取thread2状态
        System.out.println(thread2.getName() + "状态:" + thread2.getState());
        //尝试设置thread2的中断状态
        thread2.interrupt();
        //获取是否thread2是否处于中断,返回true,说明是,
        //但是没有抛出异常,此时我们便没办法处理这个出于阻塞态的线程
        System.out.println(thread2.getName() + "是否是中断状态: " + thread2.isInterrupted());
        System.out.println(thread2.getName() + "由于处于synchronized阻塞态,即使处于中断态,也没有抛出异常,无法结束");

        System.out.println();
        System.out.println("=========>开始处理这个问题");
        System.out.println();


        //获取thread1的状态,可以发现是TIMED_WAITING.并且是采用Thread.sleep(100)方式,可以被中断并抛出异常
        System.out.println(thread1.getName() + "状态:" + thread1.getState());
        //尝试设置thread1的中断状态
        thread1.interrupt();
        System.out.println(thread1.getName() + "是否是中断状态: " + thread1.isInterrupted());

        //由于thread1 中断了等待,处理了异常,结束了运行,释放了锁。
        //thred2获得了锁,进入同步块,执行sleep方法进入TIMED_WAITING状态
        //由于thread2在前面设置了中断状态,因此也立即从sleep方法出抛出异常,并且正常结束线程。
    }

    static class Interrupt implements Runnable {
        @Override
        public void run() {
            synchronized (Thread.class) {
                try {
                    //处于等待  该等待方法可被中断,此时会中断等待,并且抛出异常,程序可以结束.
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    //cache处理中断异常,结束等待,使得线程能正常结束运行
                    System.out.println(Thread.currentThread().getName() + "抛出了异常,结束了TIMED_WAITING,该线程可以返回");
                }
            }

        }
    }
}
