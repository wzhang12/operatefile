package concurrentinterview.guava.futurecallback;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;

/**
 *
 */
public class FutureCallbackExample {
    private static ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ListenableFuture<String> listenableFuture =
                service.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {//这个返回值是个泛型
                        //TimeUnit.SECONDS.wait(1);会报错
                        TimeUnit.SECONDS.sleep(1);
                        return "123";
                    }
                });
        CountDownLatch countDownLatch=new CountDownLatch(1);
        FutureCallbackImpl callback = new FutureCallbackImpl(countDownLatch);
        Futures.addCallback(listenableFuture, callback);
      // Futures.addCallback(listenableFuture, callback, service);
        System.out.println("aa");
        //TimeUnit.SECONDS.sleep(2);由于任务提交后主线程继续执行因为主线程执行的较快，所以想要打印下去要等待处理完
        countDownLatch.await();//
        System.out.println(listenableFuture.getClass());//这里不知道具体的子类类型要是可以知道就好了！！！！
        System.out.println(callback.getCallbackResult() + "...");
        service.shutdown();

    }
}