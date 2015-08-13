package verifyDict;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegxUtil
{
      public static TreeMap<String,Integer> findAllIndexInLine(String regx,String line){
          Pattern pattern = Pattern.compile(regx);
          Matcher matcher = pattern.matcher(line);
          TreeMap<String,Integer> treeMap=new TreeMap<String,Integer>();
          while (matcher.find()) {
              treeMap.put(matcher.toMatchResult().group(),matcher.start());
          }
          return treeMap;
      }
      public static ArrayList<String> findMatchedWords(String regx,String line){
    	   Pattern pattern = Pattern.compile(regx);
           Matcher matcher = pattern.matcher(line);
           ArrayList<String> arrayList = new ArrayList<String>();
           while (matcher.find()) {
        	   arrayList.add(matcher.toMatchResult().group().substring(0,matcher.toMatchResult().group().length()-1));
           }
           return arrayList;
      }
      public static int countMatchedWords(String regx,String line){
    	   Pattern pattern = Pattern.compile(regx);
           Matcher matcher = pattern.matcher(line);
           int i=0;
           while (matcher.find()) {
        	   i++;
           }
          return i;
      }
      public static String findFirstMatchedWords(String regx,String line){
    	  Pattern pattern = Pattern.compile(regx);
          Matcher matcher = pattern.matcher(line);
          if(matcher.find()){
        	  return matcher.toMatchResult().group();
          }else{
        	  return null;
          }
      }
}
