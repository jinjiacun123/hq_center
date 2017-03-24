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
import com.otod.bean.quote.minute.MinuteQueue;
import com.otod.dao.DayDao;
import com.otod.dao.Minute15Dao;
import com.otod.dao.Minute1Dao;
import com.otod.dao.Minute30Dao;
import com.otod.dao.Minute5Dao;
import com.otod.dao.Minute60Dao;
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

/**
 *
 * @author Administrator
 */
public class ExchangeInitDao {

    private ExchangeData exchangeData;
    private Connector connector = null;

    public ExchangeInitDao() {
        connector = new MysqlConnector();
    }

    public void doWork(ExchangeData exchangeData) {
        try {
            this.exchangeData = exchangeData;
            System.out.println("Exchange初始化");
            doMinute();
            doKLine(ApplicationConstant.MINUTE1);
            doKLine(ApplicationConstant.MINUTE5);
            doKLine(ApplicationConstant.MINUTE15);
            doKLine(ApplicationConstant.MINUTE30);
            doKLine(ApplicationConstant.MINUTE60);
            doKLine(ApplicationConstant.DAY);
            doKLine(ApplicationConstant.WEEK);
            doKLine(ApplicationConstant.MONTH);
        } finally {
            connector.close();
        }

    }

    private void doMinute() {
    }

    private void doKLine(int period) {
        List<KLineData> list = null;
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();
            MasterData masterData = (MasterData) entryset.getValue();
            if (exchangeData.code.equals(masterData.exchCode)) {
                KLineQueue klineQueue = new KLineQueue();
                if (period == ApplicationConstant.MINUTE1) {
                    Minute1Dao minute1Dao = new Minute1Dao();
                    minute1Dao.setConnector(connector);
                    list = minute1Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getMinute1Map().put(symbol, klineQueue);
                } else if (period == ApplicationConstant.MINUTE5) {
                    Minute5Dao minute5Dao = new Minute5Dao();
                    minute5Dao.setConnector(connector);
                    list = minute5Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getMinute5Map().put(symbol, klineQueue);
                } else if (period == ApplicationConstant.MINUTE15) {
                    Minute15Dao minute15Dao = new Minute15Dao();
                    minute15Dao.setConnector(connector);
                    list = minute15Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getMinute15Map().put(symbol, klineQueue);
                } else if (period == ApplicationConstant.MINUTE30) {
                    Minute30Dao minute30Dao = new Minute30Dao();
                    minute30Dao.setConnector(connector);
                    list = minute30Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getMinute30Map().put(symbol, klineQueue);
                } else if (period == ApplicationConstant.MINUTE60) {
                    Minute60Dao minute60Dao = new Minute60Dao();
                    minute60Dao.setConnector(connector);
                    list = minute60Dao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getMinute60Map().put(symbol, klineQueue);
                } else if (period == ApplicationConstant.DAY) {
                    DayDao dayDao = new DayDao();
                    dayDao.setConnector(connector);
                    list = dayDao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getDayMap().put(symbol, klineQueue);
                } else if (period == ApplicationConstant.WEEK) {
                    WeekDao weekDao = new WeekDao();
                    weekDao.setConnector(connector);
                    list = weekDao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getWeekMap().put(symbol, klineQueue);
                } else if (period == ApplicationConstant.MONTH) {
                    MonthDao monthDao = new MonthDao();
                    monthDao.setConnector(connector);
                    list = monthDao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
                    klineQueue.setSymbol(symbol);
                    klineQueue.setQueue(list);
                    ServerContext.getMonthMap().put(symbol, klineQueue);
                }
            }
        }
    }
}