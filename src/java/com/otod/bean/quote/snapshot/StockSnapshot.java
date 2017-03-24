/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;

/**
 *
 * @author Administrator
 */
public class StockSnapshot extends Snapshot {

    @Override
    public void updateSnapshot(Snapshot snapshot) {
        StockSnapshot sSnapshot = (StockSnapshot) snapshot;
        symbol = sSnapshot.symbol;
        tradeDate = snapshot.tradeDate;
        quoteDate = sSnapshot.quoteDate;
        quoteTime = sSnapshot.quoteTime;

        this.openPrice = sSnapshot.openPrice;
        this.highPrice = sSnapshot.highPrice;
        this.lowPrice = sSnapshot.lowPrice;
        this.lastPrice = sSnapshot.lastPrice;

        this.pClose = sSnapshot.pClose;

        if (this.lastPrice != 0) {
            this.change = lastPrice - pClose;
            this.changeRate = change / this.pClose * 100;
            if (Double.isInfinite(changeRate) || Double.isNaN(changeRate)) {
                changeRate = 0;
            }
        }
        if (volume != 0) {
            lastVolume = (int) (sSnapshot.volume - volume);
        }
        if (turnover != 0) {
            lastTurnover = (int) (sSnapshot.turnover - turnover);
        }

        volume = sSnapshot.volume;
        turnover = sSnapshot.turnover;
        bidQueue = sSnapshot.bidQueue;
        askQueue = sSnapshot.askQueue;
        if (sSnapshot.bidQueue.size() >= 5) {
            this.bid1Price = sSnapshot.bidQueue.get(0).price;
        }
        if (sSnapshot.askQueue.size() >= 5) {
            this.ask1Price = sSnapshot.askQueue.get(4).price;
        }

    }

    public void updateTempSnapshot(Snapshot snapshot) {
        StockSnapshot sSnapshot = (StockSnapshot) snapshot;
        this.openPrice = sSnapshot.openPrice;
        this.highPrice = sSnapshot.highPrice;
        this.lowPrice = sSnapshot.lowPrice;
        this.lastPrice = sSnapshot.lastPrice;
        this.pClose = sSnapshot.pClose;
        volume = sSnapshot.volume;
        turnover = sSnapshot.turnover;
        bidQueue = sSnapshot.bidQueue;
        askQueue = sSnapshot.askQueue;
    }

    public boolean equalsTemp(Object obj) {
        if (obj == null) {
            return false;
        }
        final Snapshot other = (Snapshot) obj;

        if (Double.doubleToLongBits(this.openPrice) != Double.doubleToLongBits(other.openPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.highPrice) != Double.doubleToLongBits(other.highPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lowPrice) != Double.doubleToLongBits(other.lowPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lastPrice) != Double.doubleToLongBits(other.lastPrice)) {
            return false;
        }

        if (Double.doubleToLongBits(this.pClose) != Double.doubleToLongBits(other.pClose)) {
            return false;
        }

        if (Double.doubleToLongBits(this.volume) != Double.doubleToLongBits(other.volume)) {
            return false;
        }

        if (Double.doubleToLongBits(this.turnover) != Double.doubleToLongBits(other.turnover)) {
            return false;
        }
        for (int i = 0; i < this.bidQueue.size(); i++) {
            if (bidQueue.get(i).price != other.bidQueue.get(i).price) {
                return false;
            }
            if (bidQueue.get(i).volume != other.bidQueue.get(i).volume) {
                return false;
            }
        }
        for (int i = 0; i < this.askQueue.size(); i++) {
            if (askQueue.get(i).price != other.askQueue.get(i).price) {
                return false;
            }
            if (askQueue.get(i).volume != other.askQueue.get(i).volume) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Snapshot other = (Snapshot) obj;
        if ((this.symbol == null) ? (other.symbol != null) : !this.symbol.equals(other.symbol)) {
            return false;
        }
        if ((this.cnName == null) ? (other.cnName != null) : !this.cnName.equals(other.cnName)) {
            return false;
        }
        if ((this.exchCode == null) ? (other.exchCode != null) : !this.exchCode.equals(other.exchCode)) {
            return false;
        }
        if (this.decimal != other.decimal) {
            return false;
        }
        if (this.tradeDate != other.tradeDate) {
            return false;
        }

        if (Double.doubleToLongBits(this.openPrice) != Double.doubleToLongBits(other.openPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.highPrice) != Double.doubleToLongBits(other.highPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lowPrice) != Double.doubleToLongBits(other.lowPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lastPrice) != Double.doubleToLongBits(other.lastPrice)) {
            return false;
        }
        if (Double.doubleToLongBits(this.change) != Double.doubleToLongBits(other.change)) {
            return false;
        }
        if (Double.doubleToLongBits(this.changeRate) != Double.doubleToLongBits(other.changeRate)) {
            return false;
        }
        if (Double.doubleToLongBits(this.pClose) != Double.doubleToLongBits(other.pClose)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lastVolume) != Double.doubleToLongBits(other.lastVolume)) {
            return false;
        }
        if (Double.doubleToLongBits(this.volume) != Double.doubleToLongBits(other.volume)) {
            return false;
        }
        if (Double.doubleToLongBits(this.lastTurnover) != Double.doubleToLongBits(other.lastTurnover)) {
            return false;
        }
        if (Double.doubleToLongBits(this.turnover) != Double.doubleToLongBits(other.turnover)) {
            return false;
        }
        if (this.bidQueue != other.bidQueue && (this.bidQueue == null || !this.bidQueue.equals(other.bidQueue))) {
            return false;
        }
        if (this.askQueue != other.askQueue && (this.askQueue == null || !this.askQueue.equals(other.askQueue))) {
            return false;
        }
        if (Double.doubleToLongBits(this.bid1Price) != Double.doubleToLongBits(other.bid1Price)) {
            return false;
        }
        if (this.bid1Volume != other.bid1Volume) {
            return false;
        }
        if (Double.doubleToLongBits(this.ask1Price) != Double.doubleToLongBits(other.ask1Price)) {
            return false;
        }
        if (this.ask1Volume != other.ask1Volume) {
            return false;
        }
        for (int i = 0; i < this.bidQueue.size(); i++) {
            if (bidQueue.get(i).price != other.bidQueue.get(i).price) {
                return false;
            }
            if (bidQueue.get(i).volume != other.bidQueue.get(i).volume) {
                return false;
            }
        }
        for (int i = 0; i < this.askQueue.size(); i++) {
            if (askQueue.get(i).price != other.askQueue.get(i).price) {
                return false;
            }
            if (askQueue.get(i).volume != other.askQueue.get(i).volume) {
                return false;
            }

        }
        return true;
    }

    @Override
    public StockSnapshot clone() {
        StockSnapshot quote = new StockSnapshot();
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
        quote.bidQueue = bidQueue;
        quote.askQueue = askQueue;
        quote.bid1Price = bid1Price;
        quote.bid1Volume = bid1Volume;
        quote.ask1Price = ask1Price;
        quote.ask1Volume = ask1Volume;
        return quote;
    }
}
