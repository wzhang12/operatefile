package verifyDict;

import java.io.*;
import java.util.Map;

public class PageWriter {

	private DataOutputStream dataOutputStream;
	public PageWriter(String filePath) {
		try {
		    dataOutputStream = new DataOutputStream(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 *  读取“一页”数据，每页大小永远等于pageSize。剩余可读数据不足一页的，后面用空字符串补足
	 */
	public synchronized void writePage(Map<Long, String> map) {
		

		try {
				
		    for (Map.Entry<Long, String> me : map.entrySet()) {
		        dataOutputStream.writeLong(me.getKey().longValue());
                dataOutputStream.writeUTF(me.getValue());
            }
        
		}
		catch (Exception e) {
			e.printStackTrace(); // 出错时强制终止程序
			System.exit(1);
		}

	}

	public DataOutputStream getOut() {
		return dataOutputStream;
	}
	
	
}
