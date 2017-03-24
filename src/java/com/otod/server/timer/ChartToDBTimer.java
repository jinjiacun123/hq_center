/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.timer;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.kline.KLineData;
import com.otod.bean.quote.kline.KLineQueue;

import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.minute.MinuteData;
import com.otod.bean.quote.minute.MinuteQueue;
import com.otod.dao.DayDao;
import com.otod.dao.Minute15Dao;
import com.otod.dao.Minute1Dao;
import com.otod.dao.Minute30Dao;
import com.otod.dao.Minute5Dao;
import com.otod.dao.Minute60Dao;
import com.otod.dao.MinuteDao;
import com.otod.dao.MonthDao;
import com.otod.dao.WeekDao;
import com.otod.db.Connector;
import com.otod.db.MysqlConnector;
import com.otod.util.ApplicationConstant;
import com.otod.util.mutithread.WorkHandle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ChartToDBTimer extends WorkHandle {

    public ChartToDBTimer() {
    }

    @Override
    public void doWork() {

        doMinute();
        doMinute1KLine();
        doMinute5KLine();
        doMinute15KLine();
        doMinute30KLine();
        doMinute60KLine();
        doDayKLine();
        doWeekKLine();
        doMonthKLine();

    }

    private void doMinute() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            MinuteDao minuteDao = new MinuteDao();
            minuteDao.setConnector(connector);
            List<MinuteData> updList = new ArrayList<MinuteData>();
            MinuteData minuteData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                MinuteQueue minuteQueue = ServerContext.getMinuteMap().get(symbol);
                if (minuteQueue != null) {
                    int size = minuteQueue.getQueue().size();
                    if (size > 0) {
                        minuteData = minuteQueue.getQueue().get(size - 1);
                        updList.add(minuteData);
                    }
                }
            }
            if (updList.size() > 0) {
                minuteDao.batchUpdate(updList);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doMinute1KLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            Minute1Dao minute1Dao = new Minute1Dao();
            minute1Dao.setConnector(connector);
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getMinute1Map().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        updList.add(klineData);
                    }
                }
            }
            if (updList.size() > 0) {
                minute1Dao.batchUpdate(updList);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doMinute5KLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            Minute5Dao minute5Dao = new Minute5Dao();
            minute5Dao.setConnector(connector);
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getMinute5Map().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        updList.add(klineData);
                    }
                }
            }
            if (updList.size() > 0) {
                minute5Dao.batchUpdate(updList);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doMinute15KLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            Minute15Dao minute15Dao = new Minute15Dao();
            minute15Dao.setConnector(connector);
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getMinute15Map().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        updList.add(klineData);
                    }
                }
            }
            if (updList.size() > 0) {
                minute15Dao.batchUpdate(updList);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doMinute30KLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            Minute30Dao minute30Dao = new Minute30Dao();
            minute30Dao.setConnector(connector);
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getMinute30Map().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        updList.add(klineData);
                    }
                }
            }
            if (updList.size() > 0) {
                minute30Dao.batchUpdate(updList);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doMinute60KLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            Minute60Dao minute60Dao = new Minute60Dao();
            minute60Dao.setConnector(connector);
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getMinute60Map().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        updList.add(klineData);
                    }
                }
            }
            if (updList.size() > 0) {
                minute60Dao.batchUpdate(updList);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doDayKLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            DayDao dayDao = new DayDao();
            dayDao.setConnector(connector);
            List<KLineData> saveList = new ArrayList<KLineData>();
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getDayMap().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        if (klineData.getPreQuoteDate() != 0) {
                            updList.add(klineData);
                        }
                    }
                }
            }
            if (saveList.size() > 0) {
                dayDao.batchSave(saveList);
            }
            if (updList.size() > 0) {
                dayDao.batchUpdate(updList);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doWeekKLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            WeekDao weekDao = new WeekDao();
            weekDao.setConnector(connector);
            List<KLineData> saveList = new ArrayList<KLineData>();
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getWeekMap().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        if (klineData.getPreQuoteDate() != 0) {
                            updList.add(klineData);
                        }
                    }
                }
            }
            if (saveList.size() > 0) {
                weekDao.batchSave(saveList);
            }
            if (updList.size() > 0) {
                weekDao.batchUpdate(updList);
                for (int i = 0; i < updList.size(); i++) {
                    klineData = updList.get(i);
                    klineData.setPreQuoteDate(0);
                }
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }

    private void doMonthKLine() {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            MonthDao monthDao = new MonthDao();
            monthDao.setConnector(connector);
            List<KLineData> saveList = new ArrayList<KLineData>();
            List<KLineData> updList = new ArrayList<KLineData>();
            KLineData klineData = null;
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                KLineQueue klineQueue = ServerContext.getMonthMap().get(symbol);
                if (klineQueue != null) {
                    int size = klineQueue.getQueue().size();
                    if (size > 0) {
                        klineData = klineQueue.getQueue().get(size - 1);
                        if (klineData.getPreQuoteDate() != 0) {
                            updList.add(klineData);
                        }
                    }
                }
            }
            if (saveList.size() > 0) {
                monthDao.batchSave(saveList);
            }
            if (updList.size() > 0) {
                monthDao.batchUpdate(updList);
                for (int i = 0; i < updList.size(); i++) {
                    klineData = updList.get(i);
                    klineData.setPreQuoteDate(0);
                }
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }
}