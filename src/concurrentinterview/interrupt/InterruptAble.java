package concurrentinterview.interrupt;

import sun.nio.ch.Interruptible;

/**
 * 来源：http://agapple.iteye.com/blog/970055
 * 下面程序的特点是，当线程中断后能立刻进行响应，但这不是通过轮询来实现的（run方法中while循环）
 * 这样在不知道什么时候会出现中断的情况下十分有用
 * 下面是说明：
 * 代码说明，几个取巧的点：
 * 	位置1：利用sun提供的blockedOn方法，绑定对应的Interruptible事件处理钩子到指定的Thread上。
 *  位置2：执行完代码后，清空钩子。避免使用连接池时，对下一个Thread处理事件的影响。
 *  位置3：定义了Interruptible事件钩子的处理方法，回调InterruptSupport.this.interrupt()方法，
 *  子类可以集成实现自己的业务逻辑。
 *
 */
interface InterruptAble { // 定义可中断的接口

    public void interruptThread() throws InterruptedException;
}

abstract class InterruptSupport implements InterruptAble {

    private volatile boolean interrupted = false;

    private Interruptible interruptor = new Interruptible() {
        @Override
        public void interrupt(Thread t) {
            interrupted = true;
            InterruptSupport.this.interruptThread(); // 位置3
        }
    };

    public final boolean execute() {
        try {
            blockedOn(interruptor); // 位置1
            if (Thread.currentThread().isInterrupted()) { // 立马被interrupted
                interruptor.interrupt(null);
            }
            // 执行业务代码
            bussiness();
        } finally {
            blockedOn(null); // 位置2
        }
        return interrupted;
    }

    public abstract void bussiness();

    public abstract void interruptThread();

    // -- sun.misc.SharedSecrets --
    static void blockedOn(Interruptible intr) {
        sun.misc.SharedSecrets.getJavaLangAccess().blockedOn(
                Thread.currentThread(), intr);
    }
}

class BusinessTest extends InterruptSupport {

    @Override
    public void bussiness() {
        try {
            while (true) {
                System.out.println("run business...");
                Thread.sleep(100);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void interruptThread() {
        System.out.println("business finish");
    }
}

 class InterruptAndLockSupportTest01 {
    public static void main(String args[]) throws Exception {
        final BusinessTest test = new BusinessTest();
        Thread t = new Thread() {

            @Override
            public void run() {
                long start = System.currentTimeMillis();
                try {
                    System.out.println("InterruptRead start!");
                    test.execute();
                } catch (RuntimeException e) {
                    System.out.println("InterruptRead end! cost time : "
                            + (System.currentTimeMillis() - start));
                }
            }
        };
        t.start();
        // 先让测试线程执行一段时间
        Thread.sleep(500);
        // 发出interrupt中断
        t.interrupt();
    }
}
