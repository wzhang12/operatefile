//package verifyDict;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Random;
//import java.util.Set;
//
//import org.eclipse.jface.dialogs.MessageDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.browser.Browser;
//import org.eclipse.swt.browser.LocationAdapter;
//import org.eclipse.swt.browser.LocationEvent;
//import org.eclipse.swt.events.MouseAdapter;
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.DirectoryDialog;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.FileDialog;
//import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.MessageBox;
//import org.eclipse.swt.widgets.ProgressBar;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.swt.events.FocusAdapter;
//import org.eclipse.swt.events.FocusEvent;
//
//public class MainWindow
//{
//    private static Text dictContent;
//
//    private static Text dictName;
//
//    private static Text console;
//
//    private static Label lblNewLabel_1;
//
//    private static Text sourceFilePath;
//
//    private static Text dictFolder;
//
//    // 输入的记录数
//    private static Text itemnum;
//
//    private static int[] randoms;
//
//    private static int currentItemNum = 0;// 数现在进行到第几条
//
//    // 每次点开始的时候需要重置dataInputStream
//    private static RandomAccessFile randomAccessFile = null;
//
//    // key是词语，String是词语所在文件的路径
//    private static HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
//
//    private static Browser item;
//
//    private static int pagenum;
//
//    // fp加起来被选中了多少次
//    private static int fpTotal;
//
//    // tp加起来 被选中了多少次
//    private static int tpTotal;
//
//    // fn加起来 被选中了多少次
//    private static int fnTotal;
//
//    private static double precision;
//
//    private static double recall;
//
//    // 用来记录用户的选择 1是标记有错 2是标记有遗漏 3标记有正确 4其他 0是没有选择
//    private static int[] records;
//
//    private static Label progressComment;
//
//    private static Button fp;
//
//    private static Button fn;
//
//    private static Button tp;
//
//    private static Button spares;
//
//    private static Text resultPath;
//
//    private static Button previous;
//
//    private static Button next;
//
//    /**
//     * Launch the application.
//     *
//     * @param args
//     */
//    public static void main(String[] args)
//    {
//
//        Display display = Display.getDefault();
//        final Shell main = new Shell();
//        main.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseUp(MouseEvent e) {
//                main.forceFocus();
//            }
//        });
//        main.setSize(778, 710);
//        main.setText("VerifyDict");
//
//        dictContent = new Text(main, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
//        dictContent.setBounds(10, 92, 108, 491);
//
//        Label lblNewLabel = new Label(main, SWT.NONE);
//        lblNewLabel.setBounds(10, 38, 36, 17);
//        lblNewLabel.setText("词库：");
//
//        dictName = new Text(main, SWT.BORDER);
//        dictName.setEditable(false);
//        dictName.setBounds(10, 61, 80, 25);
//
//        console = new Text(main, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
//        console.setBounds(124, 443, 472, 139);
//
//        lblNewLabel_1 = new Label(main, SWT.NONE);
//        lblNewLabel_1.setBounds(124, 420, 66, 17);
//        lblNewLabel_1.setText("CONSOLE");
//
//        Button btnNewButton = new Button(main, SWT.NONE);
//        btnNewButton.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                dictContent.setText("");
//                dictName.setText("");
//            }
//        });
//        btnNewButton.setBounds(10, 589, 53, 27);
//        btnNewButton.setText("reset");
//
//        Button btnNewButton_1 = new Button(main, SWT.NONE);
//        btnNewButton_1.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                String dicts = dictContent.getText();
//                try {
//                    IOUtil.writeLines(dictName.getText(), dicts);
//                } catch (Exception e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//            }
//        });
//        btnNewButton_1.setBounds(65, 589, 53, 27);
//        btnNewButton_1.setText("save");
//
//        sourceFilePath = new Text(main, SWT.BORDER);
//        sourceFilePath.setBounds(602, 70, 108, 23);
//
//        Button sourceFilePathButton = new Button(main, SWT.NONE);
//        sourceFilePathButton.addMouseListener(new MouseAdapter()
//        {
//            String sourcePath;
//
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                FileDialog fileDialog = new FileDialog(main);
//                fileDialog.setText("chooser");
//                fileDialog.open();
//                sourceFilePath.setText(fileDialog.getFilterPath() + File.separator + fileDialog.getFileName());
//
//                if (!sourceFilePath.getText().equals(sourcePath)) {
//                    try {
//                        if (randomAccessFile != null) {
//                            randomAccessFile.close();
//                        }
//                    } catch (IOException e2) {
//                        System.err.println("关闭读入流发生异常");
//                    }
//                    sourcePath = sourceFilePath.getText();
//                    if (sourcePath.lastIndexOf(".") == -1)
//                        return;
//                    String dest = sourcePath.substring(0, sourcePath.lastIndexOf(".")) + ".tmp";
//                    generateIndexFile(sourcePath, dest);
//                    try {
//                        randomAccessFile = new RandomAccessFile(dest, "r");
//                        // randomAccessFile.seek(new File(dest).length());
//                    } catch (Exception e1) {
//                        System.err.println("找不到带索引的文件");
//                    }
//                }
//
//            }
//        });
//        sourceFilePathButton.setBounds(716, 68, 36, 27);
//        sourceFilePathButton.setText("...");
//
//        dictFolder = new Text(main, SWT.BORDER);
//        dictFolder.setBounds(602, 122, 108, 23);
//
//        Button dictFolderButton = new Button(main, SWT.NONE);
//        dictFolderButton.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                DirectoryDialog fileDialog = new DirectoryDialog(main);
//                fileDialog.setText("chooser");
//                fileDialog.open();
//                dictFolder.setText(fileDialog.getFilterPath());
//                ArrayList<String> arrayList = IOUtil.getFolderFilesPath(new File(dictFolder.getText()));
//                for (String path : arrayList) {
//                    try {
//                        String[] words = IOUtil.readLines(path);
//                        for (int i = 0; i < words.length; i++) {
//                            ArrayList<String> list;
//                            if ((list = hashMap.get(words[i])) != null) {
//                                list.add(path);
//                                hashMap.put(words[i], list);
//                            } else {
//                                list = new ArrayList<String>();
//                                list.add(path);
//                                hashMap.put(words[i], list);
//                            }
//                        }
//                    } catch (Exception e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        });
//        dictFolderButton.setText("...");
//        dictFolderButton.setBounds(716, 120, 36, 27);
//
//        itemnum = new Text(main, SWT.BORDER);
//        itemnum.setBounds(602, 174, 45, 23);
//
//        Label label = new Label(main, SWT.NONE);
//        label.setBounds(602, 46, 108, 17);
//        label.setText("选择文件源文件：");
//
//        Label label_1 = new Label(main, SWT.NONE);
//        label_1.setText("选择词库文件夹：");
//        label_1.setBounds(602, 99, 108, 17);
//
//        Label label_2 = new Label(main, SWT.NONE);
//        label_2.setText("输入需要标记的条数：");
//        label_2.setBounds(602, 151, 150, 17);
//        final ProgressBar progressBar = new ProgressBar(main, SWT.NONE);
//        progressBar.setBounds(602, 227, 150, 37);
//        Button start = new Button(main, SWT.NONE);
//        start.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                main.forceFocus();
//            }
//        });
//
//        start.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                MessageBox box = new MessageBox(main, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
//                // 设置对话框的标题
//                box.setText("提醒");
//                // 设置对话框显示的消息
//                box.setMessage("确定要开始或重新开始吗？");
//                // 打开对话框，将返回值赋给choice
//                int choice = box.open();
//                if (choice == SWT.NO)
//                    return;
//                String sourcePath=sourceFilePath.getText();
//                String dest = sourcePath.substring(0, sourcePath.lastIndexOf(".")) + ".tmp";
//                RandomAccessFile rf = null;
//                try {
//                    rf = new RandomAccessFile(dest, "r");
//                    long len = rf.length();
//                    rf.seek(len-4);
//                    pagenum=rf.readInt()-1;
//                } catch (Exception e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//
//                currentItemNum = 0;
//                randoms = generateRandomNumberArray(Integer.valueOf(itemnum.getText().trim()), pagenum);
//                records = new int[Integer.valueOf(itemnum.getText().trim())];
//                progressBar.setMaximum(Integer.valueOf(itemnum.getText().trim()));
//                records = new int[Integer.valueOf(itemnum.getText().trim())];
//                progressBar.setSelection(0);
//                progressComment.setText("0/" + Integer.valueOf(itemnum.getText().trim()));
//                item.setText("");
//
//            }
//        });
//        start.setBounds(653, 170, 99, 27);
//        start.setText("开始");
//
//        previous = new Button(main, SWT.NONE);
//        previous.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                String itemContent = "";
//                try {
//                    currentItemNum--;
//                    if (currentItemNum < 1) {
//                        currentItemNum = 1;
//                    }
//                    itemContent = readIndexFile(randoms[currentItemNum - 1]);
//                    System.out.println(randoms[randoms.length - 1]);
//                    System.out.println(randoms.length);
//                    System.out.println(randoms[currentItemNum - 1] + itemContent);
//                    Set<String> set = hashMap.keySet();
//                    StringBuilder itemContentBuilder = new StringBuilder();
//                    int index = 0;
//                    for (String regx : set) {
//
//                        String word = RegxUtil.findFirstMatchedWords(regx, itemContent);
//                        if (word != null) {
//                            String[] strs = RegxUtil.findFirstMatchedWordsAndIndex(regx, itemContent);
//                            if (index < Integer.valueOf(strs[1])) {
//                                itemContentBuilder.append(itemContent.substring(index, Integer.valueOf(strs[1])
//                                    + strs[0].length()));
//                                index = Integer.valueOf(strs[1]) + strs[0].length();
//                            }
//                            itemContentBuilder.append("<font color=\"#F00000\">" + word + "(" + regx + ")" + "</font>");
//                            ArrayList<String> pathes = hashMap.get(word);
//                            StringBuffer sBuffer = new StringBuffer();
//                            sBuffer.append("<font color=\"#F00000\">(");
//                            for (String path : pathes) {
//                                sBuffer.append("<a href=" + path + ">");
//                                sBuffer.append(path.substring(path.lastIndexOf(File.separator) + 1,
//                                    path.lastIndexOf("."))
//                                    + "</a>|");
//                            }
//                            sBuffer.append(")</font>");
//                            itemContentBuilder.append(sBuffer);
//
//                        }
//                    }
//                    if (index != itemContent.length()) {
//                        itemContentBuilder.append(itemContent.substring(index, itemContent.length()));
//                    }
//                    item.setText(itemContentBuilder.toString());
//                    if (records[currentItemNum - 1] == 1) {
//                        fp.setSelection(true);
//                        spares.setSelection(false);
//                        tp.setSelection(false);
//                        fn.setSelection(false);
//                    } else if (records[currentItemNum - 1] == 2) {
//                        fn.setSelection(true);
//                        spares.setSelection(false);
//                        tp.setSelection(false);
//                        fp.setSelection(false);
//                    } else if (records[currentItemNum - 1] == 3) {
//                        tp.setSelection(true);
//                        spares.setSelection(false);
//                        fn.setSelection(false);
//                        fp.setSelection(false);
//                    } else if (records[currentItemNum - 1] == 4) {
//                        spares.setSelection(true);
//                    } else {
//                        spares.setSelection(false);
//                        tp.setSelection(false);
//                        fn.setSelection(false);
//                        fp.setSelection(false);
//                    }
//                } catch (Exception e1) {
//                    // TODO Auto-generated catch block
//                    System.err.println("发生错误");
//                }
//                item.setText(itemContent);
//            }
//        });
//        previous.setText("后退");
//        previous.setBounds(602, 270, 150, 27);
//
//        next = new Button(main, SWT.NONE);
//
//        next.addMouseListener(new MouseAdapter()
//        {
//
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                item.setText("");
//                String itemContent = "";
//                try {
//                    currentItemNum++;
//                    if (currentItemNum > randoms.length) {
//                        currentItemNum = randoms.length;
//                    }
//                    progressBar.setSelection(currentItemNum);
//                    progressComment.setText(currentItemNum + "/" + Integer.valueOf(itemnum.getText().trim()));
//                    itemContent = readIndexFile(randoms[currentItemNum - 1]);
//                    System.out.println(randoms[randoms.length - 1]);
//                    System.out.println(randoms.length);
//                    System.out.println(randoms[currentItemNum - 1] + itemContent);
//                    Set<String> set = hashMap.keySet();
//                    StringBuilder itemContentBuilder = new StringBuilder();
//                    int index = 0;
//                    for (String regx : set) {
//
//                        String word = RegxUtil.findFirstMatchedWords(regx, itemContent);
//                        if (word != null) {
//                            String[] strs = RegxUtil.findFirstMatchedWordsAndIndex(regx, itemContent);
//                            if (index < Integer.valueOf(strs[1])) {
//                                itemContentBuilder.append(itemContent.substring(index, Integer.valueOf(strs[1])
//                                    + strs[0].length()));
//                                index = Integer.valueOf(strs[1]) + strs[0].length();
//                            }
//                            itemContentBuilder.append("<font color=\"#F00000\">" + word + "(" + regx + ")" + "</font>");
//                            ArrayList<String> pathes = hashMap.get(word);
//                            StringBuffer sBuffer = new StringBuffer();
//                            sBuffer.append("<font color=\"#F00000\">(");
//                            for (String path : pathes) {
//                                sBuffer.append("<a href=" + path + ">");
//                                sBuffer.append(path.substring(path.lastIndexOf(File.separator) + 1,
//                                    path.lastIndexOf("."))
//                                    + "</a>|");
//                            }
//                            sBuffer.append(")</font>");
//                            itemContentBuilder.append(sBuffer);
//
//                        }
//                    }
//                    if (index != itemContent.length()) {
//                        itemContentBuilder.append(itemContent.substring(index, itemContent.length()));
//                    }
//                    item.setText(itemContentBuilder.toString());
//                    if (records[currentItemNum - 1] == 1) {
//                        fp.setSelection(true);
//                        spares.setSelection(false);
//                        tp.setSelection(false);
//                        fn.setSelection(false);
//                    } else if (records[currentItemNum - 1] == 2) {
//                        fn.setSelection(true);
//                        spares.setSelection(false);
//                        tp.setSelection(false);
//                        fp.setSelection(false);
//                    } else if (records[currentItemNum - 1] == 3) {
//                        tp.setSelection(true);
//                        spares.setSelection(false);
//                        fn.setSelection(false);
//                        fp.setSelection(false);
//                    } else if (records[currentItemNum - 1] == 4) {
//                        spares.setSelection(true);
//                    } else {
//                        spares.setSelection(false);
//                        tp.setSelection(false);
//                        fn.setSelection(false);
//                        fp.setSelection(false);
//                    }
//
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//            }
//        });
//        next.setText("前进");
//        next.setBounds(602, 303, 150, 27);
//
//        Button btnNewButton_2 = new Button(main, SWT.NONE);
//        btnNewButton_2.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                MyPrintStream mps = new MyPrintStream(System.out, dictContent);
//                System.setErr(mps);
//                System.setOut(mps);
//                try {
//                    FileDialog fileDialog = new FileDialog(main);
//                    fileDialog.setText("chooser");
//                    fileDialog.open();
//                    dictName.setText(fileDialog.getFilterPath() + File.separator + fileDialog.getFileName());
//                    String[] dicts = IOUtil.readLines(dictName.getText());
//                    for (int i = 0; i < dicts.length; i++) {
//                        System.out.println(dicts[i]);
//                    }
//
//                } catch (Exception e1) {
//                    // TODO 能设置到console
//                    mps.setText(console);
//                    System.err.println("发生错误");
//                    mps.setText(dictContent);
//                }
//            }
//        });
//        btnNewButton_2.setBounds(96, 58, 22, 29);
//        btnNewButton_2.setText("...");
//
//        Button btnNewButton_3 = new Button(main, SWT.NONE);
//        btnNewButton_3.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                if (resultPath.getText() == null || resultPath.getText().trim() == "") {
//                    MessageDialog.openInformation(main, "提示", "请输入所要保存的路径");
//                    return;
//                }
//                try {
//                    StringBuffer recordsBuffer = new StringBuffer();
//                    for (int i = 0; i < currentItemNum - 1; i++) {
//                        recordsBuffer.append(records[i] + ",");
//                    }
//                    StringBuffer randomsBuffer = new StringBuffer();
//                    for (int j = 0; j < currentItemNum - 1; j++) {
//                        randomsBuffer.append(randoms[j] + ",");
//                    }
//                    String line =
//                        new Date() + "\t" + "词典路径:" + dictFolder.getText() + "\t" + "源文件路径:" + sourceFilePath.getText()
//                            + "\t" + "标记的个数:" + currentItemNum + "\t" + "总个数:" + itemnum.getText() + "\t"
//                            + "precision:" + precision + "\t" + "recall:" + recall + "\t" + "标记详情:"
//                            + recordsBuffer.toString() + "\t" + "标记所对应的编号:" + randomsBuffer.toString();
//                    IOUtil.writeLineByAppend(resultPath.getText(), line);
//                } catch (Exception e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//
//            }
//        });
//        btnNewButton_3.setBounds(516, 594, 80, 27);
//        btnNewButton_3.setText("保存");
//
//        fp = new Button(main, SWT.RADIO);
//        fp.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                if (records[currentItemNum - 1] == 1) {
//
//                } else if (records[currentItemNum - 1] == 2) {
//                    fpTotal++;
//                    fnTotal--;
//                    records[currentItemNum - 1] = 1;
//                } else if (records[currentItemNum - 1] == 3) {
//                    fpTotal++;
//                    tpTotal--;
//                    records[currentItemNum - 1] = 1;
//                } else if (records[currentItemNum - 1] == 4) {
//
//                } else {
//                    records[currentItemNum - 1] = 1;
//                    fpTotal++;
//                }
//                Double precisionRaw = Double.valueOf(tpTotal) / (Double.valueOf(tpTotal) + Double.valueOf(fpTotal));
//                BigDecimal b = new BigDecimal(precisionRaw);
//                precision = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                console.setText("precision:" + precision + "\n" + "recall:" + recall);
//
//            }
//        });
//        fp.setBounds(126, 599, 80, 17);
//        fp.setText("标记有错");
//
//        fn = new Button(main, SWT.RADIO);
//        fn.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                if (records[currentItemNum - 1] == 1) {
//                    fpTotal--;
//                    fnTotal++;
//                    records[currentItemNum - 1] = 2;
//                } else if (records[currentItemNum - 1] == 2) {
//
//                } else if (records[currentItemNum - 1] == 3) {
//                    fnTotal++;
//                    tpTotal--;
//                    records[currentItemNum - 1] = 2;
//                } else if (records[currentItemNum - 1] == 4) {
//
//                } else {
//                    records[currentItemNum - 1] = 2;
//                    fnTotal++;
//                }
//                System.out.println(fnTotal + " " + tpTotal + " " + fpTotal);
//                Double recallRaw = Double.valueOf(tpTotal) / (Double.valueOf(tpTotal) + Double.valueOf(fnTotal));
//                BigDecimal b1 = new BigDecimal(recallRaw);
//                recall = b1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                console.setText("precision:" + precision + "\n" + "recall:" + recall);
//            }
//        });
//        fn.setText("标记有遗漏");
//        fn.setBounds(207, 599, 97, 17);
//
//        tp = new Button(main, SWT.RADIO);
//        tp.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                if (records[currentItemNum - 1] == 1) {
//                    fpTotal--;
//                    tpTotal++;
//                    records[currentItemNum - 1] = 3;
//                } else if (records[currentItemNum - 1] == 2) {
//                    fnTotal--;
//                    tpTotal++;
//                    records[currentItemNum - 1] = 3;
//                } else if (records[currentItemNum - 1] == 3) {
//
//                } else if (records[currentItemNum - 1] == 4) {
//
//                } else {
//                    records[currentItemNum - 1] = 3;
//                    tpTotal++;
//                }
//                Double precisionRaw = Double.valueOf(tpTotal) / (Double.valueOf(tpTotal) + Double.valueOf(fpTotal));
//                BigDecimal b2 = new BigDecimal(precisionRaw);
//                precision = b2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                Double recallRaw = Double.valueOf(tpTotal) / (Double.valueOf(tpTotal) + Double.valueOf(fnTotal));
//                BigDecimal b3 = new BigDecimal(recallRaw);
//                recall = b3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                console.setText("precision:" + precision + "\n" + "recall:" + recall);
//            }
//        });
//        tp.setText("标记正确");
//        tp.setBounds(310, 599, 86, 17);
//
//        spares = new Button(main, SWT.RADIO);
//        spares.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                records[currentItemNum - 1] = 4;
//            }
//        });
//        spares.setText("其他");
//        spares.setBounds(402, 599, 97, 17);
//
//        item = new Browser(main, SWT.NONE);
//        item.addLocationListener(new LocationAdapter()
//        {
//            @Override
//            public void changing(LocationEvent event)
//            {
//                String urlString = event.location;
//                if (!"about:blank".equals(urlString)) {
//                    item.stop();
//                    item.setText(item.getText());
//                    String[] words = null;
//                    String dictPath = urlString.substring("file:///".length(), urlString.length());
//                    try {
//                        words = IOUtil.readLines(dictPath);
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    dictContent.setText("");
//                    for (String word : words) {
//                        dictContent.append(word + "\n");
//                    }
//                    dictName.setText(dictPath);
//                }
//            }
//        });
//        item.setBounds(124, 61, 472, 348);
//
//        Button btnNewButton_4 = new Button(main, SWT.NONE);
//        btnNewButton_4.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                console.setText("");
//            }
//        });
//        btnNewButton_4.setBounds(472, 416, 124, 25);
//        btnNewButton_4.setText("reset console");
//
//        progressComment = new Label(main, SWT.NONE);
//        progressComment.setAlignment(SWT.RIGHT);
//        progressComment.setBounds(602, 203, 150, 20);
//        progressComment.setText("0/0");
//
//        resultPath = new Text(main, SWT.BORDER);
//        resultPath.setBounds(263, 417, 161, 23);
//
//        Button button = new Button(main, SWT.NONE);
//        button.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mouseUp(MouseEvent e)
//            {
//                FileDialog fileDialog = new FileDialog(main);
//                fileDialog.setText("chooser");
//                fileDialog.open();
//                resultPath.setText(fileDialog.getFilterPath() + File.separator + fileDialog.getFileName());
//            }
//        });
//        button.setText("...");
//        button.setBounds(430, 415, 36, 27);
//
//        Label lblNewLabel_2 = new Label(main, SWT.NONE);
//        lblNewLabel_2.setAlignment(SWT.RIGHT);
//        lblNewLabel_2.setBounds(196, 420, 61, 17);
//        lblNewLabel_2.setText("结果保存在");
//
//        //=========================
//        //快捷键
//        //=========================
//        Listener listener =new Listener(){
//
//            @Override
//            public void handleEvent(Event e)
//            {
//                System.out.println(e.keyCode);
//                if (main.isFocusControl()) {
//                    if (e.keyCode == 100) {
//                        e.widget = next;
//                        // 主动触发button点击事件
//                        next.notifyListeners(SWT.MouseUp, e);
//                    }
//                    if(e.keyCode==97){
//                        e.widget = previous;
//                        // 主动触发button点击事件
//                        previous.notifyListeners(SWT.MouseUp, e);
//                    }
//                    if(e.keyCode==106){
//                        fp.setSelection(true);
//                        e.widget = fp;
//                        // 主动触发button点击事件
//                        fp.notifyListeners(SWT.MouseUp, e);
//                    }
//                    if(e.keyCode==107){
//                        fn.setSelection(true);
//                        e.widget = fn;
//                        // 主动触发button点击事件
//                        fn.notifyListeners(SWT.MouseUp, e);
//                    }if(e.keyCode==108){
//                        tp.setSelection(true);
//                        e.widget = tp;
//                        // 主动触发button点击事件
//                        tp.notifyListeners(SWT.MouseUp, e);
//                    }if(e.keyCode==59){
//                        spares.setSelection(true);
//                        e.widget = spares;
//                        // 主动触发button点击事件
//                        spares.notifyListeners(SWT.MouseUp, e);
//                    }
//                }
//
//            }
//
//        };
//
//
//
//        main.getDisplay().addFilter(SWT.KeyDown, listener);
//
//
//        main.open();
//        main.layout();
//        while (!main.isDisposed()) {
//            if (!display.readAndDispatch()) {
//                display.sleep();
//            }
//        }
//    }
//
//    private static String readIndexFile(int rowNum) throws Exception
//    {
//        randomAccessFile.seek(0);
//        while (randomAccessFile.readInt() != rowNum) {
//            randomAccessFile.skipBytes(randomAccessFile.readUnsignedShort());
//        }
//        String content = randomAccessFile.readUTF();
//
//        return content;
//
//    }
//
//    public static int[] generateRandomNumberArray(int size, int largest)
//    {
//        Random random = new Random();
//        boolean[] bool = new boolean[largest];
//        int[] nums = new int[size];
//        int randInt = 0;
//        for (int j = 0; j < size; j++) {
//            do {
//
//                randInt = random.nextInt(largest);
//                nums[j] = randInt + 1;
//            } while (bool[randInt]);
//            bool[randInt] = true;
//        }
//        Arrays.sort(nums);
//        return nums;
//    }
//
//    private static boolean isExists(int[] range, long l)
//    {
//        for (int i = 0; i < range.length; i++) {
//            if (range[i] > l) {
//                return false;
//            }
//            if (range[i] == l) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static void generateIndexFile(String sourcePath, String dest)
//    {
//        if (!new File(dest).exists()) {
//            PageReader pageReader = new PageReader(sourcePath, 4000);
//            PageWriter pageWriter = new PageWriter(dest);
//            SegTask segTask = new SegTask(pageReader, pageWriter);
//            Thread[] threads = new Thread[3];
//            for (int i = 0; i < 3; i++) {
//                Thread thread = new Thread(segTask);
//                threads[i] = thread;
//                thread.start();
//            }
//            while (true) {
//                if (!threads[0].isAlive() && !threads[1].isAlive() && !threads[2].isAlive()) {
//                    try {
//                        if (pageWriter != null && pageWriter.getOut() != null) {
//                            pageWriter.getOut().writeInt(pageWriter.getPagenum()+1);
//                            pageWriter.getOut().flush();
//                            pageWriter.getOut().close();
//                        }
//                        if (pageReader.getIn() != null && pageReader != null) {
//                            pageReader.getIn().close();
//                        }
//                    } catch (Exception en) {
//                        en.printStackTrace();
//                    }
//                    break;
//                }
//            }
//        }
//    }
//    // ====================================================
//    // 快捷键
//    // ====================================================
//
//}
