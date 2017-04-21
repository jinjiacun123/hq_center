/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.dao;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.finance.FinanceData;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.tradetime.PeriodTime;
import com.otod.bean.quote.tradetime.TimeNode;
import com.otod.dao.FinanceDao;
import com.otod.db.Connector;
import com.otod.db.MysqlConnector;

import com.otod.util.DateUtil;
import com.otod.util.HttpClientUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ExchangeOpenDao {

    private ExchangeData exchangeData;
    private Connector connector = null;
    private ArrayList<Integer> cnHoliday;

    public ExchangeOpenDao() {
        connector = new MysqlConnector();
    }

    public void doWork(ExchangeData exchangeData) {
        try {
            this.exchangeData = exchangeData;
            int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));
            int time = Integer.parseInt(DateUtil.formatDate(null, "HHmmss"));
            System.out.println("交易所开盘" + exchangeData.code + "||" + exchangeData.cnName + "||" + exchangeData.tradeDate + "||" + date + "||" + time);
            //doMinuteClear();
            doSnapshotClear();
            //doAuthorize();
            
            exchangeData.isTrade = true;
            exchangeData.tradeDate = date;
            MasterData.updateMasterDataByExchange(exchangeData);
            doFinanceUpdate();//更新财务信息
            exchangeData.signalType = ExchangeData.OpenSignal;
        } finally {
            connector.close();
        }
    }

    private void doAuthorize() {
        ServerContext.setAuthorizeFlag(false);
        String url = "http://115.29.77.191/AuthorizeServer/RszxCtpFuturesServlet?exec=authorize";
        HashMap header = new HashMap();
        byte[] bytes = HttpClientUtil.createHttpRequest(url, header);
        if (bytes == null || bytes.length == 0) {
            return;
        }
        try {
            String contents = new String(bytes, "UTF-8");
            String status = contents;
            if (status.equals("1")) {
                ServerContext.setAuthorizeFlag(true);
            } else {
                ServerContext.setAuthorizeFlag(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doMinuteClear() {
        MasterData masterData = null;
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();
            masterData = (MasterData) entryset.getValue();
            if (masterData.exchCode.equals(exchangeData.code)) {
                ServerContext.getMinuteMap().remove(masterData.symbol);
            }
        }
    }

    public void doSnapshotClear() {
        MasterData masterData = null;
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();
            masterData = (MasterData) entryset.getValue();
            if (masterData.exchCode.equals(exchangeData.code)) {
                ServerContext.getSnapshotMap().remove(masterData.symbol);
            }
        }
    }
    
    //清除排序字典
    public void doSortMapClear(){
        
    }
    
    //清楚股票数据集
    public void doStockClear(){
        
    }
    
    private void doFinanceUpdate(){
        FinanceDao financeDao = new FinanceDao();
        financeDao.setConnector(connector);
        Map<String, FinanceData> financeMap = ServerContext.getFinanceMap();
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();
            FinanceData financeData = financeDao.getBySymbol(symbol);
            financeMap.put(symbol, financeData);
        }
        System.out.println("更新财务数据");
    }
}