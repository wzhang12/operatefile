package concurrentinterview.printABCD;

import file.snippet.*;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by DELL on 2015/9/9.
 */
public class PrintABCD {
    private CyclicBarrier cyclicBarrier;
    private volatile int count;

    public PrintABCD() {
        cyclicBarrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                PrintABCD.this.count++;
                System.out.println(count);
            }
        });
    }

    public void printABC(char charc, int i) {
        try {
            System.out.print(charc);
            IOUtil.write("C:\\Users\\DELL\\Desktop\\threads\\" + ((i + count) % 4 + 1) + ".txt", charc + "");
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }
}
