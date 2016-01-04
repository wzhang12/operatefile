package concurrentinterview.printABC;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by DELL on 2015/9/9.
 */
public class PrintABC {
    private Lock lock;
    private Condition condition;
    private int count;

    public PrintABC(){
        lock=new ReentrantLock();
        condition=lock.newCondition();
    }
    public void printABC(char charc,int i){
        try {
            lock.lock();
            while (true) {
                if (count % 3 == i - 1) {
                    System.out.print(charc);
                    count++;
                    condition.signalAll();
                    break;
                } else {
                    condition.await();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
