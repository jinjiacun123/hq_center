/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.minute;

import com.otod.util.ApplicationConstant;

/**
 * 分时数据信息类
 *
 * @author Administrator
 */
public class MinuteData {

    private int id;
    private String symbol;//证券代码
    private int tradeDate;//交易日期
    private int quoteDate;//行情日期
    private int quoteTime;//行情时间
    private double pClose;//昨收价格
    private double closePrice;//收盘价
    private double volume;//成交量
    private double turnover;//成交金额
    private int dataType = ApplicationConstant.MEMORY_DATA;//数据类型，分为内存数据和数据库数据，默认为内存数据
    private int dbOperType;//数据库处理操作，有插入和更新两种

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

    public int getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(int tradeDate) {
        this.tradeDate = tradeDate;
    }

    public double getpClose() {
        return pClose;
    }

    public void setpClose(double pClose) {
        this.pClose = pClose;
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

    @Override
    public MinuteData clone() {
        MinuteData minuteData = new MinuteData();
        minuteData.id = this.id;
        minuteData.symbol = this.symbol;
        minuteData.tradeDate = this.tradeDate;
        minuteData.quoteDate = this.quoteDate;
        minuteData.quoteTime = this.quoteTime;
        minuteData.pClose = this.pClose;
        minuteData.closePrice = this.closePrice;
        minuteData.volume = this.volume;
        minuteData.turnover = this.turnover;
        minuteData.dataType = this.dataType;
        minuteData.dbOperType = this.dbOperType;
        return minuteData;
    }
}
