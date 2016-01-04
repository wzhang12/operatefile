package concurrentinterview.guava;

import com.google.common.util.concurrent.Monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/6/23.
 */
public class MonitorExample {
    private List<String> list = new ArrayList<String>();
    private static final int MAX_SIZE = 10;

    private Monitor monitor = new Monitor();
    private Monitor.Guard listBelowCapacity = new Monitor.Guard(monitor) {

        @Override
        public boolean isSatisfied() {
            return list.size() < MAX_SIZE ;
        }


    };
    private Monitor.Guard listUpCapacity = new Monitor.Guard(monitor) {

        @Override
        public boolean isSatisfied() {
            return list.size()>0  ;
        }


    };

    public void addToList(String item) throws InterruptedException {
        monitor.enterWhen(listBelowCapacity);
        try {
            list.add(item);
        } finally {
            monitor.leave();
        }
    }

    public void removeItem()throws InterruptedException {
        monitor.enterWhen(listUpCapacity);
        try {
            list.remove(0);
        } finally {
            monitor.leave();
        }
    }

    public static void main(String[] arg) throws InterruptedException {
        final MonitorExample example = new MonitorExample();
        final ExecutorService EXEC = Executors.newCachedThreadPool();

            EXEC.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            example.addToList("1");
                            System.out.println("add item");
                        TimeUnit.MICROSECONDS.sleep(100);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        EXEC.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        example.removeItem();
                        System.out.println("remove item");
                        TimeUnit.MICROSECONDS.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        TimeUnit.SECONDS.sleep(30);
    }
}