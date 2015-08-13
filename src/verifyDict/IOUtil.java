package verifyDict;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
	
	// 按行读取文件为String数组中
	public static String[] readLines(String filePath) throws Exception {
		List<String> li = new ArrayList<>();
		try (
			BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(filePath),
                "UTF-8"));
		) {
			String ln;
			while ((ln = in.readLine()) != null) li.add(ln);
		}
	
		return li.toArray(new String[li.size()]);
	}
	
	// 按行写入List至文件中
	public static void writeLines(String filePath, List<?> lines) throws Exception {
		try (
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
		) {
			for (Object obj : lines) {
				out.write(obj.toString());
				out.write("\n");
			}
		}
	}
	public static void writeLines(String filePath, String line) throws Exception {
        try (
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
        ) {
            out.write(line);
        }
    }
	   public static ArrayList<String> getFolderFilesPath(File f){
	         ArrayList<String> arrayList =new ArrayList<String>();
	        //判断传入对象是否为一个文件夹对象
	        if(!f.isDirectory()){
	            System.err.println("你输入的不是一个文件夹，请检查路径是否有误！！");
	        }
	        else{
	            File[] t = f.listFiles();
	            for(int i=0;i<t.length;i++){
	                //判断文件列表中的对象是否为文件夹对象，如果是则执行递归，直到把此文件夹中所有文件输出为止
	                if(t[i].isDirectory()){
	                    getFolderFilesPath(t[i]);
	                }
	                else{
	                    arrayList.add(t[i].getAbsolutePath());
	                }
	            }
	        }
	        return arrayList;
	 
	    }
	
}
