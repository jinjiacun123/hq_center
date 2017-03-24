/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;

import com.otod.util.IndicatorUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class SnapshotQueue {

    private String symbol;
    private List<Snapshot> queue = new ArrayList<Snapshot>();
 
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Snapshot> getQueue() {
        return queue;
    }

    public void setQueue(List<Snapshot> queue) {
        this.queue = queue;
    }
}
