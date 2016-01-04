package matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixGenerator {
	
	private static final String wordsFile  = "E:\\BaiduYunDownload\\frequent1.txt";
	private static final String linesFile  = "E:\\BaiduYunDownload\\脉动8.txt";
	private static final String resultFile = "E:\\BaiduYunDownload\\matrix2.txt";

	private static final int THREADS = 3;
	private static final int MIN_FREQUENCY = 2;
	private static final int TASK_SIZE = 4000;
	
	// 失败作
	// 尝试对Matrix2进行进一步改进，结果内存占用和运行速度都没有改善
	// 甚至好像还更差一点
	public static void main(String args[]) throws Exception {
		
		long t1 = System.currentTimeMillis();
		
		String[] 
			words = IOUtil.readLines(wordsFile);
		PageReader 
			reader = new PageReader(linesFile, TASK_SIZE);
		Map<String, Map<String, AtomicInteger>> 
			counter = new ConcurrentHashMap<>();

		Thread[] threads = new Thread[THREADS];
		for (int i = 0; i < THREADS; i++) {
			Thread t = new Thread(new MatrixTask(reader, words, counter));
			threads[i] = t;
			t.start();
		}
		for (int i = 0; i < THREADS; i++) {
			threads[i].join();
		}
		
		List<MatrixCell> result = new ArrayList<>();
		for (Map.Entry<String, Map<String, AtomicInteger>> row : counter.entrySet()) {
			String rowNo = row.getKey();
			Map<String, AtomicInteger> rowData = row.getValue();
			for (Map.Entry<String, AtomicInteger> cell : rowData.entrySet()) {
				String colNo = cell.getKey();
				int data = cell.getValue().get();
				if (data > MIN_FREQUENCY)
					result.add(new MatrixCell(rowNo, colNo, data));
			}
		}
		
		Collections.sort(result);
		IOUtil.writeLines(resultFile, result);
		
		long t2 = System.currentTimeMillis();
		System.out.println("done in " + (t2 - t1) + " ms");
	}
	
}
