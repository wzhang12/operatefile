package guavaexample.threads;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * Created by DELL on 2015/7/31.
 */
public class LearningAPI {
    @Test
    public void future() throws Exception {

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture future1 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 1.");
                return 1;
            }
        });

        ListenableFuture future2 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 2.");
                //       throw new RuntimeException("----call future 2.");
                return 2;
            }
        });

        final ListenableFuture allFutures = Futures.allAsList(future1, future2);

        final ListenableFuture transform = Futures.transform(allFutures, new AsyncFunction<List<Integer>, Boolean>() {
            @Override
            public ListenableFuture apply(List<Integer> results) throws Exception {
                System.out.println("apply开始");
                Thread.sleep(6000);
                System.out.println("apply结束");
                return Futures.immediateFuture(1L);
            }
        });

        Futures.addCallback(transform, new FutureCallback<Object>() {

            public void onSuccess(Object result) {
                for (int i = 0; i < 1000000000; i++) {
                    i++;
                }
                System.out.println(result.getClass());
                System.out.printf("success with: %s%n", result);
            }

            public void onFailure(Throwable thrown) {
                System.out.printf("onFailure%s%n", thrown.getMessage());
            }
        });
        System.out.println("main处理开始");
        Thread.sleep(5000);
        System.out.println("main处理完");
        System.out.println(transform.get());
        System.out.println("是否阻塞");//由于main执行完了 但是
        System.out.println(service.isTerminated());

//        result: 猜测，由于主线程执行完了，但是onSuccess没有执行完程序终止，
//        main处理开始
//        call future 2.
//        call future 1.
//        apply开始
//                main处理完
//        apply结束
//        1
//        是否阻塞
//        false

    }

    @Test
    public  void testCollection(){
        String[] subdirs = { "usr", "local", "lib" };
        String directory = Joiner.on("/").join(subdirs);
        System.out.println(directory);
    }
}
