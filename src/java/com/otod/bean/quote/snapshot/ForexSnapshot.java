/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.master.MasterData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ForexSnapshot extends Snapshot {

    @Override
    public void updateSnapshot(Snapshot quote) {
        MasterData masterData = ServerContext.getMasterMap().get(quote.getSymbol());
        ForexSnapshot sSnapshot = (ForexSnapshot) quote;
        this.symbol = sSnapshot.symbol;
        this.cnName = masterData.cnName;
        this.exchCode = masterData.exchCode;
        this.tradeDate = masterData.tradeDate;
        this.quoteDate = sSnapshot.quoteDate;
        this.quoteTime = sSnapshot.quoteTime;
        this.lastPrice = sSnapshot.lastPrice;
        this.openPrice = sSnapshot.openPrice;
        this.highPrice = sSnapshot.highPrice;
        this.lowPrice = sSnapshot.lowPrice;
        this.pClose = sSnapshot.pClose;

        if (this.lastPrice != 0) {
            this.change = lastPrice - pClose;
            this.changeRate = change / this.pClose * 100;
            if (Double.isInfinite(changeRate) || Double.isNaN(changeRate)) {
                changeRate = 0;
            }
        }

        this.lastVolume = sSnapshot.lastVolume;
        this.lastTurnover = sSnapshot.lastTurnover;
        this.volume += sSnapshot.lastVolume;
        this.turnover += sSnapshot.lastTurnover;
        this.bid1Price = sSnapshot.bid1Price;
        this.bid1Volume = sSnapshot.bid1Volume;
        this.ask1Price = sSnapshot.ask1Price;
        this.ask1Volume = sSnapshot.ask1Volume;
        this.bidQueue = sSnapshot.bidQueue;
        this.askQueue = sSnapshot.askQueue;
    }

    @Override
    public ForexSnapshot clone() {
        ForexSnapshot quote = new ForexSnapshot();
        quote.symbol = symbol;
        quote.cnName = cnName;
        quote.exchCode = exchCode;
        quote.decimal = decimal;
        quote.tradeDate = tradeDate;
        quote.quoteDate = quoteDate;
        quote.quoteTime = quoteTime;
        quote.openPrice = openPrice;
        quote.highPrice = highPrice;
        quote.lowPrice = lowPrice;
        quote.lastPrice = lastPrice;
        quote.change = change;
        quote.changeRate = changeRate;
        quote.pClose = pClose;
        quote.lastVolume = lastVolume;
        quote.volume = volume;
        quote.lastTurnover = lastTurnover;
        quote.turnover = turnover;
        List<BidAsk> bQueue = new ArrayList<BidAsk>();//买排队
        List<BidAsk> aQueue = new ArrayList<BidAsk>();//卖排队
        for (int i = 0; i < bidQueue.size(); i++) {
            BidAsk bid = bidQueue.get(i);
            bQueue.add(bid.clone());
        }
        for (int i = 0; i < askQueue.size(); i++) {
            BidAsk ask = askQueue.get(i);
            aQueue.add(ask.clone());
        }
        quote.bidQueue = bQueue;
        quote.askQueue = aQueue;
        quote.bid1Price = bid1Price;
        quote.bid1Volume = bid1Volume;
        quote.ask1Price = ask1Price;
        quote.ask1Volume = ask1Volume;

        return quote;
    }
}