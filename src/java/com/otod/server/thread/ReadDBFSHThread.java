/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.thread;

import com.linuxense.javadbf.DBFReader;
import com.otod.bean.ServerContext;
import com.otod.bean.quote.StockDividend;
import com.otod.bean.quote.finance.FinanceData;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.snapshot.BidAsk;
import com.otod.bean.quote.snapshot.Snapshot;
import com.otod.bean.quote.snapshot.StockSnapshot;
import com.otod.util.Config;
import com.otod.util.DateUtil;
import com.otod.util.Help;
import com.otod.util.StringUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Administrator
 */
public class ReadDBFSHThread extends Thread {

    @Override
    public void run() {
        while (true) {
            Date start=new Date();
            doReadDBF();
            Date end=new Date();
           // System.out.println("SH "+(end.getTime()-start.getTime()));
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReadDBFSHThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void doReadDBF() {
        String path = Config.DBF_PATH 
                    + Config.FILE_SPLITE 
                    + Config.DBF_SH;
        //System.out.println(path);
        InputStream is = null;
        ByteBuffer buffer = null;
        byte[] temp = null;

        try {
            File file = new File(path);
            if (file.exists() && file.isFile() && file.length() > 0 && file.canRead()) {
                buffer = ByteBuffer.allocate((int) file.length());
                is = new FileInputStream(path);
                int count;
                //buffer.order(ByteOrder.LITTLE_ENDIAN);
                byte datas[] = new byte[1024 * 20];
                while ((count = is.read(datas)) != -1) {
                    buffer.put(datas, 0, count);
                }
                datas = null;
            }

            temp = new byte[buffer.position()];
            buffer.flip();
            buffer.get(temp);
        } catch (Exception ex) {
//            System.out.println(ex.toString());
        } finally {
            if (buffer != null) {
                buffer.clear();                             
            }
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
            }
        }

        if (temp == null) {
            return;
        }
        try {
            // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
            DBFReader reader = new DBFReader(new ByteArrayInputStream(temp));
            reader.setCharactersetName("GBK");
            Object[] rowValues;

            int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));
            int time =  Integer.parseInt(DateUtil.formatDate(null, "HHmmss"));
            double pClose,      lastPrice, lowPrice, hightPrice,volume;
            double change_rate, change;
            double ltag;
            double earning;
            double open;
            DecimalFormat df = new DecimalFormat("#.00000");  
            
            Map<String,Map<String, Double>> marketList = ServerContext.getMarketList();
            String marketName = "";
            
            Map<String, Double> listSortMMap            = null;//成交额
            Map<String, Double> listSortRaiseMap        = null;//涨跌幅
            Map<String, Double> listSortUpdownMap        = null;//涨跌幅
            Map<String, Double> listSortAmplitudeMap    = null;//振幅
            Map<String, Double> listSortTurnoverRateMap = null;//换手率
            Map<String, Double> listSortEarmingMap      = null;//市盈率
            Map<String, FinanceData> financeMap  = ServerContext.getFinanceMap();
            FinanceData financeData = null;
            
           // int i = 0;
            while ((rowValues = reader.nextRecord()) != null) {
             //   i++;
               // if(i>2)
               //     break;
                if (rowValues[0] == null) {
                    continue;
                }
                String symbol = String.valueOf(rowValues[0]).trim();
                if (symbol.equals("") || symbol.length() < 6) {
                    continue;
                }
                
                //市场查找
                symbol = "SH" + symbol;
                marketName = Help.findMarketByCode(Config.TYPE_SH, symbol);
                if(marketName != ""){
                     //获取市场对应排序
                    listSortMMap            = marketList.get(marketName+"_SORT_M");//成交额
                    listSortUpdownMap       = marketList.get(marketName+"_SORT_UPDOWN");//涨跌额
                    listSortRaiseMap        = marketList.get(marketName+"_SORT_RAISE");//涨跌幅
                    listSortAmplitudeMap    = marketList.get(marketName+"_SORT_AMPLITUDE");//振幅
                    listSortTurnoverRateMap = marketList.get(marketName+"_SORT_TURNOVERRATE");//换手率
                    listSortEarmingMap      = marketList.get(marketName+"_SORT_EARMING");//市盈率
                }
                
                MasterData masterData = ServerContext.getMasterMap().get(symbol);
                if (masterData == null) {
                    continue;
                }
                
                StockSnapshot stockSnapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(symbol);
                if(stockSnapshot == null)
                    stockSnapshot = new StockSnapshot();
                stockSnapshot.setSymbol(symbol);
                stockSnapshot.setCnName(String.valueOf(rowValues[1]).trim());
                stockSnapshot.setQuoteDate(date);
                stockSnapshot.setQuoteTime(time);
                stockSnapshot.setOpenPrice(Double.parseDouble(String.valueOf(rowValues[3]).trim()));
                stockSnapshot.setHighPrice(Double.parseDouble(String.valueOf(rowValues[5]).trim()));
                stockSnapshot.setLowPrice(Double.parseDouble(String.valueOf(rowValues[6]).trim()));
                stockSnapshot.setLastPrice(Double.parseDouble(String.valueOf(rowValues[7]).trim()));//+new Random().nextInt(10) + 1
                stockSnapshot.setpClose(Double.parseDouble(String.valueOf(rowValues[2]).trim()));
                stockSnapshot.setVolume(getVolume(String.valueOf(rowValues[10]).trim()));
                stockSnapshot.setTurnover(Double.parseDouble(String.valueOf(rowValues[4]).trim()));
                open = Double.parseDouble(String.valueOf(rowValues[3]).trim());
               
                //new add
                if(!marketName.equals("") && (open != 0)){
                    try{
                        listSortMMap.put(symbol, Double.parseDouble(String.valueOf(rowValues[4]).trim()));                        
                        pClose = Double.parseDouble(String.valueOf(rowValues[2]).trim());
                        lastPrice = Double.parseDouble(String.valueOf(rowValues[7]).trim());
                        lowPrice = Double.parseDouble(String.valueOf(rowValues[6]).trim());
                        hightPrice = Double.parseDouble(String.valueOf(rowValues[5]).trim());
                        volume = Double.parseDouble(String.valueOf(rowValues[10]).trim());
                        if(lastPrice != 0){
                            change_rate = 100*(lastPrice-pClose)/(double)pClose;
                            change_rate = Double.parseDouble(df.format(change_rate));
                        }
                        else{
                            change_rate = 0.00;
                        }
                        listSortUpdownMap.put(symbol, lastPrice-pClose);
                        listSortRaiseMap.put(symbol, change_rate);
                        stockSnapshot.setChange(lastPrice-pClose);
                        stockSnapshot.setChangeRate(change_rate);
                        Help.countPlat(symbol, marketName, (lastPrice-pClose)/(double)pClose);
                       // Help.checkIsDayRaiseFallStop(symbol, (lastPrice-pClose)/(double)pClose);
                       if((hightPrice - lowPrice) != 0)
                        listSortAmplitudeMap.put(symbol, (hightPrice - lowPrice)/(double)pClose);
                       else{
                           listSortAmplitudeMap.put(symbol, 0.00);
                       }
                        financeData = (FinanceData)financeMap.get(symbol);
                        if(financeData != null && financeData.getSymbol() != null){                           
                            if(financeData.getLtag() != 0){
                                ltag = volume/financeData.getLtag();
                                stockSnapshot.setTurnoverRate(Double.parseDouble(StringUtil.formatNumber(ltag,5)));
                                listSortTurnoverRateMap.put(symbol, Double.parseDouble(StringUtil.formatNumber(ltag,5)));
                            }
                            if(financeData.getJly()!= 0){
                                earning = stockSnapshot.getLastPrice()/(financeData.getJly()/financeData.getZgb());
                                stockSnapshot.setEarming(Double.parseDouble(StringUtil.formatNumber(earning,5)));
                                listSortEarmingMap.put(symbol,Double.parseDouble(StringUtil.formatNumber(earning,5)));
                            }
                        }
                    }
                    catch(Exception ex){
                        System.out.println("err:"+String.valueOf(rowValues[4]).trim());
                    }
                }
                
                /*
                BidAsk bid1 = null;
                if(stockSnapshot.getBidQueue().size() == 0){
                    bid1 = new BidAsk();
                }else{
                    bid1 = (BidAsk)stockSnapshot.getBidQueue().get(0);
                }
                bid1.setPrice(Double.parseDouble(String.valueOf(rowValues[8]).trim()));
                bid1.setVolume(getVolume(String.valueOf(rowValues[12]).trim()));                
                stockSnapshot.getBidQueue().add(bid1);
                BidAsk ask1 = null;
                if(stockSnapshot.getAskQueue().size() == 0){
                    ask1 = new BidAsk();
                }
                else{
                    ask1 = (BidAsk)stockSnapshot.getAskQueue().get(0);
                }
                ask1.setPrice(Double.parseDouble(String.valueOf(rowValues[9]).trim()));
                ask1.setVolume(getVolume(String.valueOf(rowValues[17]).trim()));
                stockSnapshot.getAskQueue().add(0, ask1);
                BidAsk bid2 = null;
                if(stockSnapshot.getBidQueue().size() == 1){
                    bid2 = new BidAsk();
                }else{
                   bid2 = (BidAsk)stockSnapshot.getBidQueue().get(1);
                }
                bid2.setPrice(Double.parseDouble(String.valueOf(rowValues[13]).trim()));
                bid2.setVolume(getVolume(String.valueOf(rowValues[14]).trim()));
                stockSnapshot.getBidQueue().add(bid2);
                BidAsk ask2 = null;
                if(stockSnapshot.getAskQueue().size() == 1){
                    ask2 = new BidAsk();
                }else{
                    ask2 = (BidAsk)stockSnapshot.getAskQueue().get(1);
                }
                ask2.setPrice(Double.parseDouble(String.valueOf(rowValues[18]).trim()));
                ask2.setVolume(getVolume(String.valueOf(rowValues[19]).trim()));
                stockSnapshot.getAskQueue().add(0, ask2);
                BidAsk bid3 = null;
                if(stockSnapshot.getBidQueue().size() == 2){
                    bid3 = new BidAsk();
                }else{
                    bid3 = (BidAsk)stockSnapshot.getBidQueue().get(2);
                }
                bid3.setPrice(Double.parseDouble(String.valueOf(rowValues[15]).trim()));
                bid3.setVolume(getVolume(String.valueOf(rowValues[16]).trim()));
                stockSnapshot.getBidQueue().add(bid3);
                BidAsk ask3 = null;
                if(stockSnapshot.getAskQueue().size() == 2){
                    ask3 = new BidAsk();
                }else{
                    ask3 = (BidAsk)stockSnapshot.getAskQueue().get(2);
                }
                ask3.setPrice(Double.parseDouble(String.valueOf(rowValues[20]).trim()));
                ask3.setVolume(getVolume(String.valueOf(rowValues[21]).trim()));
                stockSnapshot.getAskQueue().add(0, ask3);
                BidAsk bid4 = null;
                if(stockSnapshot.getBidQueue().size() == 3){
                    bid4 = new BidAsk();
                }else{
                    bid4 = (BidAsk)stockSnapshot.getBidQueue().get(3);
                }
                bid4.setPrice(Double.parseDouble(String.valueOf(rowValues[22]).trim()));
                bid4.setVolume(getVolume(String.valueOf(rowValues[23]).trim()));
                stockSnapshot.getBidQueue().add(bid4);
                BidAsk ask4 = null;
                if(stockSnapshot.getAskQueue().size() == 3){
                    ask4 = new BidAsk();
                }else{
                    ask4 = (BidAsk)stockSnapshot.getAskQueue().get(3);
                }
                ask4.setPrice(Double.parseDouble(String.valueOf(rowValues[26]).trim()));
                ask4.setVolume(getVolume(String.valueOf(rowValues[27]).trim()));
                stockSnapshot.getAskQueue().add(0, ask4);                
                BidAsk bid5 = null;
                if(stockSnapshot.getBidQueue().size() == 4){
                    bid5 = new BidAsk();
                }else{
                    bid5 = (BidAsk)stockSnapshot.getBidQueue().get(4);
                }
                bid5.setPrice(Double.parseDouble(String.valueOf(rowValues[24]).trim()));
                bid5.setVolume(getVolume(String.valueOf(rowValues[25]).trim()));
                stockSnapshot.getBidQueue().add(bid5);
                BidAsk ask5 = null;
                if(stockSnapshot.getAskQueue().size() == 4){
                    ask5 = new BidAsk();
                }else{
                    ask5 = (BidAsk)stockSnapshot.getAskQueue().get(4);
                }
                ask5.setPrice(Double.parseDouble(String.valueOf(rowValues[28]).trim()));
                ask5.setVolume(getVolume(String.valueOf(rowValues[29]).trim()));
                stockSnapshot.getAskQueue().add(0, ask5);
                */
               
                StockSnapshot hSnapshot = (StockSnapshot) ServerContext.getTempSnapshotMap().get(symbol);//品种为健值的基本数据体
                /**
                 * 按照品种，进行分别操作：
                 * 1.不存在，则添加，作为基本数据;
                 * 2.存在，如果不相同，进行更新临时，添加实时队列
                 */
                if (hSnapshot == null) {
                   // ServerContext.getRtSnapshotQueue().add(stockSnapshot.clone());
                    ServerContext.getSnapshotMap().put(stockSnapshot.symbol, stockSnapshot);
                    ServerContext.getTempSnapshotMap().put(stockSnapshot.symbol, stockSnapshot);
                } else {
                    if (!hSnapshot.equalsTemp(stockSnapshot)) {
                        hSnapshot.updateTempSnapshot(stockSnapshot);
                        hSnapshot.updateSnapshot(hSnapshot);
                      //  ServerContext.getRtSnapshotQueue().add(stockSnapshot);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            temp = null;
            System.gc();
        }
    }

    public int getVolume(String str) {
        int volume = 0;
        volume = (int) (Double.parseDouble(str) / 100);
        return volume;
    }
}
