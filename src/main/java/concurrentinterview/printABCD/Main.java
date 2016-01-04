package concurrentinterview.printABCD;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * four threads only can print A B C D accordingly,coordinate each other print
 * file1:abcdabcdabcdabcd...
 * file2:bcdabcdabcdabcd...
 * file3:cdabcdabcdabcd...
 * file4:dabcdabcdabcd...
 */
public class Main {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        PrintABCD printABCD = new PrintABCD();

        for (int i = 0; i < 100; i++) {
            printABCD.getCyclicBarrier().reset();
            executorService.execute(new Job(printABCD, 'A', 4));
            executorService.execute(new Job(printABCD, 'B', 3));
            executorService.execute(new Job(printABCD, 'C', 2));
            executorService.execute(new Job(printABCD, 'D', 1));
            printABCD.getCyclicBarrier().await();
        }


        executorService.shutdown();
    }
}
