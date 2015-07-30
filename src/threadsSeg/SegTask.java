package threadsSeg;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.List;

/**
 * Created by DELL on 2015/7/30.
 */
public class SegTask implements Runnable {
    private final PageReader pageReader;
    private final PageWriter pageWriter;

    public SegTask(PageReader pageReader,PageWriter pageWriter) {
        this.pageReader=pageReader;
        this.pageWriter=pageWriter;
    }

    @Override
    public void run() {
        String[] lines = null;
        while ((lines = pageReader.readPage()) != null) {
            String[] outLines = new String[lines.length];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                List<Term> parse = NlpAnalysis.parse(lines[i]);
                for (Term term : parse) {
                    sb.append(term.toString()).append(" ");
                }
                outLines[i] = sb.toString();
                sb = new StringBuilder();
            }
            pageWriter.writePage(outLines);
            System.out.println(outLines);
        }
    }
}
