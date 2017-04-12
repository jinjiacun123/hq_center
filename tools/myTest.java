import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

public class myTest{
    static int sort_by_int(){
	//声明  
	Map<String,Integer> hashMap = new HashMap<String,Integer>();  
	//向Map中添加数据  
	//.....  
	hashMap.put("SH000001", 2);
	hashMap.put("SH000002", 8);
	hashMap.put("SH000003", 7);
	hashMap.put("SH000004", 5);
	//转换  
	ArrayList<Map.Entry<String, Integer>> arrayList = new ArrayList<Map.Entry<String, Integer>>(  
											    hashMap.entrySet());  
	//排序  
	Collections.sort(arrayList, new Comparator<Map.Entry<String, Integer>>() {  
		public int compare(Map.Entry<String, Integer> map1,  
				   Map.Entry<String, Integer> map2) {  
		    return (map2.getValue() - map1.getValue());  
		}  
	    });  
	//输出  
	for (Map.Entry<String, Integer> entry : arrayList) {  
	    System.out.println(entry.getKey() + "\t" + entry.getValue());  
	}
	return 0;
    }

    static int sort_by_float(){
	//声明  
	Map<String,Float> hashMap = new HashMap<String,Float>();  
	//向Map中添加数据  
	//.....  
	hashMap.put("SH000001", 2.1f);
	hashMap.put("SH000002", 8.3f);
	hashMap.put("SH000003", 7.5f);
	hashMap.put("SH000004", 5.0f);
	//转换  
	ArrayList<Map.Entry<String, Float>> arrayList = new ArrayList<Map.Entry<String,Float>>(hashMap.entrySet());  
	//排序  
	Collections.sort(arrayList, new Comparator<Map.Entry<String, Float>>(){  
		public int compare(Map.Entry<String, Float> map1,  
				   Map.Entry<String,Float> map2) {  
		    return ((map2.getValue() - map1.getValue() == 0) ? 0  
			    : (map2.getValue() - map1.getValue() > 0) ? 1  
                            : -1);  
		}  
	    });  
	//输出  
	for (Map.Entry<String, Float> entry : arrayList) {  
	    System.out.println(entry.getKey() + "\t" + entry.getValue());  
	}  
	return 0;
    }

    static int sort_by_double(){
	//声明  
	Map<String,Double> hashMap = new HashMap<String,Double>();  
	//向Map中添加数据  
	//.....  
	hashMap.put("SH000001", 2.1d);
	hashMap.put("SH000002", 8.3d);
	hashMap.put("SH000003", 7.5d);
	hashMap.put("SH000004", 5.0d);
	//转换  
	ArrayList<Map.Entry<String, Double>> arrayList = new ArrayList<Map.Entry<String,Double>>(hashMap.entrySet());  
	//排序  
	Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>(){  
		public int compare(Map.Entry<String, Double> map1,  
				   Map.Entry<String,Double> map2) {  
		    return ((map2.getValue() - map1.getValue() == 0) ? 0  
			    : (map2.getValue() - map1.getValue() > 0) ? 1  
                            : -1);  
		}  
	    });  
	//输出  
	for (Map.Entry<String, Double> entry : arrayList) {  
	    System.out.println(entry.getKey() + "\t" + entry.getValue());  
	}
	return 0;
    }

    public static void main(String[] args){
	//sort_by_int();
	//sort_by_float();
	sort_by_double();
    }
}
