/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.dao;

import com.otod.bean.ServerContext;

import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.kline.KLineData;
import com.otod.bean.quote.kline.KLineQueue;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.minute.MinuteData;
import com.otod.bean.quote.finance.FinanceData;
import com.otod.bean.quote.minute.MinuteQueue;
import com.otod.dao.DayDao;

import com.otod.dao.Minute15Dao;
import com.otod.dao.Minute1Dao;
import com.otod.dao.Minute30Dao;
import com.otod.dao.Minute5Dao;
import com.otod.dao.Minute60Dao;
import com.otod.dao.MinuteDao;
import com.otod.dao.FinanceDao;
import com.otod.dao.MonthDao;
import com.otod.dao.WeekDao;
import com.otod.db.Connector;
import com.otod.db.MysqlConnector;
import com.otod.util.ApplicationConstant;

import java.sql.Connection;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class InitDataDao {

    private Connector connector = null;

    public InitDataDao() {
        connector = new MysqlConnector();
    }

    public void doWork() {
        try {
            System.out.println("开始加载！");
            doInitMinute();//初始化分钟信息
            doInitKLine();//初始化k线信息
            doInitFinance();//初始化财务信息
            System.out.println("加载完成！");
        } finally {
            connector.close();
        }
    }

    private void doInitFinance(){
        FinanceDao financeDao = new FinanceDao();
        financeDao.setConnector(connector);
        Map<String, FinanceData> financeMap = ServerContext.getFinanceMap();
        System.out.println("要初始化的财务数据条数:"+financeMap.size());
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();            
            FinanceData financeData = financeDao.getBySymbol(symbol);
            financeMap.put(symbol, financeData);
        }
        System.out.println("初始化财务数据");
    }
    
    private void doInitMinute() {
        MinuteDao minuteDao = new MinuteDao();
        minuteDao.setConnector(connector);
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();
            MasterData masterData = (MasterData) entryset.getValue();
            List<MinuteData> list = minuteDao.getBySymbolAndDate(symbol, masterData.tradeDate);
            MinuteQueue minuteQute = new MinuteQueue();
            if (list.size() > 0) {
                minuteQute.setpClose(list.get(0).getpClose());
                minuteQute.setQueue(list);
                ServerContext.getMinuteMap().put(symbol, minuteQute);
            }

        }
    }


    private void doInitKLine() {
        Minute1Dao minute1Dao = new Minute1Dao();
        minute1Dao.setConnector(connector);

        Minute5Dao minute5Dao = new Minute5Dao();
        minute5Dao.setConnector(connector);

        Minute15Dao minute15Dao = new Minute15Dao();
        minute15Dao.setConnector(connector);

        Minute30Dao minute30Dao = new Minute30Dao();
        minute30Dao.setConnector(connector);

        Minute60Dao minute60Dao = new Minute60Dao();
        minute60Dao.setConnector(connector);

        DayDao dayDao = new DayDao();
        dayDao.setConnector(connector);

        WeekDao weekDao = new WeekDao();
        weekDao.setConnector(connector);

        MonthDao monthDao = new MonthDao();
        monthDao.setConnector(connector);

        KLineQueue klineQueue = null;
        List<KLineData> list = null;
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();
            MasterData masterData = (MasterData) entryset.getValue();

            list = minute1Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getMinute1Map().put(symbol, klineQueue);

            list = minute5Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getMinute5Map().put(symbol, klineQueue);

            list = minute15Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getMinute15Map().put(symbol, klineQueue);

            list = minute30Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getMinute30Map().put(symbol, klineQueue);

            list = minute60Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getMinute60Map().put(symbol, klineQueue);

            list = dayDao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getDayMap().put(symbol, klineQueue);

            list = weekDao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getWeekMap().put(symbol, klineQueue);

            list = monthDao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            klineQueue = new KLineQueue();
            klineQueue.setQueue(list);
            ServerContext.getMonthMap().put(symbol, klineQueue);

        }
    }
}
