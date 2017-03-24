/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.exchange;

import com.otod.bean.quote.tradetime.TimeNode;
import com.otod.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class ExchangeData {

    public String code;
    public String cnName;
    public int tradeDate;
    public int openTime = -1;
    public int closeTime = -1;
    public List<TimeNode> tradeTimes;//交易时间
    public String tradeTimeKey;
    public boolean isTrade = false;
    public static int OpenSignal = 0;//开盘信号
    public static int CloseSignal = 1;//收盘信号
    public int signalType = -1;//运行信号
    public ArrayList<Integer> holidayList = new ArrayList<Integer>();
    

    @Override
    public ExchangeData clone() {
        ExchangeData exchangeData = new ExchangeData();
        exchangeData.code = this.code;
        exchangeData.cnName = this.cnName;
        exchangeData.tradeDate = this.tradeDate;
        exchangeData.openTime = this.openTime;
        exchangeData.closeTime = this.closeTime;
        exchangeData.tradeTimes = this.tradeTimes;
        exchangeData.holidayList = (ArrayList<Integer>) this.holidayList.clone();
        return exchangeData;
    }

    public boolean isHoliday(Date date) {
        int week = DateUtil.getDayOfWeek(date);
        if (week == 1 || week == 7) {
            return true;
        }
        //System.out.println(DateUtil.formatDate(date, "yyyyMMdd"));
        int idx = holidayList.indexOf(Integer.parseInt(DateUtil.formatDate(date, "yyyyMMdd")));
        if (idx >= 0) {
            return true;
        }

        return false;
    }
}
