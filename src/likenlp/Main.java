//package likenlp;
//
//import com.google.common.base.Charsets;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//import com.google.common.io.Files;
//import com.google.common.io.LineProcessor;
//import main.java.com.github.stuxuhai.jpinyin.PinyinFormat;
//import main.java.com.github.stuxuhai.jpinyin.PinyinHelper;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//
///**
// * Created by DELL on 2015/8/3.
// */
//public class Main {
//    private static String sourcePath="C:\\Users\\DELL\\Desktop\\pinyin\\患者文本.txt";
//    private static String outputPath="C:\\Users\\DELL\\Desktop\\pinyin\\患者文本1.txt";
//    private static String dicPath="C:\\Users\\DELL\\Desktop\\pinyin\\words.txt";
//    public static void main(String[] args) throws IOException {
//        //将字典中的词转换成拼音集合，并记录字符串的长度
//        List<String> words=Files.readLines(new File(dicPath), Charsets.UTF_8);
//        //key:拼音 value:词语的长度
//        HashMap<String,ArrayList<String>> map=Maps.newHashMap();
//        Set<String> set=Sets.newHashSet();
//        BufferedWriter bw=Files.newWriter(new File(outputPath), Charsets.UTF_8);
//        ArrayList<String> list=null;
//        for(String word:words){
//            word=word.trim();
//            String firstPY=PinyinHelper.convertToPinyinString(word.charAt(0)+"","", PinyinFormat.WITHOUT_TONE);
//            if((list=map.get(firstPY))==null){
//                list= new ArrayList<String>();
//            }
//            list.add(PinyinHelper.convertToPinyinString(word,"",PinyinFormat.WITHOUT_TONE)+"_"+word.length()+"_"+word);
//            System.out.println(list);
//            map.put(firstPY, list);
//        }
//
//        //所有的value
//        ImmutableList<String> lines =Files.asCharSource(new File(sourcePath), Charsets.UTF_8).readLines(new LineProcessor<ImmutableList<String>>() {
//            /**
//             *
//             * @param s 文件中每一行文本
//             * @return
//             * @throws IOException
//             */
//            @Override
//            public boolean processLine(String s) throws IOException {
//                ArrayList<String> pinyinList=null;
//                for (int i = 0; i < s.length(); i++) {
//                    if((pinyinList=map.get(PinyinHelper.convertToPinyinString(s.charAt(i) + "", "", PinyinFormat.WITHOUT_TONE)))!=null){
//                        for (int j = 0; j <pinyinList.size() ; j++) {
//                            String pinyinSententWord;
//                            if(i + Integer.valueOf(pinyinList.get(j).split("_")[1])<s.length()) {
//                                String sententWord=s.substring(i, i + Integer.valueOf(pinyinList.get(j).split("_")[1]));
//                                pinyinSententWord = PinyinHelper.convertToPinyinString(sententWord, "", PinyinFormat.WITHOUT_TONE);
//                                if (pinyinList.get(j).split("_")[0].equals(pinyinSententWord)&&!sententWord.equals(pinyinList.get(j).split("_")[2])) {
//                                    set.add(s.substring(i, i + Integer.valueOf(pinyinList.get(j).split("_")[1])));
//                                }
//                             }
//                        }
//                    }
//                }
//                return true;
//            }
//
//            @Override
//            public ImmutableList<String> getResult() {
//                return null;
//            }
//        });
//        for(String word:set){
//            bw.write(word+"\n");
//        }
//        bw.flush();
//        bw.close();
//    }
//}
