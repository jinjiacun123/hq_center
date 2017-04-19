/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jime
 * 涨停价，跌停价
 */
public class UpDownStopPrice {
    private double upStopPrice;
    private double downStopPrice;
    private Map<String,Double> ruleList = new HashMap<String, Double>(){{//规则列表
        put("N", 1.44);
        put("ST", 1.05);
        put("*ST", 1.05);
        put("P", 1.05);
    }};
    
     /*
    通过股票代码和昨收，计算出当前的涨停价和跌停价
    
    计算涨跌停：

    1.1、股票代码中，含字母N的
         涨停价=昨收*1.44
         跌停价=昨收*0.64
    1.2、股票代码，含ST或*ST或P的
         涨停价=昨收*1.05
         跌停价=昨收*0.95
    1.3、其他
         涨停价=昨收*1.1
         跌停价=昨收*0.9
    */
    public UpDownStopPrice(String name, double pClose){
         double[] params = {1.1,0.9};
        
        if(name.startsWith("N")){
            params[0] = 1.44;
            params[1] = 0.64;
        }else if(name.startsWith("ST") || name.startsWith("*ST") || name.startsWith("P")){
            params[0] = 1.05;
            params[1] = 0.95;
        }
        this.upStopPrice = pClose*params[0];
        this.downStopPrice = pClose*params[1];
    }

    public double getUpStopPrice() {
        return upStopPrice;
    }

    public void setUpStopPrice(double upStopPrice) {
        this.upStopPrice = upStopPrice;
    }

    public double getDownStopPrice() {
        return downStopPrice;
    }

    public void setDownStopPrice(double downStopPrice) {
        this.downStopPrice = downStopPrice;
    }
    
    public String getKey(String code){
        //List<String> ls=new ArrayList<String>();
        String ls = null;
        Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        Matcher matcher = pattern.matcher(code);
        while(matcher.find()){
           //ls.add(matcher.group());
            ls = matcher.group();
        }
        return ls;
    }
}
