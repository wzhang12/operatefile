package verifyDict;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class HashMapSortedUtil
{
    /**
     * @MethodDescription: 返回有序的hashMap并将原来的map给清空
     * @Author: Wen.Zhang
     * @param map 待排序hashmap
     * @return
     * @return TreeMap<String,Integer>
     * @throws
     * @CreateDate: 2015年4月23日 下午3:36:00
     * @Version: 1.0
     */
    public static <T> TreeMap<T, Integer> SortByValueMinimal(Map<T, Integer> map)
    {
        if (map == null || map.size() == 0) {
            return new TreeMap<T, Integer>();
        }
        ValueComparator<T> vc = new ValueComparator<T>(map);
        TreeMap<T, Integer> sortedMap = new TreeMap<T, Integer>(vc);
        Iterator<Entry<T, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<T, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}

class ValueComparator <T> implements Comparator<T>
{

    Map<T, Integer> map;

    public ValueComparator(Map<T, Integer> base)
    {
        this.map = base;
    }

    public int compare(T a, T b)
    {
        if (map.get(a) >= map.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys
    }
}
