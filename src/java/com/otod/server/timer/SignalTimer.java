/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.timer;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.master.MasterData;
import com.otod.util.mutithread.WorkHandle;
import com.otod.util.DateUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class SignalTimer extends WorkHandle {

    private URL url = null;
    private String filePath = null;

    public SignalTimer() {
      
    }

    @Override
    public void doWork() {
        int hm = Integer.parseInt(DateUtil.formatDate(null, "HHmm"));

        if (hm == 600) {
            //filePath = "src/java";
//            ArrayList<Integer> holidayList = readHolidayList(filePath + "/cn_holiday.txt");
//
//            ExchangeData shExchange = ServerContext.getExchangeMap().get("SH");
//            shExchange.holidayList = holidayList;
//
//            ExchangeData szExchange = ServerContext.getExchangeMap().get("SZ");
//            szExchange.holidayList = holidayList;

        }
        doExchangeSignal();
        
    }


    public void doExchangeSignal() {
        int hm = Integer.parseInt(DateUtil.formatDate(null, "HHmm"));
        int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));

        ExchangeData exchangeData = null;
        for (Map.Entry<String, ExchangeData> entryset : ServerContext.getExchangeMap().entrySet()) {
            String code = (String) entryset.getKey();
            exchangeData = (ExchangeData) entryset.getValue();

            if (exchangeData.holidayList.isEmpty() && exchangeData.isTrade) {
                if (exchangeData.closeTime >= 0 && exchangeData.closeTime == hm && exchangeData.signalType != ExchangeData.CloseSignal) {
                    exchangeData.signalType = ExchangeData.CloseSignal;
                    ServerContext.getSignalQueue().add(exchangeData);
                }
            } else if (exchangeData.holidayList.size() > 0 && !exchangeData.isHoliday(new Date())) {
                if (exchangeData.openTime >= 0 && exchangeData.openTime == hm && exchangeData.signalType != ExchangeData.OpenSignal) {

                    exchangeData.signalType = ExchangeData.OpenSignal;
                    ServerContext.getSignalQueue().add(exchangeData);
                }
                if (exchangeData.closeTime >= 0 && exchangeData.closeTime == hm && exchangeData.isTrade == true && exchangeData.signalType != ExchangeData.CloseSignal) {

                    exchangeData.signalType = ExchangeData.CloseSignal;
                    ServerContext.getSignalQueue().add(exchangeData);
                }
            }
        }
    }

    private ArrayList<Integer> readHolidayList(String filePath) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "GBK"));
            String str = null;
            int date = 0;
            while ((str = br.readLine()) != null) {
                if (!str.startsWith("#")) {
                    date = Integer.parseInt(str.replace("-", ""));
                    list.add(date);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

//        @Override
//    public void doWork() {
//        int hm = Integer.parseInt(DateUtil.formatDate(null, "HHmm"));
//        int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));
//        String filePath = url.getPath();
//        ExchangeData exchangeData = null;
//
//        //if (hm == 600) 
//        {
//            // filePath = "src/java";
//
//
//
//            ArrayList<Integer> holidayList = getHolidayList(filePath + "/cn_holiday.txt");
//            exchangeData = ShareModel.getExchangeMap().get("SH");
//            exchangeData.holidayList = holidayList;
//        }
//
//        Iterator it = ShareModel.getExchangeMap().entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, ExchangeData> entryset = (Map.Entry<String, ExchangeData>) it.next();
//            String code = (String) entryset.getKey();
//            exchangeData = (ExchangeData) entryset.getValue();
//
//            if (exchangeData.openTime == hm) {
//                if (!exchangeData.isHoliday(new Date())) {
//                    exchangeData.quoteDate = date;
//                    ExchangeData temp = exchangeData.clone();
//                    temp.signalType = ExchangeData.OpenSignal;
//                    ShareModel.getSignalQueue().add(temp);
//                }
//
//            } else if (exchangeData.closeTime == hm) {
//                //if (!exchangeData.isHoliday(new Date())) 
//                {
//                    ExchangeData temp = exchangeData.clone();
//                    temp.signalType = ExchangeData.CloseSignal;
//                    ShareModel.getSignalQueue().add(temp);
//                }
//            }
//        }
//    }
    private ArrayList<Integer> getHolidayList(String filePath) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "GBK"));
            String str = null;
            int date = 0;
            while ((str = br.readLine()) != null) {
                if (!str.startsWith("#")) {
                    date = Integer.parseInt(str.replace("-", ""));
                    list.add(date);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
