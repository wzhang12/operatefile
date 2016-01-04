package verifyDict;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegxUtil
{
    public static TreeMap<String, Integer> findAllIndexInLine(String regx, String line)
    {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(line);
        TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>();
        while (matcher.find()) {
            treeMap.put(matcher.toMatchResult().group(), matcher.start());
        }
        return treeMap;
    }

    public static ArrayList<String> findMatchedWords(String regx, String line)
    {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(line);
        ArrayList<String> arrayList = new ArrayList<String>();
        while (matcher.find()) {
            arrayList.add(matcher.toMatchResult().group().substring(0, matcher.toMatchResult().group().length() - 1));
        }
        return arrayList;
    }

    public static int countMatchedWords(String regx, String line)
    {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(line);
        int i = 0;
        while (matcher.find()) {
            i++;
        }
        return i;
    }

    public static String findFirstMatchedWords(String regx, String line)
    {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.toMatchResult().group();
        } else {
            return null;
        }
    }

    public static HashMap<String[],Integer> findFirstMatchedWords(Collection<String> regxes, String line)
    {
        HashMap<String[],Integer> hashMap = new HashMap<String[],Integer>();
       
        for (String regx : regxes) {
            String[] strings = new String[2];
            Pattern pattern = Pattern.compile(regx);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                strings[0]=regx;
                strings[1]=matcher.toMatchResult().group();
                hashMap.put(strings,matcher.start());
            } 
        }
        return hashMap;
    }
    /**
     * 
     * @MethodDescription: 
     * @Author: Wen.Zhang
     * @param regx
     * @param line
     * @return 返还的意思word 二是index
     * @return String[]
     * @throws
     * @CreateDate: 2015年8月18日 下午7:29:03
     * @Version: 1.0
     */
    public static String[] findFirstMatchedWordsAndIndex(String regx, String line)
    {
        String[] strings=new String[2];
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            strings[0]=matcher.toMatchResult().group();
            strings[1]=String.valueOf(matcher.start());
            return strings;
        } else {
            return null;
        }
    }
}
