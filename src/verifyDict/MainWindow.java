package verifyDict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainWindow
{
    private static Text dictContent;

    private static Text dictName;

    private static Text item;

    private static Text console;

    private static Label lblNewLabel_1;

    private static Text sourceFilePath;

    private static Text dictFolder;

    private static Text itemnum;

    private static int[] randoms = generateRandomNumberArray(100, 100000);

    private static int currentItemNum = 0;// 数现在进行到第几条

    // 每次点开始的时候需要重置dataInputStream
    private static RandomAccessFile randomAccessFile = null;

    // key是词语，String是词语所在文件的路径
    private static HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        Display display = Display.getDefault();
        final Shell main = new Shell();
        main.setSize(778, 710);
        main.setText("VerifyDict");

        dictContent = new Text(main, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
        dictContent.setBounds(10, 92, 108, 491);

        Label lblNewLabel = new Label(main, SWT.NONE);
        lblNewLabel.setBounds(10, 38, 36, 17);
        lblNewLabel.setText("词库：");

        dictName = new Text(main, SWT.BORDER);
        dictName.setEditable(false);
        dictName.setBounds(10, 61, 80, 25);

        item = new Text(main, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
        item.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL));
        item.setBounds(126, 38, 470, 376);

        console = new Text(main, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
        console.setBounds(124, 443, 472, 139);

        lblNewLabel_1 = new Label(main, SWT.NONE);
        lblNewLabel_1.setBounds(124, 420, 61, 17);
        lblNewLabel_1.setText("console");

        Button btnNewButton = new Button(main, SWT.NONE);
        btnNewButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
                dictContent.setText("");
                dictName.setText("");
            }
        });
        btnNewButton.setBounds(10, 589, 53, 27);
        btnNewButton.setText("reset");

        Button btnNewButton_1 = new Button(main, SWT.NONE);
        btnNewButton_1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
                String dicts = dictContent.getText();
                try {
                    IOUtil.writeLines(dictName.getText(), dicts);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(65, 589, 53, 27);
        btnNewButton_1.setText("save");

        sourceFilePath = new Text(main, SWT.BORDER);
        sourceFilePath.setBounds(602, 70, 108, 23);

        Button sourceFilePathButton = new Button(main, SWT.NONE);
        sourceFilePathButton.addMouseListener(new MouseAdapter()
        {
            String sourcePath;

            @Override
            public void mouseUp(MouseEvent e)
            {
                FileDialog fileDialog = new FileDialog(main);
                fileDialog.setText("chooser");
                fileDialog.open();
                sourceFilePath.setText(fileDialog.getFilterPath() + File.separator + fileDialog.getFileName());

                if (!sourceFilePath.getText().equals(sourcePath)) {
                    try {
                        if (randomAccessFile != null) {
                            randomAccessFile.close();
                        }
                    } catch (IOException e2) {
                        System.err.println("关闭读入流发生异常");
                    }
                    sourcePath = sourceFilePath.getText();
                    String dest = sourcePath.substring(0, sourcePath.lastIndexOf(".")) + ".tmp";
                    try {
                        randomAccessFile = new RandomAccessFile(dest, "r");
                    } catch (FileNotFoundException e1) {
                        System.err.println("找不到带索引的文件");
                    }
                    generateIndexFile(sourcePath, dest);
                }

            }
        });
        sourceFilePathButton.setBounds(716, 68, 36, 27);
        sourceFilePathButton.setText("...");

        dictFolder = new Text(main, SWT.BORDER);
        dictFolder.setBounds(602, 122, 108, 23);

        Button dictFolderButton = new Button(main, SWT.NONE);
        dictFolderButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
                DirectoryDialog fileDialog = new DirectoryDialog(main);
                fileDialog.setText("chooser");
                fileDialog.open();
                dictFolder.setText(fileDialog.getFilterPath());
                ArrayList<String> arrayList = IOUtil.getFolderFilesPath(new File(dictFolder.getText()));
                for (String path : arrayList) {
                    try {
                        String[] words = IOUtil.readLines(path);
                        for (int i = 0; i < words.length; i++) {
                            ArrayList<String> list;
                            if ((list = hashMap.get(words[i])) != null) {
                                list.add(path);
                                hashMap.put(words[i], list);
                            } else {
                                list = new ArrayList<String>();
                                list.add(path);
                                hashMap.put(words[i], list);
                            }
                        }
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });
        dictFolderButton.setText("...");
        dictFolderButton.setBounds(716, 120, 36, 27);

        itemnum = new Text(main, SWT.BORDER);
        itemnum.setBounds(602, 174, 45, 23);

        Label label = new Label(main, SWT.NONE);
        label.setBounds(602, 46, 108, 17);
        label.setText("选择文件源文件：");

        Label label_1 = new Label(main, SWT.NONE);
        label_1.setText("选择词库文件夹：");
        label_1.setBounds(602, 99, 108, 17);

        Label label_2 = new Label(main, SWT.NONE);
        label_2.setText("输入需要标记的条数：");
        label_2.setBounds(602, 151, 125, 17);

        Button start = new Button(main, SWT.NONE);

        start.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
            }
        });
        start.setBounds(653, 170, 99, 27);
        start.setText("开始");

        ProgressBar progressBar = new ProgressBar(main, SWT.NONE);
        progressBar.setBounds(602, 203, 150, 37);

        Button previous = new Button(main, SWT.NONE);
        previous.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
                String itemContent = "";
                try {
                    currentItemNum--;
                    if (currentItemNum < 1) {
                        currentItemNum = 1;
                    }
                    itemContent = readIndexFile(randoms[currentItemNum - 1]);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    System.err.println("发生错误");
                }
                item.setText(itemContent);
            }
        });
        previous.setText("后退");
        previous.setBounds(602, 246, 150, 27);

        Button next = new Button(main, SWT.NONE);
        next.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseUp(MouseEvent e)
            {
                item.setText("");
                String itemContent = "";
                try {
                    currentItemNum++;
                    if (currentItemNum > randoms.length) {
                        currentItemNum = randoms.length;
                    }
                    itemContent = readIndexFile(randoms[currentItemNum - 1]);
                    Set<String> set = hashMap.keySet();
                    for (String word : set) {
                        Pattern pattern = Pattern.compile(word.trim());
                        Matcher matcher = pattern.matcher(itemContent);
                        if (matcher.find()) {
                            
                        }
                        
                        Font font = item.getFont();
                        item.setFont(SWTResourceManager.getFont("微软雅黑", 13, SWT.NORMAL | SWT.COLOR_RED));
                        item.append(word);
                        ArrayList<String> pathes = hashMap.get(word);
                        item.append("(");
                        for (String path : pathes) {
                             item.append(path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."))+"|");
                        }
                        item.append(")");
                        item.setFont(font);
                    }
                } catch (Exception e1) {
                    System.err.println("发生错误");
                }
            }
        });
        next.setText("前进");
        next.setBounds(602, 282, 150, 27);

        Button btnNewButton_2 = new Button(main, SWT.NONE);
        btnNewButton_2.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
                MyPrintStream mps = new MyPrintStream(System.out, dictContent);
                System.setErr(mps);
                System.setOut(mps);
                try {
                    FileDialog fileDialog = new FileDialog(main);
                    fileDialog.setText("chooser");
                    fileDialog.open();
                    dictName.setText(fileDialog.getFilterPath() + File.separator + fileDialog.getFileName());
                    String[] dicts = IOUtil.readLines(dictName.getText());
                    for (int i = 0; i < dicts.length; i++) {
                        System.out.println(dicts[i]);
                    }

                } catch (Exception e1) {
                    // TODO 能设置到console
                    mps.setText(console);
                    System.err.println("发生错误");
                    mps.setText(dictContent);
                }
            }
        });
        btnNewButton_2.setBounds(96, 58, 22, 29);
        btnNewButton_2.setText("...");

        Button btnNewButton_3 = new Button(main, SWT.NONE);
        btnNewButton_3.setBounds(516, 588, 80, 27);
        btnNewButton_3.setText("保存");

        Button fp = new Button(main, SWT.RADIO);
        fp.setBounds(126, 599, 69, 17);
        fp.setText("标记有错");

        Button fn = new Button(main, SWT.RADIO);
        fn.setText("标记有遗漏");
        fn.setBounds(198, 599, 80, 17);

        Button tp = new Button(main, SWT.RADIO);
        tp.setText("标记正确");
        tp.setBounds(284, 599, 69, 17);

        Button button_2 = new Button(main, SWT.RADIO);
        button_2.setText("其他");
        button_2.setBounds(359, 599, 97, 17);

        main.open();
        main.layout();
        while (!main.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    private static String readIndexFile(int rowNum) throws Exception
    {
        while (randomAccessFile.readLong() != rowNum) {
            randomAccessFile.skipBytes(randomAccessFile.readUnsignedShort());
        }
        String content = randomAccessFile.readUTF();
        randomAccessFile.seek(0);

        return content;

    }

    public static int[] generateRandomNumberArray(int size, int largest)
    {
        Random random = new Random();
        boolean[] bool = new boolean[largest];
        int[] nums = new int[size];
        int randInt = 0;
        for (int j = 0; j < size; j++) {
            do {

                randInt = random.nextInt(largest);
                nums[j] = randInt + 1;
            } while (bool[randInt]);
            bool[randInt] = true;
        }
        Arrays.sort(nums);
        return nums;
    }

    private static boolean isExists(int[] range, long l)
    {
        for (int i = 0; i < range.length; i++) {
            if (range[i] > l) {
                return false;
            }
            if (range[i] == l) {
                return true;
            }
        }
        return false;
    }

    private static void generateIndexFile(String sourcePath, String dest)
    {
        if (!new File(dest).exists()) {
            PageReader pageReader = new PageReader(sourcePath, 4000);
            PageWriter pageWriter = new PageWriter(dest);
            SegTask segTask = new SegTask(pageReader, pageWriter);
            Thread[] threads = new Thread[3];
            for (int i = 0; i < 3; i++) {
                Thread thread = new Thread(segTask);
                threads[i] = thread;
                thread.start();
            }
            while (true) {
                if (!threads[0].isAlive() && !threads[1].isAlive() && !threads[2].isAlive()) {
                    try {
                        if (pageWriter != null && pageWriter.getOut() != null) {
                            pageWriter.getOut().flush();
                            pageWriter.getOut().close();
                        }
                        if (pageReader.getIn() != null && pageReader != null) {
                            pageReader.getIn().close();
                        }
                    } catch (Exception en) {
                        en.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
