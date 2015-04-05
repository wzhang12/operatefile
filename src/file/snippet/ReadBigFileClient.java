package file.snippet;

import java.io.File;

public class ReadBigFileClient {
	public static void main(String[] args) {
		File file = new File("D://document.txt");
		BigFileHandler searchThread = new BigFileHandler(file, "aa", 0,
				Integer.MAX_VALUE);
		Thread thread = new Thread(searchThread);
		thread.start();
	
		/*
		 * String str="我去123"; System.out.print(str.indexOf("我去"));
		 */
	}
}
