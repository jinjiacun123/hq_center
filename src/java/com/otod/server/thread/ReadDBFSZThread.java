/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.thread;

import com.linuxense.javadbf.DBFReader;
import com.otod.bean.ServerContext;
import com.otod.bean.quote.finance.FinanceData;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.snapshot.BidAsk;
import com.otod.bean.quote.snapshot.Snapshot;
import com.otod.bean.quote.snapshot.StockSnapshot;
import com.otod.util.Config;
import com.otod.util.DateUtil;
import com.otod.util.Help;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ReadDBFSZThread extends Thread {

    @Override
    public void run() {
        while (true) {
            doReadDBF();
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
                    + Config.DBF_SZ;
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
                buffer = null;
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
            double pClose, lastPrice, lowPrice, hightPrice,volume;
            double ltag;
            
            Map<String,Map<String, Double>> marketList = ServerContext.getMarketList();
            String marketName = "";
            
            Map<String, Double> listSortMMap            = null;//成交额
            Map<String, Double> listSortRaiseMap        = null;//涨跌幅
            Map<String, Double> listSortUpdownMap       = null;//涨跌额
            Map<String, Double> listSortAmplitudeMap    = null;//振幅
            Map<String, Double> listSortTurnoverRateMap = null;//换手率
            Map<String, Double> listSortEarmingMap      = null;//市盈率
            Map<String, FinanceData> financeMap  = ServerContext.getFinanceMap();
            FinanceData financeData = null;
            while ((rowValues = reader.nextRecord()) != null) {
                if (rowValues[0] == null) {
                    continue;
                }
                String symbol = String.valueOf(rowValues[0]).trim();
                /*
                System.out.println(symbol);
                byte[] b = symbol.getBytes();
                System.out.print(Help.byteToBit(b[0])); System.out.print(" ");
                System.out.print(Help.byteToBit(b[1])); System.out.print(" ");
                System.out.print(Help.byteToBit(b[2])); System.out.print(" ");
                System.out.print(Help.byteToBit(b[3])); System.out.print(" ");
                System.out.print(Help.byteToBit(b[4])); System.out.print(" ");
                System.out.print(Help.byteToBit(b[5])); System.out.print(" ");
                */
                /*
                System.out.println(" ");                
                System.out.print(symbol.length());System.out.println(" ");
                System.out.print(symbol.getBytes().length);System.out.println(" ");
                if(Help.checkHalf(symbol)){
                    System.out.print("true");
                    System.out.println("");
                }
*/
                /*
                if(i> 100){
                    System.out.println(i);
                }
                if(symbol.equals("120001")){
                    System.out.println(symbol);
                }
*/
                if (symbol.equals("") || symbol.length() < 6) {
                    continue;
                }
                symbol = "SZ" + symbol;                
                marketName = Help.findMarketByCode(Config.TYPE_SZ, symbol);
               
                if(marketName != ""){
                     //获取市场对应排序
                    listSortMMap            = marketList.get(marketName+"_SORT_M");//成交额
                    listSortUpdownMap       = marketList.get(marketName+"_SORT_UPDOWN");//涨跌额
                    listSortRaiseMap        = marketList.get(marketName+"_SORT_RAISE");//涨跌幅
                    listSortAmplitudeMap    = marketList.get(marketName+"_SORT_AMPLITUDE");//振幅
                    listSortTurnoverRateMap = marketList.get(marketName+"_SORT_TURNOVERRATE");//换手率
                    listSortEarmingMap      = marketList.get(marketName+"_SORT_EARMING");//市盈率
                }
                /*
                MasterData masterData = ServerContext.getMasterMap().get(symbol);
                if (masterData == null) {
                    continue;
                }
*/
                StockSnapshot stockSnapshot = new StockSnapshot();
                stockSnapshot.setSymbol(symbol);
                stockSnapshot.setCnName(String.valueOf(rowValues[1]).trim());
                stockSnapshot.setQuoteDate(date);
                stockSnapshot.setQuoteTime(time);
                stockSnapshot.setOpenPrice(Double.parseDouble(String.valueOf(rowValues[3]).trim()));
                stockSnapshot.setHighPrice(Double.parseDouble(String.valueOf(rowValues[8]).trim()));
                stockSnapshot.setLowPrice(Double.parseDouble(String.valueOf(rowValues[9]).trim()));
                stockSnapshot.setLastPrice(Double.parseDouble(String.valueOf(rowValues[4]).trim()));
                stockSnapshot.setpClose(Double.parseDouble(String.valueOf(rowValues[2]).trim()));
                stockSnapshot.setVolume(getVolume(String.valueOf(rowValues[5]).trim()));
                stockSnapshot.setTurnover(Double.parseDouble(String.valueOf(rowValues[6]).trim()));
                //new add
                if((!marketName.equals("")&&(!symbol.startsWith("SZ000000")))){
                    try{
                        listSortMMap.put(symbol, Double.parseDouble(String.valueOf(rowValues[4]).trim()));                        
                        pClose = Double.parseDouble(String.valueOf(rowValues[2]).trim());
                        lastPrice = Double.parseDouble(String.valueOf(rowValues[4]).trim());
                        lowPrice = Double.parseDouble(String.valueOf(rowValues[9]).trim());
                        hightPrice = Double.parseDouble(String.valueOf(rowValues[8]).trim());
                        volume = Double.parseDouble(String.valueOf(rowValues[5]).trim());
                        listSortRaiseMap.put(symbol, (lastPrice-pClose)/lastPrice);
                        listSortUpdownMap.put(symbol, lastPrice-pClose);
                        Help.checkIsDayRaiseFallStop(symbol, (lastPrice-pClose)/lastPrice);
                        listSortAmplitudeMap.put(symbol, (hightPrice - lowPrice)/lowPrice);
                        financeData = (FinanceData)financeMap.get(symbol);
                        if(financeData != null && financeData.getGxrq() != 0){
                            //listSortTurnoverRateMap.put(symbol, volume/5);
                            //System.out.println(financeData.getLtag());
                            stockSnapshot.setTurnoverRate(volume/financeData.getLtag());
                            listSortTurnoverRateMap.put(symbol, volume/financeData.getLtag());
                            stockSnapshot.setEarming(stockSnapshot.getLastPrice()/financeData.getShly());
                            listSortEarmingMap.put(symbol, stockSnapshot.getLastPrice()/financeData.getShly());
                        }
                    }
                    catch(Exception ex){
                        //System.out.println("err:"+String.valueOf(rowValues[4]).trim());
                    }
                }
                BidAsk ask1 = new BidAsk();
                ask1.setPrice(Double.parseDouble(String.valueOf(rowValues[15]).trim()));
                ask1.setVolume(getVolume(String.valueOf(rowValues[16]).trim()));
                stockSnapshot.getAskQueue().add(0, ask1);
                BidAsk ask2 = new BidAsk();
                ask2.setPrice(Double.parseDouble(String.valueOf(rowValues[17]).trim()));
                ask2.setVolume(getVolume(String.valueOf(rowValues[18]).trim()));
                stockSnapshot.getAskQueue().add(0, ask2);
                BidAsk ask3 = new BidAsk();
                ask3.setPrice(Double.parseDouble(String.valueOf(rowValues[19]).trim()));
                ask3.setVolume(getVolume(String.valueOf(rowValues[20]).trim()));
                stockSnapshot.getAskQueue().add(0, ask3);
                BidAsk ask4 = new BidAsk();
                ask4.setPrice(Double.parseDouble(String.valueOf(rowValues[21]).trim()));
                ask4.setVolume(getVolume(String.valueOf(rowValues[22]).trim()));
                stockSnapshot.getAskQueue().add(0, ask4);
                BidAsk ask5 = new BidAsk();
                ask5.setPrice(Double.parseDouble(String.valueOf(rowValues[23]).trim()));
                ask5.setVolume(getVolume(String.valueOf(rowValues[24]).trim()));
                stockSnapshot.getAskQueue().add(0, ask5);

                BidAsk bid1 = new BidAsk();
                bid1.setPrice(Double.parseDouble(String.valueOf(rowValues[25]).trim()));
                bid1.setVolume(getVolume(String.valueOf(rowValues[26]).trim()));
                stockSnapshot.getBidQueue().add(bid1);
                BidAsk bid2 = new BidAsk();
                bid2.setPrice(Double.parseDouble(String.valueOf(rowValues[27]).trim()));
                bid2.setVolume(getVolume(String.valueOf(rowValues[28]).trim()));
                stockSnapshot.getBidQueue().add(bid2);
                BidAsk bid3 = new BidAsk();
                bid3.setPrice(Double.parseDouble(String.valueOf(rowValues[29]).trim()));
                bid3.setVolume(getVolume(String.valueOf(rowValues[30]).trim()));
                stockSnapshot.getBidQueue().add(bid3);
                BidAsk bid4 = new BidAsk();
                bid4.setPrice(Double.parseDouble(String.valueOf(rowValues[31]).trim()));
                bid4.setVolume(getVolume(String.valueOf(rowValues[32]).trim()));
                stockSnapshot.getBidQueue().add(bid4);
                BidAsk bid5 = new BidAsk();
                bid5.setPrice(Double.parseDouble(String.valueOf(rowValues[33]).trim()));
                bid5.setVolume(getVolume(String.valueOf(rowValues[34]).trim()));
                stockSnapshot.getBidQueue().add(bid5);

                StockSnapshot hSnapshot = (StockSnapshot) ServerContext.getTempSnapshotMap().get(symbol);
                if (hSnapshot == null) {
                    ServerContext.getRtSnapshotQueue().add(stockSnapshot.clone());
                    ServerContext.getSnapshotMap().put(stockSnapshot.symbol, stockSnapshot);
                    ServerContext.getTempSnapshotMap().put(stockSnapshot.symbol, stockSnapshot);
                } else {
                    if (!hSnapshot.equalsTemp(stockSnapshot)) {
                        hSnapshot.updateTempSnapshot(stockSnapshot);
                        hSnapshot.updateSnapshot(hSnapshot);
                        ServerContext.getRtSnapshotQueue().add(stockSnapshot);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            temp = null;
        }
    }

    public int getVolume(String str) {
        int volume = 0;
        volume = (int) (Double.parseDouble(str) / 100);
        return volume;
    }
}
