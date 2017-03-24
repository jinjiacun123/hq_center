/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean;

import com.otod.bean.quote.StockDividend;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.kline.KLineQueue;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.minute.MinuteQueue;
import com.otod.bean.quote.snapshot.ForexSnapshot;
import com.otod.bean.quote.snapshot.Snapshot;
import com.otod.bean.quote.snapshot.SnapshotQueue;
import com.otod.bean.quote.tick.TickQueue;
import com.otod.bean.quote.tradetime.PeriodTime;
import com.otod.bean.quote.tradetime.TimeNode;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Administrator
 */
public class ServerContext {

    private static LinkedBlockingQueue<List<ForexSnapshot>> dbSnapshotQueue = new LinkedBlockingQueue<List<ForexSnapshot>>();
    private static LinkedBlockingQueue<Object> signalQueue = new LinkedBlockingQueue<Object>();
    private static LinkedBlockingQueue<Snapshot> rtSnapshotQueue = new LinkedBlockingQueue<Snapshot>();
    private static ConcurrentHashMap<String, Snapshot> snapshotMap = new ConcurrentHashMap<String, Snapshot>();
    private static ConcurrentHashMap<String, Snapshot> tempSnapshotMap = new ConcurrentHashMap<String, Snapshot>();
    private static ConcurrentHashMap<String, MasterData> masterMap = new ConcurrentHashMap<String, MasterData>();
    private static ConcurrentHashMap<String, MasterData> keyMasterMap = new ConcurrentHashMap<String, MasterData>();
    private static ConcurrentHashMap<String, ExchangeData> exchangeMap = new ConcurrentHashMap<String, ExchangeData>();
    private static ConcurrentHashMap<String, List<TimeNode>> tradeTimeMap = new ConcurrentHashMap<String, List<TimeNode>>();
    private static ConcurrentHashMap<String, PeriodTime> periodTimeMap = new ConcurrentHashMap<String, PeriodTime>();
    private static ConcurrentHashMap<String, List<String>> groupMap = new ConcurrentHashMap<String, List<String>>();
    private static ConcurrentHashMap<String, MinuteQueue> minuteMap = new ConcurrentHashMap<String, MinuteQueue>();
    private static ConcurrentHashMap<String, KLineQueue> minute1Map = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> minute3Map = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> minute5Map = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> minute10Map = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> minute15Map = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> minute30Map = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> minute60Map = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> dayMap = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> weekMap = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, KLineQueue> monthMap = new ConcurrentHashMap<String, KLineQueue>();
    private static ConcurrentHashMap<String, SnapshotQueue> listMap  = new ConcurrentHashMap<String, SnapshotQueue>();
    private static ConcurrentHashMap<String, TickQueue> tickMap = new ConcurrentHashMap<String, TickQueue>();
    private static LinkedBlockingQueue<Object> chartToDBQueue = new LinkedBlockingQueue<Object>();
    private static LinkedBlockingQueue<Snapshot> quoteToDBQueue = new LinkedBlockingQueue<Snapshot>();
    private static boolean authorizeFlag = true;
    private static ConcurrentHashMap<String, List<StockDividend>> stockDividendMap = new ConcurrentHashMap<String, List<StockDividend>>();


    public static ConcurrentHashMap<String, Snapshot> getSnapshotMap() {
        return snapshotMap;
    }

    public static void setSnapshotMap(ConcurrentHashMap<String, Snapshot> snapshotMap) {
        ServerContext.snapshotMap = snapshotMap;
    }

    public static LinkedBlockingQueue<List<ForexSnapshot>> getDbSnapshotQueue() {
        return dbSnapshotQueue;
    }

    public static void setDbSnapshotQueue(LinkedBlockingQueue<List<ForexSnapshot>> dbSnapshotQueue) {
        ServerContext.dbSnapshotQueue = dbSnapshotQueue;
    }

    public static ConcurrentHashMap<String, MasterData> getMasterMap() {
        return masterMap;
    }

    public static void setMasterMap(ConcurrentHashMap<String, MasterData> masterMap) {
        ServerContext.masterMap = masterMap;
    }

    public static ConcurrentHashMap<String, MasterData> getKeyMasterMap() {
        return keyMasterMap;
    }

    public static void setKeyMasterMap(ConcurrentHashMap<String, MasterData> keyMasterMap) {
        ServerContext.keyMasterMap = keyMasterMap;
    }

    public static ConcurrentHashMap<String, List<TimeNode>> getTradeTimeMap() {
        return tradeTimeMap;
    }

    public static void setTradeTimeMap(ConcurrentHashMap<String, List<TimeNode>> tradeTimeMap) {
        ServerContext.tradeTimeMap = tradeTimeMap;
    }

    public static ConcurrentHashMap<String, PeriodTime> getPeriodTimeMap() {
        return periodTimeMap;
    }

    public static void setPeriodTimeMap(ConcurrentHashMap<String, PeriodTime> periodTimeMap) {
        ServerContext.periodTimeMap = periodTimeMap;
    }

    public static ConcurrentHashMap<String, ExchangeData> getExchangeMap() {
        return exchangeMap;
    }

    public static void setExchangeMap(ConcurrentHashMap<String, ExchangeData> exchangeMap) {
        ServerContext.exchangeMap = exchangeMap;
    }

    public static LinkedBlockingQueue<Object> getSignalQueue() {
        return signalQueue;
    }

    public static void setSignalQueue(LinkedBlockingQueue<Object> signalQueue) {
        ServerContext.signalQueue = signalQueue;
    }

    public static ConcurrentHashMap<String, List<String>> getGroupMap() {
        return groupMap;
    }

    public static void setGroupMap(ConcurrentHashMap<String, List<String>> groupMap) {
        ServerContext.groupMap = groupMap;
    }

    public static ConcurrentHashMap<String, MinuteQueue> getMinuteMap() {
        return minuteMap;
    }

    public static void setMinuteMap(ConcurrentHashMap<String, MinuteQueue> minuteMap) {
        ServerContext.minuteMap = minuteMap;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMinute1Map() {
        return minute1Map;
    }

    public static void setMinute1Map(ConcurrentHashMap<String, KLineQueue> minute1Map) {
        ServerContext.minute1Map = minute1Map;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMinute3Map() {
        return minute3Map;
    }

    public static void setMinute3Map(ConcurrentHashMap<String, KLineQueue> minute3Map) {
        ServerContext.minute3Map = minute3Map;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMinute5Map() {
        return minute5Map;
    }

    public static void setMinute5Map(ConcurrentHashMap<String, KLineQueue> minute5Map) {
        ServerContext.minute5Map = minute5Map;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMinute10Map() {
        return minute10Map;
    }

    public static void setMinute10Map(ConcurrentHashMap<String, KLineQueue> minute10Map) {
        ServerContext.minute10Map = minute10Map;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMinute15Map() {
        return minute15Map;
    }

    public static void setMinute15Map(ConcurrentHashMap<String, KLineQueue> minute15Map) {
        ServerContext.minute15Map = minute15Map;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMinute30Map() {
        return minute30Map;
    }

    public static void setMinute30Map(ConcurrentHashMap<String, KLineQueue> minute30Map) {
        ServerContext.minute30Map = minute30Map;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMinute60Map() {
        return minute60Map;
    }

    public static void setMinute60Map(ConcurrentHashMap<String, KLineQueue> minute60Map) {
        ServerContext.minute60Map = minute60Map;
    }

    public static ConcurrentHashMap<String, KLineQueue> getDayMap() {
        return dayMap;
    }

    public static void setDayMap(ConcurrentHashMap<String, KLineQueue> dayMap) {
        ServerContext.dayMap = dayMap;
    }

    public static ConcurrentHashMap<String, KLineQueue> getWeekMap() {
        return weekMap;
    }

    public static void setWeekMap(ConcurrentHashMap<String, KLineQueue> weekMap) {
        ServerContext.weekMap = weekMap;
    }

    public static ConcurrentHashMap<String, KLineQueue> getMonthMap() {
        return monthMap;
    }
    
    public static ConcurrentHashMap<String, SnapshotQueue> getListMap() {
        return listMap;
    }

    public static void setMonthMap(ConcurrentHashMap<String, KLineQueue> monthMap) {
        ServerContext.monthMap = monthMap;
    }

    public static ConcurrentHashMap<String, TickQueue> getTickMap() {
        return tickMap;
    }

    public static void setTickMap(ConcurrentHashMap<String, TickQueue> tickMap) {
        ServerContext.tickMap = tickMap;
    }

    public static LinkedBlockingQueue<Snapshot> getRtSnapshotQueue() {
        return rtSnapshotQueue;
    }

    public static void setRtSnapshotQueue(LinkedBlockingQueue<Snapshot> rtSnapshotQueue) {
        ServerContext.rtSnapshotQueue = rtSnapshotQueue;
    }

    public static LinkedBlockingQueue<Object> getChartToDBQueue() {
        return chartToDBQueue;
    }

    public static void setChartToDBQueue(LinkedBlockingQueue<Object> chartToDBQueue) {
        ServerContext.chartToDBQueue = chartToDBQueue;
    }

    public static LinkedBlockingQueue<Snapshot> getQuoteToDBQueue() {
        return quoteToDBQueue;
    }

    public static void setQuoteToDBQueue(LinkedBlockingQueue<Snapshot> quoteToDBQueue) {
        ServerContext.quoteToDBQueue = quoteToDBQueue;
    }

    public static ConcurrentHashMap<String, Snapshot> getTempSnapshotMap() {
        return tempSnapshotMap;
    }

    public static void setTempSnapshotMap(ConcurrentHashMap<String, Snapshot> tempSnapshotMap) {
        ServerContext.tempSnapshotMap = tempSnapshotMap;
    }

    public static boolean isAuthorizeFlag() {
        return authorizeFlag;
    }

    public static void setAuthorizeFlag(boolean authorizeFlag) {
        ServerContext.authorizeFlag = authorizeFlag;
    }

    public static ConcurrentHashMap<String, List<StockDividend>> getStockDividendMap() {
        return stockDividendMap;
    }

    public static void setStockDividendMap(ConcurrentHashMap<String, List<StockDividend>> stockDividendMap) {
        ServerContext.stockDividendMap = stockDividendMap;
    }
    
}
