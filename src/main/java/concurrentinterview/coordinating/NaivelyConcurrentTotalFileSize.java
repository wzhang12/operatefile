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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class NaivelyConcurrentTotalFileSize {
  private long getTotalSizeOfFilesInDir(
    final ExecutorService service, final File file) 
    throws InterruptedException, ExecutionException, TimeoutException {
    if (file.isFile()) return file.length();
    
    long total = 0;
    final File[] children = file.listFiles();
    
    if (children != null) {
      final List<Future<Long>> partialTotalFutures = 
        new ArrayList<Future<Long>>();      
      for(final File child : children) {
        partialTotalFutures.add(service.submit(new Callable<Long>() { 
          public Long call() throws InterruptedException, 
            ExecutionException, TimeoutException { 
            return getTotalSizeOfFilesInDir(service, child); 
          }
        }));
      }
      
      for(final Future<Long> partialTotalFuture : partialTotalFutures) 
        total += partialTotalFuture.get(100, TimeUnit.SECONDS); 
    }
    
    return total;
  }

  private long getTotalSizeOfFile(final String fileName) 
    throws InterruptedException, ExecutionException, TimeoutException {
	  final ExecutorService service = Executors.newFixedThreadPool(100);
	  try {
	    return getTotalSizeOfFilesInDir(service, new File(fileName)); 
	  } finally {
		  service.shutdown();
	  }
  }
    
  public static void main(final String[] args) 
    throws InterruptedException, ExecutionException, TimeoutException {
    final long start = System.nanoTime();
    final long total = new NaivelyConcurrentTotalFileSize()
      .getTotalSizeOfFile(args[0]);
    final long end = System.nanoTime();
    System.out.println("Total Size: " + total);
    System.out.println("Time taken: " + (end - start)/1.0e9);
  }
}
