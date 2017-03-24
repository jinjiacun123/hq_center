/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.thread;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.master.MasterData;
import com.otod.server.dao.ExchangeCloseDao;
import com.otod.server.dao.ExchangeOpenDao;


/**
 *
 * @author Administrator
 */
public class SignalHandleThread extends Thread {

    @Override
    public void run() {
        Object obj = null;
        while (true) {
            try {
                obj = ServerContext.getSignalQueue().take();
                if (obj instanceof MasterData) {
                    doMaster((MasterData) obj);
                } else if (obj instanceof ExchangeData) {
                    doExchange((ExchangeData) obj);
                }
            } catch (InterruptedException ex) {
                System.out.println("error:" + ((MasterData) obj).symbol);
                ex.printStackTrace();
            }
        }
    }

    public void doMaster(MasterData masterData) {
       
    }

    public void doExchange(ExchangeData exchangeData) {
        ExchangeCloseDao exchangeCloseDao = null;
        ExchangeOpenDao exchangeOpenDao = null;
        if (exchangeData.signalType == ExchangeData.OpenSignal) {
            exchangeOpenDao = new ExchangeOpenDao();
            exchangeOpenDao.doWork(exchangeData);
        } else if (exchangeData.signalType == ExchangeData.CloseSignal) {
            exchangeCloseDao = new ExchangeCloseDao();
            exchangeCloseDao.doWork(exchangeData);
        }
    }
}
