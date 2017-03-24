/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.minute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author Administrator
 */
public class MinuteQueue {

    private String symbol;
    private double pClose;
    private List<MinuteData> queue = new ArrayList<MinuteData>();

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<MinuteData> getQueue() {
        return queue;
    }

    public void setQueue(List<MinuteData> queue) {
        this.queue = queue;
    }

    public double getpClose() {
        return pClose;
    }

    public void setpClose(double pClose) {
        this.pClose = pClose;
    }
}
