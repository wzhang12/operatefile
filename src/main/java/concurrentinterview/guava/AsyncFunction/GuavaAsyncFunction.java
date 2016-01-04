package concurrentinterview.guava.AsyncFunction;

import com.google.common.util.concurrent.*;

import java.util.concurrent.*;

public class GuavaAsyncFunction {
    public static void main(String[] args) {

        ExecutorService deletegate = Executors.newFixedThreadPool(1);
        ExecutorService deletegateForAsyncFunction = Executors.newFixedThreadPool(1);

        ListeningExecutorService pool = MoreExecutors.listeningDecorator(deletegate);
        ListeningExecutorService poolForAsyncFunction = MoreExecutors.listeningDecorator(deletegateForAsyncFunction);
        ListenableFuture<String> resultFromWorker = pool.submit(new Worker());

        ListenableFuture<String> finalResult = Futures.transform(resultFromWorker, new AsyncTransformation(poolForAsyncFunction));

        Futures.addCallback(finalResult, new MyFutureCallback());

    }

    private static final class Worker implements Callable<String> {
        public String call() throws Exception {
            try {
                System.out.println("Executing in thread="+Thread.currentThread().getName());
                //simultate some work
                TimeUnit.SECONDS.sleep(3);
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
            return "CALCULATED_VALUE";
        }
    }

    /**
     * Almost like Function transformation but it is asynchronous
     */
    private static final class AsyncTransformation implements AsyncFunction<String, String> {

        private final ListeningExecutorService poolToRunFunctionIn;

        public AsyncTransformation(ListeningExecutorService poolToRunFunctionIn){
            this.poolToRunFunctionIn = poolToRunFunctionIn;
        }

        public ListenableFuture<String> apply(String input) throws Exception {
            return poolToRunFunctionIn.submit(new FunctionWorker(input));
        }

        /**
         * 'worker' for the AsyncFunction
         */
        private static final class FunctionWorker implements Callable<String> {
            private final String input;
            public FunctionWorker(String input){
                this.input = input;
            }
            public String call() throws Exception {
                try {
                    System.out.println("Executing in thread="+Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(3);
                } catch(InterruptedException ex){
                    ex.printStackTrace();
                }
                return input + "_TRANSFORMED";
            }
        }
    }

    /**
     * what do to when the ListenableFuture has been processed
     */
    private static final class MyFutureCallback implements FutureCallback<String> {
        public void onSuccess(String result) {
            System.out.println("Result from computation = " + result);
        }

        public void onFailure(Throwable t) {
            t.printStackTrace();
        }
    }
}