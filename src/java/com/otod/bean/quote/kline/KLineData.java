/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.kline;

import com.otod.util.ApplicationConstant;

/**
 *
 * @author Administrator
 */
public class KLineData {

    private int id;//自增长id
    private String symbol;//证券代码
    private int quoteDate;//行情日期
    private int quoteTime;//行情时间
    private double openPrice;//开盘价格
    private double highPrice;//最高价格
    private double lowPrice;//最低价格
    private double closePrice;//收盘价格
    private double volume;//成交量
    private double turnover;//成交金额
    private int dataType = ApplicationConstant.MEMORY_DATA;//数据类型，分为内存数据和数据库数据，默认为内存数据
    private int dbOperType;//数据库处理操作，有插入和更新两种
    private int period;
    private int preQuoteDate;//上一次的行情日期
    private int preQuoteTime;//上一次行情时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(int quoteDate) {
        this.quoteDate = quoteDate;
    }

    public int getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(int quoteTime) {
        this.quoteTime = quoteTime;
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

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPreQuoteDate() {
        return preQuoteDate;
    }

    public void setPreQuoteDate(int preQuoteDate) {
        this.preQuoteDate = preQuoteDate;
    }

    public int getPreQuoteTime() {
        return preQuoteTime;
    }

    public void setPreQuoteTime(int preQuoteTime) {
        this.preQuoteTime = preQuoteTime;
    }

  

    @Override
    public KLineData clone() {
        KLineData klineData = new KLineData();
        klineData.symbol = this.symbol;
        klineData.quoteDate = this.quoteDate;
        klineData.quoteTime = this.quoteTime;
        klineData.openPrice = this.openPrice;
        klineData.highPrice = this.highPrice;
        klineData.lowPrice = this.lowPrice;
        klineData.closePrice = this.closePrice;
        klineData.volume = this.volume;
        klineData.turnover = this.turnover;
        klineData.dataType = this.dataType;
        klineData.dbOperType = this.dbOperType;
        klineData.period = this.period;
        klineData.preQuoteDate=this.preQuoteDate;
        klineData.preQuoteTime=this.preQuoteTime;
        return klineData;
    }
    
}
