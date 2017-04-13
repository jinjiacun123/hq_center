/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;
import com.otod.bean.ServerContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
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
    
     public static List<Map.Entry<String, Double>> sort_by_double_ex(Map<String,Double> hashMap){	
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
                        return new Double(map2.getValue()).compareTo(map1.getValue());
		}  
	    });
	return arrayList;
    }
    
    
    public static  Map<String,Double> Obj2Map(Object obj) throws Exception{
            Map<String,Double> map=new HashMap<String, Double>();
            Field[] fields = obj.getClass().getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                map.put(field.getName(), field.getDouble(obj));
            }
            return map;
        }
    
    public Object map2Obj(Map<String,Object> map,Class<?> clz) throws Exception{
            Object obj = clz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for(Field field:declaredFields){
                int mod = field.getModifiers(); 
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
            return obj;
        }
    //type:0-上海，1-深圳
    //返回:0-深圳a股票,1-深圳b股票,2-上海a股票,3-上海b股票
    public static String findMarketByCode(int type, String code){
        String marketConfig = "";
        String marketName = "";
        String[] marketList= null;
        String marketPrefix = "";
        switch(type){
            case Config.TYPE_SH:{
                marketConfig = Config.MARKET_SH;                
                marketPrefix = "SH";
            }break;
            case Config.TYPE_SZ:{
                marketConfig = Config.MARKET_SZ;
                marketPrefix = "SZ";
            }break;
        }
        marketList = marketConfig.split(",");
        if (marketList.length > 0) {
            for (int marketIndex = 0; marketIndex < marketList.length; marketIndex++) {
                String[] marketNameAndRule = marketList[marketIndex].split("-");
                if (marketNameAndRule.length > 1) {
                    marketName = marketNameAndRule[0].trim();
                    for (int ruleIndex = 1; ruleIndex < marketNameAndRule.length; ruleIndex++) {
                        if (code.startsWith(marketPrefix + marketNameAndRule[ruleIndex])) {
                            return marketPrefix + "_" + marketName;
                        }
                    }
                }

            }
        }
        return "";
    }
    
    /*
    *查找当日涨跌停股票
    *
    *涨停:股票达到10%涨幅,新股40%涨幅
    *
    *跌停:股票下跌10%
    */
    public static Boolean checkIsDayRaiseFallStop(String code, Double rate){
        ConcurrentHashMap<String,String> raiseStopMap = ServerContext.getRaiseStopMap();
        ConcurrentHashMap<String, String> fallStopMap = ServerContext.getFallStopMap();
        if(rate > 0 && rate>=0.09){
            raiseStopMap.put(code, code);
            return true;
        }else if(rate <0 && rate<=-0.09){
            fallStopMap.put(code, code);
            return true;
        }
        
        return false;
    }
    
    public static boolean checkHalf(String str) {     
          byte[] Char;     
          for(int i = 0; i < str.length(); i++) {     
              try{     
                  Char = (new   Character(str.charAt(i)).toString()).getBytes("MS932");     
              }catch(Exception   e)   {     
                  return   false;     
              }     
              if(Char.length == 1)   {     
                  return   true;     
              }     
          }     
            return   false;     
      }  
    
    public static String byteToBit(byte b) {  
        return ""  
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)  
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)  
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)  
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);  
    }  
}
