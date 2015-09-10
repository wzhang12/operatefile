package concurrentinterview.print52Z;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by DELL on 2015/9/10.
 */
public class Print52Z {
    private Lock lock;
    private Condition condition;

    public Print52Z() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void print52Z(int num, final Object[] objects) {

        try {
            lock.lock();
            int count = 0;
            for (int i = 0; i < objects.length; i++) {


                System.out.print(objects[i]);
                count++;
                if (count == num) {
                    count = 0;
                    condition.signalAll();
                    condition.await();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

