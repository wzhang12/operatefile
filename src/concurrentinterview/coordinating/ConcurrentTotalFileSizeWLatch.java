/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/
package concurrentinterview.coordinating;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentTotalFileSizeWLatch {  
  private ExecutorService service;
  final private AtomicLong pendingFileVisits = new AtomicLong();
  final private AtomicLong totalSize = new AtomicLong();
  final private CountDownLatch latch = new CountDownLatch(1);
  private void updateTotalSizeOfFilesInDir(final File file) {
    long fileSize = 0;
    if (file.isFile())
      fileSize = file.length();
    else {      
      final File[] children = file.listFiles();      
      if (children != null) {
        for(final File child : children) {
          if (child.isFile())
            fileSize += child.length();
          else {
            pendingFileVisits.incrementAndGet();
            service.execute(new Runnable() {
              //这块代码交给一个线程，程序继续向下的代码块运行
              public void run() { updateTotalSizeOfFilesInDir(child); }
            });            
          }
        }
      }
    }
    totalSize.addAndGet(fileSize);
    System.out.println("ongoing");
    if(pendingFileVisits.decrementAndGet() == 0) latch.countDown();
  }

  private long getTotalSizeOfFile(final String fileName) 
    throws InterruptedException {
    service  = Executors.newFixedThreadPool(100);
    pendingFileVisits.incrementAndGet();
    try {
     updateTotalSizeOfFilesInDir(new File(fileName));
      System.out.println("waiting");
      //主线程在等待，某个线程计算完最后一个文件大小
     latch.await(100, TimeUnit.SECONDS);
     return totalSize.longValue();
    } finally {
      service.shutdown();
    }
  }
  public static void main(final String[] args) throws InterruptedException {
    final long start = System.nanoTime();
    final long total = new ConcurrentTotalFileSizeWLatch()
      .getTotalSizeOfFile("C:\\Users\\DELL\\Desktop\\Getting Started with Google Guava");
    final long end = System.nanoTime();
    System.out.println("Total Size: " + total);
    System.out.println("Time taken: " + (end - start)/1.0e9);
  }
}
