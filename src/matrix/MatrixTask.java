package matrix;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixTask implements Runnable {
	
	private static final AtomicInteger counter = new AtomicInteger();
	
	private static final int FOUNDED_WORDS_BUF_SIZE = 1024;
	
	private final PageReader in;
	private final String[] words;
	private final Map<String, Map<String, AtomicInteger>> out;

	public MatrixTask(
			PageReader reader,
			String[] inputWords,
			Map<String, Map<String, AtomicInteger>> output
	) {
		in = reader;
		words = inputWords;
		out = output;
	}

	public void run() {
		String[] founded = new String[FOUNDED_WORDS_BUF_SIZE];
		int wordsFound;
		String[] lines;
		while ((lines = in.readPage()) != null) {
			for (String line : lines) {
				wordsFound = 0;
				for (String word : words) {
					// String.contains() -> 主要性能瓶颈，但是似乎没有更快的方法了
					if (line.contains(word)) founded[wordsFound++] = word;
				}
				for (int ptr1 = 0; ptr1 < wordsFound - 1; ptr1++) {
					for (int ptr2 = ptr1 + 1; ptr2 < wordsFound; ptr2++) {
						// counter -> 主要内存瓶颈（对于测试数据，约消耗300M内存）
						out	.computeIfAbsent(founded[ptr1], e -> new ConcurrentHashMap<>())
							.computeIfAbsent(founded[ptr2], e -> new AtomicInteger())
							.incrementAndGet();
					}
				}
				
				
				// 每4k行左右报告一次进度
				int count = counter.incrementAndGet();
				if ((count & 0xFFF) == 0)
					echo(count + " lines compared");
			}
		}
	}
	
	// t0：运行开始时间
	private static final long t0 = System.currentTimeMillis();
	// t1：上次调用echo的时间
	private static long t1 = t0;
	public synchronized static void echo(String msg) {
		long now = System.currentTimeMillis();
		System.out.println("time " + (now - t0) + " (+" + (now - t1) + "): " + msg);
		t1 = now;
	}
	
}
