/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.master;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.tradetime.TimeNode;
import com.otod.util.ApplicationConstant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class MasterData {

    public String symbol;//内码
    public String outSymbol;//外码
    public String cnName = "";//中文名称
    public String exchCode = "";//交易所代码
    public String stockType = ApplicationConstant.Futures;
    public String keyStr;//查找关键字
    public int tradeDate;//交易日期
    public String tradeTimeKey;
    public List<TimeNode> tradeTimes;//交易时间
    
    public int openTime;
    public int closeTime;
    public boolean isTrade = false;
    public int signalType = -1;//运行信号
    public int decimal = 2;  //保留小数位

    public void updateMasterData(MasterData masterData) {
        if (masterData.cnName != null && !masterData.cnName.equals("")) {
            cnName = masterData.cnName;
        }

        if (masterData.keyStr != null && !masterData.keyStr.equals("")) {
            keyStr = masterData.keyStr;
        }



    }

    public static void updateMasterDataByExchange(ExchangeData exchangeData) {
        MasterData masterData;
        List<TimeNode> tradeTime = null;
        for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
            String symbol = (String) entryset.getKey();
            masterData = (MasterData) entryset.getValue();
            if (masterData.exchCode.equals(exchangeData.code)) {
                if (exchangeData.openTime >= 0 && exchangeData.closeTime >= 0) {
                    tradeTime = ServerContext.getTradeTimeMap().get(exchangeData.tradeTimeKey);
                    masterData.tradeDate = exchangeData.tradeDate;
                    masterData.isTrade = exchangeData.isTrade;
                    masterData.tradeTimeKey = exchangeData.tradeTimeKey;
                    masterData.tradeTimes = tradeTime;
                }

            }
        }

    }

    public static void removeMasterDataByExchange(ExchangeData exchagneData) {
        MasterData masterData;
        Iterator it = ServerContext.getMasterMap().entrySet().iterator();
        ArrayList<MasterData> removeList = new ArrayList<MasterData>();
        while (it.hasNext()) {
            Map.Entry<String, MasterData> entryset = (Map.Entry<String, MasterData>) it.next();
            String symbol = (String) entryset.getKey();
            masterData = (MasterData) entryset.getValue();
            if (masterData.exchCode.equals(exchagneData.code)) {
                removeList.add(masterData);
            }
        }
        for (int i = 0; i < removeList.size(); i++) {
            masterData = removeList.get(i);
            ServerContext.getMasterMap().remove(masterData.symbol);
        }
    }
}
