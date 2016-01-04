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

public class TotalFileSizeSequential {
  private long getTotalSizeOfFilesInDir(final File file) {
    if (file.isFile()) return file.length();
    
    final File[] children = file.listFiles();
    long total = 0;
    if (children != null)
      for(final File child : children) 
        total += getTotalSizeOfFilesInDir(child);      
    return total;
  }
  
  public static void main(final String[] args) {
    final long start = System.nanoTime();
    final long total = new TotalFileSizeSequential()
      .getTotalSizeOfFilesInDir(new File(args[0]));
    final long end = System.nanoTime();
    System.out.println("Total Size: " + total);
    System.out.println("Time taken: " + (end - start)/1.0e9);
  }
}
