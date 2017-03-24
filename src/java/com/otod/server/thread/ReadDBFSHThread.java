/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.thread;

import com.linuxense.javadbf.DBFReader;
import com.otod.bean.ServerContext;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.snapshot.BidAsk;
import com.otod.bean.quote.snapshot.Snapshot;
import com.otod.bean.quote.snapshot.StockSnapshot;
import com.otod.util.Config;
import com.otod.util.DateUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
//            System.out.println("SH "+(end.getTime()-start.getTime()));
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReadDBFSHThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void doReadDBF() {
        String path = Config.DBF_PATH + "/SHOW2003.DBF";
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
            while ((rowValues = reader.nextRecord()) != null) {
                if (rowValues[0] == null) {
                    continue;
                }
                String symbol = String.valueOf(rowValues[0]).trim();
                if (symbol.equals("") || symbol.length() < 6) {
                    continue;
                }
                symbol = "SH" + symbol;

                MasterData masterData = ServerContext.getMasterMap().get(symbol);
                if (masterData == null) {
                    continue;
                }
                StockSnapshot stockSnapshot = new StockSnapshot();
                stockSnapshot.setSymbol(symbol);
                stockSnapshot.setQuoteDate(date);
                stockSnapshot.setQuoteTime(time);
                stockSnapshot.setOpenPrice(Double.parseDouble(String.valueOf(rowValues[3]).trim()));
                stockSnapshot.setHighPrice(Double.parseDouble(String.valueOf(rowValues[5]).trim()));
                stockSnapshot.setLowPrice(Double.parseDouble(String.valueOf(rowValues[6]).trim()));
                stockSnapshot.setLastPrice(Double.parseDouble(String.valueOf(rowValues[7]).trim()));
                stockSnapshot.setpClose(Double.parseDouble(String.valueOf(rowValues[2]).trim()));
                stockSnapshot.setVolume(getVolume(String.valueOf(rowValues[10]).trim()));
                stockSnapshot.setTurnover(Double.parseDouble(String.valueOf(rowValues[4]).trim()));
                BidAsk bid1 = new BidAsk();
//                System.out.println(String.valueOf(rowValues[14]).trim());
                bid1.setPrice(Double.parseDouble(String.valueOf(rowValues[8]).trim()));
                bid1.setVolume(getVolume(String.valueOf(rowValues[12]).trim()));
                stockSnapshot.getBidQueue().add(bid1);
                BidAsk ask1 = new BidAsk();
                ask1.setPrice(Double.parseDouble(String.valueOf(rowValues[9]).trim()));
                ask1.setVolume(getVolume(String.valueOf(rowValues[17]).trim()));
                stockSnapshot.getAskQueue().add(0, ask1);
                BidAsk bid2 = new BidAsk();
                bid2.setPrice(Double.parseDouble(String.valueOf(rowValues[13]).trim()));
                bid2.setVolume(getVolume(String.valueOf(rowValues[14]).trim()));
                stockSnapshot.getBidQueue().add(bid2);
                BidAsk ask2 = new BidAsk();
                ask2.setPrice(Double.parseDouble(String.valueOf(rowValues[18]).trim()));
                ask2.setVolume(getVolume(String.valueOf(rowValues[19]).trim()));
                stockSnapshot.getAskQueue().add(0, ask2);
                BidAsk bid3 = new BidAsk();
                bid3.setPrice(Double.parseDouble(String.valueOf(rowValues[15]).trim()));
                bid3.setVolume(getVolume(String.valueOf(rowValues[16]).trim()));
                stockSnapshot.getBidQueue().add(bid3);
                BidAsk ask3 = new BidAsk();
                ask3.setPrice(Double.parseDouble(String.valueOf(rowValues[20]).trim()));
                ask3.setVolume(getVolume(String.valueOf(rowValues[21]).trim()));
                stockSnapshot.getAskQueue().add(0, ask3);
                BidAsk bid4 = new BidAsk();
                bid4.setPrice(Double.parseDouble(String.valueOf(rowValues[22]).trim()));
                bid4.setVolume(getVolume(String.valueOf(rowValues[23]).trim()));
                stockSnapshot.getBidQueue().add(bid4);
                BidAsk ask4 = new BidAsk();
                ask4.setPrice(Double.parseDouble(String.valueOf(rowValues[26]).trim()));
                ask4.setVolume(getVolume(String.valueOf(rowValues[27]).trim()));
                stockSnapshot.getAskQueue().add(0, ask4);
                BidAsk bid5 = new BidAsk();
                bid5.setPrice(Double.parseDouble(String.valueOf(rowValues[24]).trim()));
                bid5.setVolume(getVolume(String.valueOf(rowValues[25]).trim()));
                stockSnapshot.getBidQueue().add(bid5);
                BidAsk ask5 = new BidAsk();
                ask5.setPrice(Double.parseDouble(String.valueOf(rowValues[28]).trim()));
                ask5.setVolume(getVolume(String.valueOf(rowValues[29]).trim()));
                stockSnapshot.getAskQueue().add(0, ask5);

                StockSnapshot hSnapshot = (StockSnapshot) ServerContext.getTempSnapshotMap().get(symbol);
                if (hSnapshot == null) {
                    ServerContext.getRtSnapshotQueue().add(stockSnapshot.clone());
                    ServerContext.getTempSnapshotMap().put(stockSnapshot.symbol, stockSnapshot);
                    ServerContext.getSnapshotMap().put(stockSnapshot.symbol, stockSnapshot);
                } else {
                    if (!hSnapshot.equalsTemp(stockSnapshot)) {
                        hSnapshot.updateTempSnapshot(stockSnapshot);
                        hSnapshot.updateSnapshot(stockSnapshot);
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
