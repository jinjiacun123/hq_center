/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.kline;

import com.otod.util.IndicatorUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class KLineQueue {

    private String symbol;
    private List<KLineData> queue = new ArrayList<KLineData>();
 
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<KLineData> getQueue() {
        return queue;
    }

    public void setQueue(List<KLineData> queue) {
        this.queue = queue;
    }
}
