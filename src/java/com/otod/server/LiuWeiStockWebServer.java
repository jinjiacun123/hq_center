/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.otod.bean.ServerContext;
import com.otod.bean.WorkHandleParam;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.tradetime.TimeNode;
import com.otod.server.dao.InitDataDao;
import com.otod.server.thread.ChartToDBThread;
import com.otod.server.thread.RTSnapshotHandleThread;
import com.otod.server.thread.ReadDBFSHThread;
import com.otod.server.thread.ReadDBFSZThread;
import com.otod.server.thread.SignalHandleThread;
import com.otod.server.thread.StockDividendThread;
import com.otod.server.timer.ChartToDBTimer;
import com.otod.server.timer.SignalTimer;
import com.otod.util.Config;
import com.otod.util.DateUtil;
import com.otod.util.HttpClientUtil;
import com.otod.util.mutithread.TimerThread;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class LiuWeiStockWebServer extends Thread {

    private URL url = null;
    private String filePath = null;
    private TimerThread signalTimer = new TimerThread();
    private TimerThread chartToDBTimer = new TimerThread();

    public LiuWeiStockWebServer() {
        try {
            url = LiuWeiStockWebServer.class.getClassLoader().getResource("");
            filePath = url.toURI().getPath();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here        
        LiuWeiStockWebServer liuWeiStockWebServer = new LiuWeiStockWebServer();
        liuWeiStockWebServer.start();
    }

    @Override
    public void run() {
        String outPath = Config.OUT_FILE;  
        try {  
            System.setOut(new PrintStream(new FileOutputStream(outPath, true)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LiuWeiStockWebServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("LiuWeiStockWebServer Startup!");
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(tz);

       // new ChartToDBThread().start();//k线保存到数据库处理(不停从队列中获取，然后同步数据库)
       // new RTSnapshotHandleThread().start();
        new SignalHandleThread().start();//开收盘的信号处理过程

        /*
            init trade list,empty list as trade queue
            初始化ServerContext.getTradeTimeMap()，交易时间节点
        */
        doTradeTimeList();
        /*
            trade basic info,include open and close
            初始化 ServerContext.getExchangeMap()，交易所开收盘基本信息
        */
        doExchangeList();
        /*
            init symbol(sh,sz),all code load list
            初始化ServerContext.getMasterMap()，当前交易所包含的品种及其名称
        */
        doMasterList();
        /*
            load minute and kline
            从数据库加载分时及其k线
        */
        doInitData();

        //new StockDividendThread().start();

        /**
         * 每读一次dbf延迟半秒，把读取到的新数据加入到实时处理队列中
         */
        new ReadDBFSHThread().start();
        new ReadDBFSZThread().start();
        //开启两种定时器
        /**
         * 处理节假日，每20秒触发一次
         */
       // doSignalTimer();//singal trigger
        /**
         * 处理分时及其k线同步数据库，没1分钟触发一次
         * 触发调度处理：按品种，批量同步数据库
         */
        //doChartToDBTimer();//chart trigger
    }

    private void doAuthorize() {
        ServerContext.setAuthorizeFlag(false);
        String url = "http://115.29.77.191/AuthorizeServer/RszxCtpFuturesServlet?exec=authorize";
        HashMap header = new HashMap();
        byte[] bytes = HttpClientUtil.createHttpRequest(url, header);
        if (bytes == null || bytes.length == 0) {
            return;
        }
        try {
            String contents = new String(bytes, "UTF-8");
            String status = contents;
            if (status.equals("1")) {
                ServerContext.setAuthorizeFlag(true);
            } else {
                ServerContext.setAuthorizeFlag(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doInitData() {
        InitDataDao initDataDao = new InitDataDao();
        initDataDao.doWork();
    }

    private void doTradeTimeList() {
        //上海 深证 交易所
        List<TimeNode> list = new ArrayList<TimeNode>();
        TimeNode timeNode = new TimeNode();
        timeNode.startTime = 930;
        timeNode.endTime = 1130;
        list.add(timeNode);
        timeNode = new TimeNode();
        timeNode.startTime = 1300;
        timeNode.endTime = 1500;
        list.add(timeNode);
        ServerContext.getTradeTimeMap().put("TradeTime1", list);
    }

    public void doExchangeList() {
        int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));
        ArrayList<Integer> holidayList = readHolidayList(filePath + "/cn_holiday.txt");
//        date=20121101;
        ExchangeData shExchange = new ExchangeData();
        shExchange.code = "SH";
        shExchange.cnName = "上海交易所";
        shExchange.openTime = 910;
        shExchange.closeTime = 1510;
        shExchange.tradeDate = date;
        shExchange.tradeTimeKey = "TradeTime1";
        shExchange.holidayList = holidayList;
        shExchange.isTrade = true;
        ServerContext.getExchangeMap().put(shExchange.code, shExchange);

        ExchangeData szExchange = new ExchangeData();
        szExchange.code = "SZ";
        szExchange.cnName = "深圳交易所";
        szExchange.openTime = 910;
        szExchange.closeTime = 1510;
        szExchange.tradeDate = date;
        szExchange.tradeTimeKey = "TradeTime1";
        szExchange.holidayList = holidayList;
        szExchange.isTrade = true;
        ServerContext.getExchangeMap().put(szExchange.code, szExchange);

    }

    private ArrayList<Integer> readHolidayList(String filePath) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "GBK"));
            String str = null;
            int date = 0;
            while ((str = br.readLine()) != null) {
                if (!str.startsWith("#")) {
                    date = Integer.parseInt(str.replace("-", ""));
                    list.add(date);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private void readMasterList(String filePath) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
            String str = null;
            int date = 0;
            while ((str = br.readLine()) != null) {
                if (!str.startsWith("#")) {
                    String[] rows = str.split(",");
                    if (rows.length < 3) {
                        continue;
                    }
                    MasterData masterData = new MasterData();
                    masterData.symbol = rows[0];
                    masterData.outSymbol = rows[0];
                    masterData.cnName = rows[1];
                    masterData.keyStr = rows[0];
                    masterData.exchCode = rows[2];
                    masterData.tradeTimeKey = "TradeTime5";
                    masterData.isTrade = true;

                    ServerContext.getMasterMap().put(masterData.symbol, masterData);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void doMasterList() {
        int date = Integer.parseInt(DateUtil.formatDate(null, "yyyyMMdd"));

        doSHMaster();
        doSZMaster();

        ExchangeData exchangeData;
        Iterator it = ServerContext.getMasterMap().entrySet().iterator();
        List<TimeNode> tradeTime = null;
        while (it.hasNext()) {
            Map.Entry<String, MasterData> entryset = (Map.Entry<String, MasterData>) it.next();
            String symbol = (String) entryset.getKey();
            MasterData masterData = (MasterData) entryset.getValue();
//            System.out.println(masterData.symbol+","+masterData.cnName);
            exchangeData = ServerContext.getExchangeMap().get(masterData.exchCode);

            if (masterData.keyStr != null) {
                ServerContext.getKeyMasterMap().put(masterData.keyStr, masterData);
            }

            if (exchangeData != null && exchangeData.openTime >= 0 && exchangeData.closeTime >= 0) {
                tradeTime = ServerContext.getTradeTimeMap().get(exchangeData.tradeTimeKey);
                masterData.tradeDate = exchangeData.tradeDate;
                masterData.isTrade = exchangeData.isTrade;
                masterData.tradeTimeKey = exchangeData.tradeTimeKey;
                masterData.tradeTimes = tradeTime;
                masterData.tradeTimeKey = exchangeData.tradeTimeKey;
            } else {
//                System.out.println(masterData.symbol+","+masterData.tradeTimeKey);
                tradeTime = ServerContext.getTradeTimeMap().get(masterData.tradeTimeKey);
//                masterData.tradeDate = exchangeData.tradeDate;
                masterData.tradeTimes = tradeTime;
                masterData.tradeDate = date;
            }
        }
    }

    public void doSHMaster() {
        System.out.println(Config.DBF_SH);
        String path = Config.DBF_PATH + Config.FILE_SPLITE + Config.DBF_SH;
        InputStream is = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 2);
            byte[] temp = null;
            File file = new File(path);

            if (file.exists() && file.isFile() && file.length() > 0 && file.canRead()) {
                ByteBuffer.allocate((int) file.length());
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

            // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
            DBFReader reader = new DBFReader(new ByteArrayInputStream(temp));
            reader.setCharactersetName("GBK");

            Object[] rowValues;
            // 一条条取出path文件中记录  
            int count = 0;

            while ((rowValues = reader.nextRecord()) != null) {
                if (rowValues[0] == null) {
                    continue;
                }
                String symbol = String.valueOf(rowValues[0]).trim();
                if (symbol.equals("") || symbol.length() < 6) {
                    continue;
                }
                MasterData masterData = new MasterData();
                String qz = symbol.substring(0, 1);
                if (qz.equals("0") || qz.equals("1") || qz.equals("2") 
                 || qz.equals("5") || qz.equals("6") || qz.equals("7")
                 || qz.equals("9")) {
                    masterData.symbol = "SH" + String.valueOf(rowValues[0]).trim();
                    masterData.cnName = String.valueOf(rowValues[1]).trim();
                    masterData.exchCode = "SH";
                    ServerContext.getMasterMap().put(masterData.symbol, masterData);
                   // System.out.println(masterData.symbol + "||" + masterData.cnName);
                    count++;
                }
            }
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
            }
        }
    }

    public void doSZMaster() {
        String path = Config.DBF_PATH + Config.FILE_SPLITE + Config.DBF_SZ;
        System.out.println(path);
        InputStream is = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 2);
            byte[] temp = null;
            File file = new File(path);

            if (file.exists() && file.isFile() && file.length() > 0 && file.canRead()) {
                ByteBuffer.allocate((int) file.length());
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

            // 根据输入流初始化一个DBFReader实例，用来读取DBF文件信息  
            DBFReader reader = new DBFReader(new ByteArrayInputStream(temp));
            reader.setCharactersetName("GBK");

            Object[] rowValues;
            // 一条条取出path文件中记录  
            int count = 0;

            while ((rowValues = reader.nextRecord()) != null) {
                if (rowValues[0] == null) {
                    continue;
                }
                String symbol = String.valueOf(rowValues[0]).trim();
                if (symbol.equals("") || symbol.length() < 6) {
                    continue;
                }
                MasterData masterData = new MasterData();
                String qz = symbol.substring(0, 1);
                if (qz.equals("3") || qz.equals("0") || qz.equals("3") || qz.equals("2")) {
                    masterData.symbol = "SZ" + String.valueOf(rowValues[0]).trim();
                    masterData.cnName = String.valueOf(rowValues[1]).trim();
                    masterData.exchCode = "SZ";
                    ServerContext.getMasterMap().put(masterData.symbol, masterData);
//                System.out.println(masterData.symbol + "||" + masterData.cnName);
                    count++;
                }
            }
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
            }
        }
    }

    public void doSignalTimer() {
        signalTimer.setPeriodDay(1000 * 20);
        WorkHandleParam workhandleParam = new WorkHandleParam();
        workhandleParam.workHandle = new SignalTimer();
        signalTimer.setWorkHandleParam(workhandleParam);
        signalTimer.start();
    }

    public void doChartToDBTimer() {
        chartToDBTimer.setPeriodDay(1000 * 60);
        WorkHandleParam workhandleParam = new WorkHandleParam();
        workhandleParam.workHandle = new ChartToDBTimer();
        chartToDBTimer.setWorkHandleParam(workhandleParam);
        chartToDBTimer.start();
    }
}
