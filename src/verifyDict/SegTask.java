package verifyDict;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 * Created by DELL on 2015/7/30.
 */
public class SegTask implements Runnable
{
    private final PageReader pageReader;

    private final PageWriter pageWriter;

    private static AtomicLong atomicLong= new AtomicLong(0L);;

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
            Map<Long, String> map=new TreeMap<Long, String>();
            for (int i = 0; i < lines.length; i++) {
                if (lines[i]!=null&&lines[i]!="") {
                    atomicLong.addAndGet(1);
                    map.put(atomicLong.get(), lines[i]);
                }
            }
            pageWriter.writePage(map);
        }

    }
}
