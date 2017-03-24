/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.thread;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.StockDividend;
import com.otod.util.HttpClientUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class StockDividendThread extends Thread {

    @Override
    public void run() {
        while (true) {
            doWork();
            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void doWork() {
        try {
            String url = "http://114.215.150.241:6080/LiuWeiStockDividendWebServer/StockDividend?r=" + Math.random();
            byte[] bytes = HttpClientUtil.createHttpRequest(url, new HashMap());
            if (bytes == null || bytes.length == 0) {
                return;
            }
            String contents = new String(bytes, "UTF-8");
            JSONArray array1 = JSONArray.fromObject(contents);
            for (int i = 0; i < array1.size(); i++) {
                JSONObject json1 = array1.getJSONObject(i);
                String symbol = json1.getString("symbol");
                JSONArray array2 = json1.getJSONArray("data");
                List<StockDividend> list = new ArrayList<StockDividend>();
                for (int j = 0; j < array2.size(); j++) {
                    JSONObject json2 = array2.getJSONObject(j);
                    StockDividend stockDividend = new StockDividend();
                    stockDividend.setCqr(json2.getString("cqr"));
                    stockDividend.setContent(json2.getString("content"));
                    list.add(stockDividend);
                }
                if (list.size() > 0) {
                    ServerContext.getStockDividendMap().put(symbol, list);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
