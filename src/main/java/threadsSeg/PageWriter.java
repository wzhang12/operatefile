package threadsSeg;

import java.io.*;

public class PageWriter {

	private BufferedWriter out;
	public PageWriter(String filePath) {
		try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
        } catch (UnsupportedEncodingException |FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 *  读取“一页”数据，每页大小永远等于pageSize。剩余可读数据不足一页的，后面用空字符串补足
	 */
	public synchronized void writePage(String[] lines) {
		

		try {
				
				for(int i=0;i<lines.length;i++){
					if(lines[i]!=null&&!lines[i].equals(""))
					out.write(lines[i]+"\n");
			}
		}
		catch (IOException e) {
			e.printStackTrace(); // 出错时强制终止程序
			System.exit(1);
		}

	}

	public BufferedWriter getOut() {
		return out;
	}
	
	
}
