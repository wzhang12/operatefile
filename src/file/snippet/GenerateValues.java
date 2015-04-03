package file.snippet;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class GenerateValues {

	public static void main(String[] args) throws IOException {
           BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\document.txt",true)));
           
           for (long i = 0; i < 100000000; i++) {
			String str="aaa\t张稳\t123\t我去\r\n";
			String str1="bbb\t张呵呵\t346\t呵呵\r\n";
			bw.write(str);
			bw.write(str1);
		}   
           bw.flush();
           bw.close();
           System.out.print("Ok");
           
	}

}
