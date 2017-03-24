/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.snapshot;

/**
 *
 * @author ok
 */
public class BidAsk {

    public double price;
    public int volume;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public BidAsk clone() {
        BidAsk bidAsk = new BidAsk();
        bidAsk.setPrice(price);
        bidAsk.setVolume(volume);
        return bidAsk;
    }
}
