package file.snippet;

import java.io.*;
import java.nio.ByteBuffer;
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
    private int end;
	public BigFileHandler(File file, String keyWord, int start, int end) {
		try {
			this.end=end;
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

	public byte[] cutOutByte(byte[] b, int j) {
		if (b.length == 0 || j == 0) {
			return null;
		}
		byte[] cut = new byte[j];
		for (int i = 0; i < j; i++) {
			cut[i] = b[i];
		}
		return cut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		long begintime = System.currentTimeMillis();
		// 找到最后是\n的位置
		final int fixvolumn = 4096;
		int last = 0;
		while(end-last>=fixvolumn) {
			int volumn = fixvolumn;
			byte[] buffer = new byte[fixvolumn];
			ByteBuffer byteBuffer = mbBuf.get(buffer);
			while (byteBuffer.get(volumn - 1) != '\n') {
				volumn--;
			}
			last += volumn;
			mbBuf.position(last + 1);
			byte[] cutBuffer = cutOutByte(buffer, volumn);
			countKeyword(cutBuffer);
		}
		if(end-last<fixvolumn){
			byte[] buffer=new byte[end-last-3];
			ByteBuffer byteBuffer = mbBuf.get(buffer);
			countKeyword(buffer);
		}
		System.out.println(index);
		long endteime = System.currentTimeMillis();
		System.out.println((begintime-endteime)/1000+"s");
	}
       
	/**
	 * @param cutBuffer
	 */
	private void countKeyword(byte[] cutBuffer) {
		String content = "";
		try {
			content = new String(cutBuffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringTokenizer token = new StringTokenizer(content, "\n");
		while (token.hasMoreTokens()) {
			String str = token.nextToken();
			String[] columns = str.split("\t");
			if (columns[0].indexOf(keyWord) != -1) {
				index++;
			}
		}
	}

}
/*
 * byte[] buffer = new byte[1024]; mbBuf.get(buffer); String content = ""; try {
 * content = new String(buffer, "UTF-8"); System.out.println(content); } catch
 * (UnsupportedEncodingException e) { e.printStackTrace(); } StringTokenizer
 * token = new StringTokenizer(content, "\n"); while (token.hasMoreTokens()) {
 * String str = token.nextToken(); String[] columns = str.split("\t"); if
 * (columns[0].indexOf(keyWord) != -1) { index++; } } System.out.print(index); }
 */