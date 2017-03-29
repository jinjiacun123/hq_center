/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 *
 * @author admin
 */
public class Help {
    public static List<Map.Entry<String, Float>> sort_by_float(Map<String,Float> hashMap){       
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
        return arrayList;
    }
    
    public static List<Map.Entry<String, Double>> sort_by_double(Map<String,Double> hashMap){	
	//转换  
	ArrayList<Map.Entry<String, Double>> arrayList = new ArrayList<Map.Entry<String,Double>>(hashMap.entrySet());  
         System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
	//排序  
	Collections.sort(arrayList, new Comparator<Map.Entry<String, Double>>(){  
		public int compare(Map.Entry<String, Double> map1,  
				   Map.Entry<String,Double> map2) { 
                    /*
		    return ((map2.getValue() - map1.getValue() == 0) ? 0  
			    : (map2.getValue() - map1.getValue() > 0) ? 1  
                            : -1);  
                            */
                    return new Double(map1.getValue()).compareTo(map2.getValue());
		}  
	    });
	return arrayList;
    }
}
