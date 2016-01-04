package concurrentinterview.guava.SettableFuture;

import com.google.common.util.concurrent.*;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 猜想1的具体类型应该是SettableFuture,要是知道2的具体类型就好了.然后试了试 这里Futures$FallbackFuture类型，Futures里面的一个内部内。。。= =
 */
public class FutureFallbackExample {
    private static ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ListenableFuture future = service.submit(new Runnable() {
            @Override
            public void run() {
                throw new IllegalArgumentException("test");
            }
        });
        FutureFallbackImpl futureFallback = new FutureFallbackImpl();
        ListenableFuture<String> newFuture = Futures.withFallback(future, futureFallback);
        System.out.println(newFuture.get());// 1

        future = service.submit(new Runnable() {
            @Override
            public void run() {
            }
        });
        futureFallback = new FutureFallbackImpl();
        newFuture = Futures.withFallback(future, futureFallback);
        TimeUnit.SECONDS.sleep(2);
        System.out.println(newFuture.get());// 2
        service.shutdown();
    }
}