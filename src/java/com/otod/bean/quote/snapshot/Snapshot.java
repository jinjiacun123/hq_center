/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;

import com.otod.bean.quote.kline.KLineData;
import com.otod.util.ApplicationConstant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Snapshot {

    public String symbol = "";//证券代码
    public String cnName = "";//证券名称
    public String exchCode;//交易所代码
    public int decimal = 0;//保留小数点
    public int tradeDate = 0;//交易日期
    public int quoteDate = 0;//行情日期
    public int quoteTime = 0;//行情时间
    public double openPrice;//开盘价格
    public double highPrice;//最高价格
    public double lowPrice;//最低价格
    public double lastPrice;//最新价格
    public double change;//涨跌
    public double changeRate;//涨跌幅
    public double pClose;//昨收价格
    public double lastVolume;//最新成交量 现量
    public double volume;//成交量
    public double lastTurnover;//最新成交金额
    public double turnover;//成交金额
    public List<BidAsk> bidQueue = new ArrayList<BidAsk>();//买排队
    public List<BidAsk> askQueue = new ArrayList<BidAsk>();//卖排队
    public double bid1Price;//买一价格
    public int bid1Volume;//买一量
    public double ask1Price;//卖一价
    public int ask1Volume;//卖一量
    
    private int dataType = ApplicationConstant.MEMORY_DATA;//数据类型，分为内存数据和数据库数据，默认为内存数据
    private int period;
    private int dbOperType;//数据库处理操作，有插入和更新两种
    
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
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

    public int getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(int tradeDate) {
        this.tradeDate = tradeDate;
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

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(double changeRate) {
        this.changeRate = changeRate;
    }

    public double getpClose() {
        return pClose;
    }

    public void setpClose(double pClose) {
        this.pClose = pClose;
    }

    public double getLastVolume() {
        return lastVolume;
    }

    public void setLastVolume(double lastVolume) {
        this.lastVolume = lastVolume;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getLastTurnover() {
        return lastTurnover;
    }

    public void setLastTurnover(double lastTurnover) {
        this.lastTurnover = lastTurnover;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public List<BidAsk> getBidQueue() {
        return bidQueue;
    }

    public void setBidQueue(List<BidAsk> bidQueue) {
        this.bidQueue = bidQueue;
    }

    public List<BidAsk> getAskQueue() {
        return askQueue;
    }

    public void setAskQueue(List<BidAsk> askQueue) {
        this.askQueue = askQueue;
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

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }
    
    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
    
    public int getDbOperType() {
        return dbOperType;
    }

    public void setDbOperType(int dbOperType) {
        this.dbOperType = dbOperType;
    }
    
    public void updateSnapshot(Snapshot snapshot) {
        
    }

    public KLineData cloneKLineData() {
        KLineData klineData = new KLineData();
        klineData.setSymbol(this.symbol);
        klineData.setQuoteDate(this.quoteDate);
        klineData.setQuoteTime(this.quoteTime);
        klineData.setOpenPrice(this.openPrice);
        klineData.setHighPrice(this.highPrice);
        klineData.setLowPrice(this.lowPrice);
        klineData.setClosePrice(this.lastPrice);
        klineData.setVolume(this.volume);
        klineData.setTurnover(this.turnover);
        return klineData;
    }

    @Override
    public Snapshot clone() {
        Snapshot quote = new Snapshot();
        quote.symbol = symbol;
        quote.cnName = cnName;
        quote.exchCode = exchCode;
        quote.decimal = decimal;
        quote.tradeDate = tradeDate;
        quote.quoteDate = quoteDate;
        quote.quoteTime = quoteTime;
        quote.openPrice = openPrice;
        quote.highPrice = highPrice;
        quote.lowPrice = lowPrice;
        quote.lastPrice = lastPrice;
        quote.change = change;
        quote.changeRate = changeRate;
        quote.pClose = pClose;
        quote.lastVolume = lastVolume;
        quote.volume = volume;
        quote.lastTurnover = lastTurnover;
        quote.turnover = turnover;
        quote.bidQueue = bidQueue;
        quote.askQueue = askQueue;
        quote.bid1Price = bid1Price;
        quote.bid1Volume = bid1Volume;
        quote.ask1Price = ask1Price;
        quote.ask1Volume = ask1Volume;
        return quote;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Snapshot other = (Snapshot) obj;
        if ((this.symbol == null) ? (other.symbol != null) : !this.symbol.equals(other.symbol)) {
            return false;
        }
        if ((this.cnName == null) ? (other.cnName != null) : !this.cnName.equals(other.cnName)) {
            return false;
        }
        if ((this.exchCode == null) ? (other.exchCode != null) : !this.exchCode.equals(other.exchCode)) {
            return false;
        }
        if (this.decimal != other.decimal) {
            return false;
        }
        if (this.tradeDate != other.tradeDate) {
            return false;
        }
        if (this.quoteDate != other.quoteDate) {
            return false;
        }
        if (this.quoteTime != other.quoteTime) {
            return false;
        }
        if (Double.doubleToLongBits(this.openPrice) != Double.doubleToLongBits(other.openPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.highPrice) != Double.doubleToLongBits(other.highPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lowPrice) != Double.doubleToLongBits(other.lowPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lastPrice) != Double.doubleToLongBits(other.lastPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.change) != Double.doubleToLongBits(other.change)) {
            return false;
        }
        if (Double.doubleToLongBits(this.changeRate) != Double.doubleToLongBits(other.changeRate)) {
            return false;
        }
        if (Double.doubleToLongBits(this.pClose) != Double.doubleToLongBits(other.pClose)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lastVolume) != Double.doubleToLongBits(other.lastVolume)) {
            return false;
        }
        if (Double.doubleToLongBits(this.volume) != Double.doubleToLongBits(other.volume)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lastTurnover) != Double.doubleToLongBits(other.lastTurnover)) {
            return false;
        }
        if (Double.doubleToLongBits(this.turnover) != Double.doubleToLongBits(other.turnover)) {
            return false;
        }
        if (this.bidQueue != other.bidQueue && (this.bidQueue == null || !this.bidQueue.equals(other.bidQueue))) {
            return false;
        }
        if (this.askQueue != other.askQueue && (this.askQueue == null || !this.askQueue.equals(other.askQueue))) {
            return false;
        }
        if (Double.doubleToLongBits(this.bid1Price) != Double.doubleToLongBits(other.bid1Price)) {
            return false;
        }
        if (this.bid1Volume != other.bid1Volume) {
            return false;
        }
        if (Double.doubleToLongBits(this.ask1Price) != Double.doubleToLongBits(other.ask1Price)) {
            return false;
        }
        if (this.ask1Volume != other.ask1Volume) {
            return false;
        }
        return true;
    }
    
}
