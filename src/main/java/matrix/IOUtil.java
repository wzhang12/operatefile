package matrix;

import java.io.*;
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

}
