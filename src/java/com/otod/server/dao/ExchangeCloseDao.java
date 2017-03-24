/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.dao;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.tradetime.TimeNode;
import com.otod.db.Connector;
import com.otod.db.MysqlConnector;
import com.otod.util.DateUtil;
import com.otod.util.StringUtil;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ExchangeCloseDao {

    private ExchangeData exchangeData;
    private Connector connector = null;
    private ArrayList<Integer> cnHoliday;

    public ExchangeCloseDao() {
        connector = new MysqlConnector();
    }

    public void doWork(ExchangeData exchangeData) {
        try {
            this.exchangeData = exchangeData;
            exchangeData.isTrade = false;
            int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));
            int time = Integer.parseInt(DateUtil.formatDate(null, "HHmmss"));
            System.out.println("交易所收盘" + exchangeData.code + "||" + exchangeData.cnName + "||" + exchangeData.tradeDate + "||" + date + "||" + time);

            doMinuteClear();

            ExchangeInitDao exchangeInitDao = new ExchangeInitDao();
            exchangeInitDao.doWork(exchangeData);
            MasterData.updateMasterDataByExchange(exchangeData);
            exchangeData.signalType = ExchangeData.CloseSignal;
        } finally {
            connector.close();
        }
    }

    public void doMinuteClear() {
        
    }

    public void doExchangeList() {
        int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));
        int time = Integer.parseInt(DateUtil.formatDate(null, "HHmm"));
        List<TimeNode> list = ServerContext.getTradeTimeMap().get("TradeTime1");
        int startTime = list.get(0).startTime;
        int endTime = list.get(list.size() - 1).endTime;
        Date sdate = DateUtil.parseDate(date + " " + StringUtil.completeByBefore(startTime + "", 4, "0"), "yyyyMMdd HHmm");
        Date edate = DateUtil.parseDate(date + " " + StringUtil.completeByBefore(endTime + "", 4, "0"), "yyyyMMdd HHmm");
//        Calendar ca = Calendar.getInstance();
//        ca.setTime(sdate);
//        ca.add(Calendar.MINUTE, -10);
//        sdate = ca.getTime();
        Calendar ca = Calendar.getInstance();
        ca.setTime(edate);
        ca.add(Calendar.MINUTE, 10);
        edate = ca.getTime();
        System.out.println("开盘时间:" + Integer.parseInt(DateUtil.formatDate(sdate, "HHmm")));
        exchangeData.openTime = Integer.parseInt(DateUtil.formatDate(sdate, "HHmm"));
        exchangeData.closeTime = Integer.parseInt(DateUtil.formatDate(edate, "HHmm"));
        exchangeData.holidayList = cnHoliday;

    }

    private void doMasterList() {

        MasterData masterData = null;

        ExchangeData exchangeData;
        Iterator it = ServerContext.getMasterMap().entrySet().iterator();
        List<TimeNode> tradeTime = null;
        while (it.hasNext()) {
            Map.Entry<String, MasterData> entryset = (Map.Entry<String, MasterData>) it.next();
            String symbol = (String) entryset.getKey();
            masterData = (MasterData) entryset.getValue();
            exchangeData = ServerContext.getExchangeMap().get(masterData.exchCode);

            if (masterData.keyStr != null) {
                ServerContext.getKeyMasterMap().put(masterData.keyStr, masterData);
            }

            if (exchangeData != null) {
                if (exchangeData.openTime >= 0 && exchangeData.closeTime >= 0) {
                    tradeTime = ServerContext.getTradeTimeMap().get(exchangeData.tradeTimeKey);
                    masterData.tradeDate = exchangeData.tradeDate;
                    masterData.isTrade = exchangeData.isTrade;
                    masterData.tradeTimes = tradeTime;
                    masterData.tradeTimeKey = exchangeData.tradeTimeKey;
                } else {
                    tradeTime = ServerContext.getTradeTimeMap().get(masterData.tradeTimeKey);
                    masterData.tradeTimes = tradeTime;
                }
            }
        }
    }
}
