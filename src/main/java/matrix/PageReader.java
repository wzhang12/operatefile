package matrix;

import java.io.*;

public class PageReader {
	
	private BufferedReader in;
	private final int ps;
	
	public PageReader(String filePath, int pageSize) {
		try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
        } catch (UnsupportedEncodingException |FileNotFoundException e) {
            e.printStackTrace();
        }
		this.ps = pageSize;
	}
	
	/**
	 *  读取“一页”数据，每页大小永远等于pageSize。剩余可读数据不足一页的，后面用空字符串补足
	 */
	public synchronized String[] readPage() {
		
		String[] page = new String[ps];
		int ptr = 0;
		
		String ln;
		try {
			while (ptr < ps && (ln = in.readLine()) != null)
				page[ptr++] = ln;
		}
		catch (IOException e) {
			e.printStackTrace(); // 出错时强制终止程序
			System.exit(1);
		}
		
		if (ptr == 0) return null; // 没有数据了
		
		int padding = ps - ptr;
		if (padding > 0)
			for (int i=0; i<padding; i++) page[ptr++] = "";
		
		return page;
	}
	
}
