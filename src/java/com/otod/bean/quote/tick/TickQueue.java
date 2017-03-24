/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.tick;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TickQueue {

    private String symbol;
    private double pClose;
    private ArrayList<TickData> queue = new ArrayList<TickData>();

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getpClose() {
        return pClose;
    }

    public void setpClose(double pClose) {
        this.pClose = pClose;
    }

 

  

    public ArrayList<TickData> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<TickData> queue) {
        this.queue = queue;
    }

    
    @Override
    public TickQueue clone() {
        TickQueue tickQueue = new TickQueue();
        tickQueue.symbol = symbol;
        tickQueue.pClose = pClose;
        tickQueue.queue = (ArrayList<TickData>) queue.clone();
        return tickQueue;
    }


}
