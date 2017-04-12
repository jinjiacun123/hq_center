import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Date;
import java.text.SimpleDateFormat;  
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
 
public class MapSortTest {
    static LinkedHashMap<String, Float> sortTF(HashMap<String, Float> tfidf) {  
        // TODO Auto-generated method stub  
        //map存入list中   
        List<Map.Entry<String,Float>> list_data = new LinkedList<Map.Entry<String, Float>>(tfidf.entrySet());  
        //注意map是无序的，要保证顺序需要LinkedHashMap  
        LinkedHashMap<String, Float> map1 = new LinkedHashMap<String, Float>();  
        Collections.sort(list_data,new Comparator<Map.Entry<String, Float>>()  
        {  
            @Override  
            public int compare(Map.Entry<String, Float> o1,  
                    Map.Entry<String, Float> o2) {  
                // TODO Auto-generated method stub  
                  
                return (int) ((o2.getValue()-o1.getValue())*1000000.0);  
            }  
        }         
        );  
        for (Iterator<Map.Entry<String, Float>> it = list_data.iterator(); it.hasNext();) {  
            Map.Entry<String,Float> entry1 = it.next();  
            map1.put(entry1.getKey(), entry1.getValue());  
            //System.out.println("key:"+entry1.getKey()+"value:"+entry1.getValue());  
        }  
        return map1;  
    }  
	
    static int sort_by_treeset(){
	Set set = new TreeSet();
	set.add(new Pair("me", "1000"));
	
	set.add(new Pair("and", "4000"));
	set.add(new Pair("you", "3000"));
	
	set.add(new Pair("food", "10000"));
	set.add(new Pair("hungry", "5000"));
	
	set.add(new Pair("later", "6000"));
	set.add(new Pair("myself", "1000"));
	
	for (Iterator i = set.iterator(); i.hasNext();) 
	    System.out.println(i.next());
	return 0;
    }
    
    static int tree_map_test_by_key(){
	Map<String, String> map = new TreeMap<String, String>(
							      new Comparator<String>() {
								  public int compare(String obj1, String obj2) {
								      // 降序排序
								      return obj2.compareTo(obj1);
								  }
							      });
        map.put("c", "ccccc");
        map.put("a", "aaaaa");
        map.put("b", "bbbbb");
        map.put("d", "ddddd");
	
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + map.get(key));
        }
	return 0;
    }
    
   static int tree_map_test_by_value(){
       Map<String, String> map = new TreeMap<String, String>();
       map.put("d", "ddddd");
       map.put("b", "bbbbb");
       map.put("a", "aaaaa");
       map.put("c", "ccccc");
       
       //这里将map.entrySet()转换成list
       List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
       //然后通过比较器来实现排序
       Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
	       //升序排序
	       public int compare(Map.Entry<String, String> o1,
				  Map.Entry<String, String> o2) {
		   return o1.getValue().compareTo(o2.getValue());
	       }
	       
	   });
       
       for(Map.Entry<String,String> mapping:list){
	   System.out.println(mapping.getKey()+":"+mapping.getValue());
       } 
       return 0;
   }
    
   static int hash_map_test(){
	Map<String, String> map = new HashMap<String, String>();
        map.put("c", "ccccc");
        map.put("a", "aaaaa");
        map.put("b", "bbbbb");
        map.put("d", "ddddd");
	
        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
		//升序排序
		public int compare(Map.Entry<String, String> o1,
				   Map.Entry<String, String> o2) {
		    return o1.getValue().compareTo(o2.getValue());
		}
		
	    });
	
        for(Map.Entry<String,String> mapping:list){
	    System.out.println(mapping.getKey()+":"+mapping.getValue());
	}
	return 0;
    }	
	
  public static void main(String[] args) {
      //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
      //log.info("开始：" + df.format(new Date(System.currentTimeMillis()))); 
      //System.out.println(df.format(new Date(System.currentTimeMillis())).ToString()); 
      //System.out.println(System.currentTimeMillis());
      //System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() ));
      //sort_by_treeset();
      //tree_map_test_by_key();
      //System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() ));
      //tree_map_test_by_value();
      //System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() ));
      //hash_map_test();
      //System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS") .format(new Date() ));
      //System.out.println(System.currentTimeMillis());
      HashMap<String,Float> itemMap = new HashMap<String, Float>();  
       itemMap.put("SH0000001", 0.001f);  
       itemMap.put("SH0000002", 0.002f);  
       itemMap.put("SH0000003", 13.35f);  
       itemMap.put("SH0000004", 12.04f);  
       Iterator<String,Float> countryCapitalIter = sortTF(itemMap);
       while(countryCapitalIter.hasNext()){  
	   //Country countryObj=countryCapitalIter.next();  
	   //String capital=countryCapitalMap.get(countryObj);  
	   System.out.println(countryObj.getName()+"----"+capital);  
       }  
  }  
  
 // System.out.println(df.format(new Date(System.currentTimeMillis())).ToString());
}
 
class Pair implements Comparable {
  private final String name;
  private final int number;
 
  public Pair(String name, int number) {
    this.name = name;
    this.number = number;
  }
 
  public Pair(String name, String number) throws NumberFormatException {
    this.name = name;
    this.number = Integer.parseInt(number); 
  }
 
  public int compareTo(Object o) {
    if (o instanceof Pair) {
      int cmp = Double.compare(number, ((Pair) o).number);
      if (cmp != 0) {
        return cmp;
      }
      return name.compareTo(((Pair) o).name);
    }
 
    throw new ClassCastException("Cannot compare Pair with "
        + o.getClass().getName());
 
  }
 
  public String toString() {
    return name + ' ' + number;
  }
}
