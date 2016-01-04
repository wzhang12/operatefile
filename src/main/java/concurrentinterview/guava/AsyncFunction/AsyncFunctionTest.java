package concurrentinterview.guava.AsyncFunction;

import com.google.common.util.concurrent.*;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


class AsyncFunctionTest{

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {

        ExecutorService normalService = Executors.newFixedThreadPool(100);
        final ListeningExecutorService service = MoreExecutors
                .listeningDecorator(normalService);

        ListenableFuture<Integer> future1 = service.submit(new Task1());
        AsyncFunction<Integer, String> asyncFunction = new AsyncFunction<Integer, String>() {
            @Override
            public ListenableFuture<String> apply(Integer input)
                    throws Exception {
                return service.submit(new Task2(input));
            }
        };
        ListenableFuture<String> futures2 = Futures.transform(future1,
                asyncFunction);
        System.out.println(futures2.get());
    }




}
class Task1 implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("task1 begin");
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(2000);
        System.out.println("task1 end");
        return new Random().nextInt();
    }
}

// add suffix to an integer
class Task2 implements Callable<String> {

    private Integer i;

    public Task2(Integer i) {
        this.i = i;
    }

    @Override
    public String call() throws Exception {
        System.out.println("task2 begin");
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(4000);
        System.out.println("task2 end");
        return i + "suffix";
    }
}





