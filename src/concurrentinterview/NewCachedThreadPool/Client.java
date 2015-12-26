package concurrentinterview.NewCachedThreadPool;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 由运行结果可以看出，cachedThreadTool的运行机制是，创建可能少的线程去执行任务
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        Service s = new Service();
        for (int i = 0; i <  3; i++) {
            Task task = new Task(new Date(), "task" + i);
            TimeUnit.SECONDS.sleep(3); // 休眠3秒，以便等待有任务执行完的线程。在信息里面能看到更丰富的 执行器的管理信息
            s.executorTask(task);
        }
        s.endService();  // 1  如果注释此代码，本程序将挂起。不会结束
    }
}
/** 模拟任务类*/
class  Task implements  Runnable{
    private Date initDate; //初始化时间
    private String name;  //任务名称
    public Task(Date initDate, String name) {
        this.initDate = initDate;
        this.name = name;
    }
    @Override
    public void run() {
        Thread t = Thread.currentThread();
        System.out.printf("  start-------------%s,任务名称：%s,时间:%s\n", t.getName(),this.name,initDate);
        try {
            long  time = (long)(Math.random() * 10); //模拟工作时间
            TimeUnit.SECONDS.sleep(time);
            System.out.printf("  end-------------%s,任务名称：%s,耗时:%s\n", t.getName(),this.name,time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
/** 服务类，接收每一个任务*/
class Service{
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    /** 接受任务，直接丢给了 线程池执行*/
    public void executorTask(Task task){
        System.out.println("service：接收了一个task");
        executor.execute(task);
        System.out.printf("service:pool执行器中线程中实际的线程数量:%d，执行器中正在执行任务的线程数量：%d，执行器中已经完成的任务数量:%d\n",executor.getPoolSize(),executor.getActiveCount(),executor.getCompletedTaskCount());
    }
    /** */
    public void endService(){
        System.out.printf("service-----------------shutdown--------------:pool执行器中线程中实际的线程数量:%d，执行器中正在执行任务的线程数量：%d，执行器中已经完成的任务数量:%d\n",executor.getPoolSize(),executor.getActiveCount(),executor.getCompletedTaskCount());
        executor.shutdown();
    }
}