package verifyDict;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TestMain
{

    public static void main(String[] args) throws Exception
    {       int[] a=MainWindow.generateRandomNumberArray(20, 30);
            for (int i = 0; i < a.length; i++) {
                System.out.println(a[i]);
            }
            String str="";
            if(str!=""){
                System.out.println("ccc");
            }
            IOUtil.getFolderFilesPath(new File("C:\\Users\\DELL\\Desktop\\format"));
    }
    
}
