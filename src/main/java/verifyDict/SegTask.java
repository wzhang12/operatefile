package verifyDict;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by DELL on 2015/7/30.
 */
public class SegTask implements Runnable
{
    private final PageReader pageReader;

    private final PageWriter pageWriter;

    private static AtomicInteger atomicInteger = new AtomicInteger();;

    public SegTask(PageReader pageReader, PageWriter pageWriter)
    {
        this.pageReader = pageReader;
        this.pageWriter = pageWriter;
    }

    @Override
    public void run()
    {
        String[] lines = null;
        while ((lines = pageReader.readPage()) != null) {
            Map<Integer, String> map = new TreeMap<Integer, String>();
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] != null && lines[i] != "") {
                    //有待改进
                    synchronized (this) {
                        atomicInteger.addAndGet(1);
                        map.put(atomicInteger.get(), lines[i]);
                    }
                    
                }
            }
            pageWriter.writePage(map);
        }

    }
}
