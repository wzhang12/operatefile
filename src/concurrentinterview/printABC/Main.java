package concurrentinterview.printABC;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DELL on 2015/9/9.
 */
public class Main {
    public static void main(String[] args){
        ExecutorService executorService=Executors.newFixedThreadPool(3);
        PrintABC printABC=new PrintABC();
        executorService.execute(new Job(printABC,'A',1));
        executorService.execute(new Job(printABC,'B',2));
        executorService.execute(new Job(printABC,'C',3));
        executorService.shutdown();
    }
}
