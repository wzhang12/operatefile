package concurrentinterview.interrupt;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 来源：http://agapple.iteye.com/blog/970055
 * Thread.interrput方法总结：
 * 1. 对于Object的sleep、wait和join方法，调用其线程的interrupt方法时会抛出InterruptedException异常，
 * 对于非sleep、wait和join方法，interrupt方法只会设置中断标志位，不会抛出InterruptedException，因此需要自己进行相应的处理
 * 
 * 2. interrupt方法可以换新执行了park方法的休眠线程
 * 
 * 3. 执行park方法设置的parkBlocker属性的notifyAll方法并不会唤醒线程
 * @author longcheng
 *
 */
public class InterruptAndLockSupportTest02 {

	private static InterruptAndLockSupportTest02 blocker = new InterruptAndLockSupportTest02();

	public static void main(String args[]) throws Exception {
//		lockSupportTest();
//		interruptWaitTest();
//		interruptSleepTest();
//		interruptParkTest();
		parkTest();
	}

	/**
	 * 测试一：
	 * LockSupport.park阻塞测试线程，主线程调用blocker的notifyAll不能把它唤醒
	 * @throws Exception
	 */
	public static void lockSupportTest() throws Exception {
		Thread t = doTest(new TestCallBack() {

			@Override
			public void callback() throws Exception {
				// 尝试sleep 5s
				System.out.println("blocker");
				LockSupport.park(blocker);
				System.out.println("wakeup now!");
			}

		});
		t.start(); // 启动读取线程

		Thread.sleep(150);
		synchronized (blocker) {
			Field field = Thread.class.getDeclaredField("parkBlocker");
			field.setAccessible(true);
			Object fBlocker = field.get(t);
			System.out.println(blocker == fBlocker);
			Thread.sleep(100);
			System.out.println("notifyAll");
			// 注意这里调用的是blocker的notifyAll方法，而不是测试线程t的notifyAll方法
			blocker.notifyAll();
		}
	}

	/**
	 * 测试2：
	 * 主线程调用测试线程的interrupt方法，若测试线程在执行wait方法，而会抛出对应的InterruptedException异常
	 * 对Object的wait和join方法同样如此
	 * 
	 * @throws InterruptedException
	 */
	protected static void interruptWaitTest() throws InterruptedException {
		final Object obj = new Object();
		
		Thread t = doTest(new TestCallBack() {

			@Override
			public void callback() throws Exception {
				obj.wait();
				System.out.println("wakeup now!");
			}

		});
		t.start(); // 启动读取线程
		Thread.sleep(1000);
		t.interrupt(); // 检查下在park时，是否响应中断
	}

	/**
	 * 主线程调用测试线程的interrupt，若测试线程执行park，则能够把线程唤醒，并且不会抛出InterruptedException异常
	 * 
	 * @throws InterruptedException
	 */
	protected static void interruptParkTest() throws InterruptedException {
		Thread t = doTest(new TestCallBack() {

			@Override
			public void callback() {
				// 尝试去park 自己线程
				LockSupport.parkNanos(blocker, TimeUnit.SECONDS.toNanos(1));
				System.out.println("wakeup now!");
			}
		});
		t.start(); // 启动读取线程
		Thread.sleep(2000);
		t.interrupt(); // 检查下在park时，是否响应中断
	}

	/**
	 * 同样，测试线程执行park了，然后再执行unpark后，接着主线程调用它的interrupt方法时能唤醒线程
	 * @throws InterruptedException
	 */
	protected static void parkTest() throws InterruptedException {
		Thread t = doTest(new TestCallBack() {

			@Override
			public void callback() {
				// 尝试去park 自己线程
				System.out.println("blocker...");
				LockSupport.park(blocker);
				System.out.println("wakeup now!");
			}
		});

		t.start(); // 启动读取线程
		Thread.sleep(2000);
		System.out.println("unpack...");
		LockSupport.unpark(t);
		t.interrupt();
	}

	public static Thread doTest(final TestCallBack call) {
		return new Thread() {

			@Override
			public void run() {
				try {
					while(true) {
						// 对于非sleep、wait和join方法，interrupt只会设置中断标志位，不会抛出InterruptedException
						// 因此需要自己进行相应的处理
						if (Thread.interrupted()) {
							throw new InterruptedException("自定义InterruptedException异常抛出");
						}
						call.callback();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
}

interface TestCallBack {
	public void callback() throws Exception;
}