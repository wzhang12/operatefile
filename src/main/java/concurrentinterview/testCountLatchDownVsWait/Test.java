package concurrentinterview.testCountLatchDownVsWait;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


/**
 * Created by DELL on 2015/12/24.
 */
public class Test {

    private static final int THREADS = 10;
    private static final int ITERATIONS = 100000;

    public static void main(String[] args) {
        // warmup
        runTest(new ArrayList<Long>(1000), 100, 10);

        // actual test
        int iterations = ITERATIONS;
        int nThreads = THREADS;
        final List<Long> times = Collections
                .synchronizedList(new ArrayList<Long>(nThreads * iterations));
        runTest(times, iterations, nThreads);

        long total = 0;
        for (long time : times) {
            total += time;
        }
        System.err.println("Total spent on toggleCondition(): " +
                ((double) total / 1000000));
        System.err.println("Average time spent on toggleCondition(): " +
                ((double) total / 1000000) / ITERATIONS);
    }

    private static void runTest(final List<Long> times, int iterations,
                                int nThreads) {
        for (int i = 0; i < iterations; i++) {
            Thread[] threads = new Thread[nThreads];
            // Switch these for different tests.
            //final Condition condition = new WaitCondition(nThreads);
            final Condition condition = new LatchCondition(nThreads);
            for (int j = 0; j < nThreads; j++) {
                threads[j] = new Thread(new Runnable() {
                    public void run() {
                        long start, end;
                        start = System.nanoTime();
                        try {
                            condition.toggleCondition();
                        } catch (InterruptedException ignored) { }
                        end = System.nanoTime();
                        times.add(end - start);
                    }
                });
                threads[j].setName("thread-" + j);
            }

            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException ignored) { }
            }
        }
    }

    public static interface Condition {

        void toggleCondition() throws InterruptedException;
    }

    public static class WaitCondition implements Condition {

        private final int number;
        private int waiters;

        public WaitCondition(int number) {
            this.number = number;
            this.waiters = 0;
        }

        public void toggleCondition() throws InterruptedException {
            synchronized (this) {
                this.waiters++;
                if (this.waiters >= this.number) {
                    this.notifyAll();
                } else {
                    this.wait();
                }
                this.waiters--;
            }
        }
    }

    public static class LatchCondition implements Condition {

        private final CountDownLatch latch;

        public LatchCondition(int number) {
            this.latch = new CountDownLatch(number);
        }

        public void toggleCondition() throws InterruptedException {
            this.latch.countDown();
            this.latch.await();
        }
    }
}