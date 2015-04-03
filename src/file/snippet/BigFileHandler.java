package file.snippet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.StringTokenizer;

public class BigFileHandler implements Runnable {
	private long index;
	private FileChannel fileChannel;
	private FileLock fileLock;
	private MappedByteBuffer mbBuf;
	private String keyWord;

	public BigFileHandler(File file, String keyWord, long start, long end) {
		try {
			this.keyWord = keyWord;
			// 得到当前文件的通道
			fileChannel = new RandomAccessFile(file, "rw").getChannel();
			// 锁定当前文件的部分
			fileLock = fileChannel.lock(start, end, false);
			// 对当前文件片段建立内存映射，如果文件过大需要切割成多个片段
			mbBuf = fileChannel.map(FileChannel.MapMode.READ_ONLY, start, end);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		String content = "";
		StringTokenizer token = new StringTokenizer(content, "\n");
		while (token.hasMoreTokens()) {
			String str = token.nextToken();
			String[] columns = str.split("\t");
			if (columns[3].indexOf(keyWord) != -1) {
				index++;
			}
		}
		System.out.print(index);
	}
}
