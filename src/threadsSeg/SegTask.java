package threadsSeg;

import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

/**
 * Created by DELL on 2015/7/30.
 */
public class SegTask implements Runnable {
	private final PageReader pageReader;
	private final PageWriter pageWriter;
	private String[] stops;

	public SegTask(PageReader pageReader, PageWriter pageWriter) {
		this.pageReader = pageReader;
		this.pageWriter = pageWriter;
	}

	public SegTask(PageReader pageReader, PageWriter pageWriter, String[] stops) {
		this.pageReader = pageReader;
		this.pageWriter = pageWriter;
		this.stops = stops;
	}

	@Override
	public void run() {
		String[] lines = null;
		JiebaSegmenter segementer = new JiebaSegmenter();
		while ((lines = pageReader.readPage()) != null) {
			String[] outLines = new String[lines.length];
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < lines.length; i++) {
				List<SegToken> tokens = segementer.process(lines[i],
						SegMode.SEARCH);
				for (SegToken term : tokens) {
					boolean flag = true;
					if (stops != null && stops.length != 0) {
						for (String stop : stops) {
							if (term.word.equals(stop)) {
								flag = false;
								break;
							}
						}
					}
					if (flag) {
						sb.append(term.word).append(" ");
					}
				}
				outLines[i] = sb.toString();
				sb = new StringBuilder();
			}
			pageWriter.writePage(outLines);
		}
	}
}
