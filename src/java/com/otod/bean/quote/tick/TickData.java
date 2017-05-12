/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.tick;

import com.otod.bean.quote.minute.MinuteData;
import com.otod.util.ApplicationConstant;

/**
 *
 * @author Administrator
 */
public class TickData {

    private int date;
    private int time;
    private String symbol;
    private String cnName;
    public String exchCode;//����������
    public double openPrice;//���̼۸�
    public double highPrice;//��߼۸�
    public double lowPrice;//��ͼ۸�
    public double lastPrice;//���¼۸�
    public double pClose;//���ռ۸�
    private double price;
    public double bid1Price;//��һ�۸�
    public int bid1Volume;//��һ��
    public double ask1Price;//��һ��
    public int ask1Volume;//��һ��
    private double volume;
    private double value;
    public double turnover;//�ɽ����

    private int type;
    private int dataType = ApplicationConstant.MEMORY_DATA;//�������ͣ���Ϊ�ڴ����ݺ����ݿ����ݣ�Ĭ��Ϊ�ڴ�����
    private int dbOperType;//���ݿ⴦��������в���͸�������

    public String getSymbol(){
        return symbol;
    }
    
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
    
   public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }
    
    public String getExchCode() {
        return exchCode;
    }

    public void setExchCode(String exchCode) {
        this.exchCode = exchCode;
    }
    
    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

     public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }
    
     public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
     public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }
    
     public double getBid1Price() {
        return bid1Price;
    }

    public void setBid1Price(double bid1Price) {
        this.bid1Price = bid1Price;
    }

    public int getBid1Volume() {
        return bid1Volume;
    }

    public void setBid1Volume(int bid1Volume) {
        this.bid1Volume = bid1Volume;
    }

    public double getAsk1Price() {
        return ask1Price;
    }

    public void setAsk1Price(double ask1Price) {
        this.ask1Price = ask1Price;
    }

    public int getAsk1Volume() {
        return ask1Volume;
    }

    public void setAsk1Volume(int ask1Volume) {
        this.ask1Volume = ask1Volume;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    public double getpClose() {
        return pClose;
    }

    public void setpClose(double pClose) {
        this.pClose = pClose;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

     public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDbOperType() {
        return dbOperType;
    }

    public void setDbOperType(int dbOperType) {
        this.dbOperType = dbOperType;
    }
    
     @Override
    public TickData clone() {
        TickData tickData   = new TickData();
        tickData.symbol     = this.symbol;
        tickData.cnName     = this.cnName;
        tickData.date       = this.date;
        tickData.time       = this.time;
        tickData.ask1Price  = this.ask1Price;
        tickData.ask1Volume = this.ask1Volume;
        tickData.bid1Price  = this.bid1Price;
        tickData.bid1Volume = this.bid1Volume;
        tickData.openPrice  = this.openPrice;
        tickData.highPrice  = this.highPrice;
        tickData.lowPrice   = this.lowPrice;
        tickData.lastPrice  = this.lastPrice;
        tickData.pClose     = this.pClose;
        tickData.volume     = this.volume;
        tickData.turnover   = this.turnover;
        tickData.dataType   = this.dataType;
        tickData.dbOperType = this.dbOperType;
        return tickData;
    }
}
