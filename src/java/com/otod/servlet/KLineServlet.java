/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.StockDividend;

import com.otod.bean.quote.kline.KLineData;
import com.otod.bean.quote.kline.KLineQueue;
import com.otod.bean.quote.master.MasterData;
import com.otod.util.DateUtil;
import com.otod.util.StringUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class KLineServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String symbol = request.getParameter("symbol");
        String period = request.getParameter("period");
        String dateStr = request.getParameter("date");
        String numStr = request.getParameter("num");
        String from = request.getParameter("from");
        String fq = request.getParameter("fq");
        String callback = request.getParameter("callback");

        if (symbol == null || symbol.equals("")) {
            return;
        }
        if (period == null || period.equals("")) {
            period = "100";
        }
        if (from == null || from.equals("")) {
            from = "mobile";
        }
        if (fq == null || fq.equals("")) {
            fq = "0";
        }


        if (dateStr == null) {
            dateStr = "";
        }
        int date = 0;
        if (!dateStr.equals("")) {
            date = Integer.parseInt(dateStr);
        }
        int num = 300;
        if (numStr != null && !numStr.equals("")) {
            num = Integer.parseInt(numStr);
        }

        List<KLineData> list = null;
        if (period.equals("100")) {
            list = doDay(symbol, period, date);
        } else if (period.equals("200")) {
            list = doDay(symbol, period, date);
        } else if (period.equals("300")) {
            list = doDay(symbol, period, date);
        } else if (period.equals("400")) {
            list = doDay(symbol, period, date);
        } else if (period.equals("001")) {
            list = doMinute(symbol, period, date);
        } else if (period.equals("005")) {
            list = doMinute(symbol, period, date);
        } else if (period.equals("015")) {
            list = doMinute(symbol, period, date);
        } else if (period.equals("030")) {
            list = doMinute(symbol, period, date);
        } else if (period.equals("060")) {
            list = doMinute(symbol, period, date);
        }
        if (list != null && list.size() > 0) {
            list = new ArrayList<KLineData>(list);
        }
        MasterData masterData = ServerContext.getMasterMap().get(symbol);
        JSONObject json = new JSONObject();
        PrintWriter out = response.getWriter();

        if (from.equals("pc")) {
            if (list != null && list.size() > 0 && masterData != null) {
                json.put("symbol", symbol);
                json.put("name", masterData.cnName);
                int start = 0;
                if (num < list.size()) {
                    start = list.size() - num;
                }
                JSONArray array = new JSONArray();
                KLineData klineData;
                for (int i = start; i < list.size(); i++) {
                    klineData = list.get(i);
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("date", klineData.getQuoteDate());
                    dataJson.put("time", klineData.getQuoteTime());
                    dataJson.put("open", klineData.getOpenPrice());
                    dataJson.put("high", klineData.getHighPrice());
                    dataJson.put("low", klineData.getLowPrice());
                    dataJson.put("close", klineData.getClosePrice());
                    dataJson.put("volume", klineData.getVolume());
                    dataJson.put("value", StringUtil.formatNumber(klineData.getTurnover(), 0));
                    array.add(dataJson);
                }
                json.put("data", array);
            }
        } else if (from.equals("mobile")) {
            if (list != null && list.size() > 0 && masterData != null) {
                List<KLineData> resultList = new ArrayList<KLineData>();

                json.put("s", symbol);
                json.put("n", masterData.cnName);
                int start = 0;
                if (num < list.size()) {
                    start = list.size() - num;
                }
                JSONArray array = new JSONArray();
                KLineData klineData;
                List<StockDividend> stockDividendList = ServerContext.getStockDividendMap().get(symbol);

                if (fq.equals("0")) {
                    for (int i = start; i < list.size(); i++) {
                        klineData = list.get(i);
                        resultList.add(klineData);
                    }
                } else if (fq.equals("1")) {
                    if (period.equals("100")) {
                        double tbl = 1;
                        for (int i = list.size() - 1; i >= start; i--) {
                            klineData = list.get(i);
                            KLineData rKlineData = klineData.clone();
                            rKlineData.setOpenPrice(rKlineData.getOpenPrice() * tbl);
                            rKlineData.setHighPrice(rKlineData.getHighPrice() * tbl);
                            rKlineData.setLowPrice(rKlineData.getLowPrice() * tbl);
                            rKlineData.setClosePrice(rKlineData.getClosePrice() * tbl);
                            resultList.add(0, rKlineData);
                            if (i > 0) {
                                KLineData pKlineData = list.get(i - 1);
                                if (stockDividendList != null) {
                                    for (int j = 0; j < stockDividendList.size(); j++) {
                                        StockDividend stockDividend = stockDividendList.get(j);
                                        String content = stockDividend.getContent();
                                        int sdate = Integer.parseInt(stockDividend.getCqr().replaceAll("-", ""));
                                        if (klineData.getQuoteDate() == sdate) {
                                            double fh = 0;
                                            String[] cs = content.split("派");
                                            if (cs.length > 1) {
                                                fh = Double.parseDouble(cs[1].substring(0, cs[1].indexOf("元")));
                                                fh = fh / 10d;
                                            }
                                            double sg = 0;
                                            cs = content.split("送");
                                            if (cs.length > 1) {
                                                sg = Double.parseDouble(cs[1].substring(0, cs[1].indexOf("股")));
                                            }
                                            cs = content.split("转");
                                            if (cs.length > 1) {
                                                sg += Double.parseDouble(cs[1].substring(0, cs[1].indexOf("股")));
                                            }
//                                            System.out.println(content);
//                                            System.out.println(fh + "," + sg);
                                            double price = (pKlineData.getClosePrice() + 0 - fh) / (1 + 0 + sg / 10d);
                                            tbl *= price / pKlineData.getClosePrice();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (period.equals("200")) {
                        double tbl = 1;
                        for (int i = list.size() - 1; i >= start; i--) {
                            klineData = list.get(i);

                            double bl = -1;
                            if (i > 0) {
                                KLineData pKlineData = list.get(i - 1);
                                if (stockDividendList != null) {
                                    for (int j = 0; j < stockDividendList.size(); j++) {
                                        StockDividend stockDividend = stockDividendList.get(j);
                                        String content = stockDividend.getContent();
                                        Date sd = DateUtil.parseDate(stockDividend.getCqr(), "yyyy-MM-dd");
                                        int sdate = Integer.parseInt(stockDividend.getCqr().replaceAll("-", ""));
                                        int syear = Integer.parseInt(DateUtil.formatDate(sd, "yyyy"));
                                        Calendar hCalendar = Calendar.getInstance();
                                        hCalendar.setTime(DateUtil.parseDate(String.valueOf(klineData.getQuoteDate()), "yyyyMMdd"));
                                        int hWeekOfYear = hCalendar.get(Calendar.WEEK_OF_YEAR);
                                        int hyear = Integer.parseInt(DateUtil.formatDate(hCalendar.getTime(), "yyyy"));
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(DateUtil.parseDate(String.valueOf(sdate), "yyyyMMdd"));
                                        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                                        if (hWeekOfYear == weekOfYear && syear == hyear) {
                                            double fh = 0;
                                            String[] cs = content.split("派");
                                            if (cs.length > 1) {
                                                fh = Double.parseDouble(cs[1].substring(0, cs[1].indexOf("元")));
                                                fh = fh / 10d;
                                            }
                                            double sg = 0;
                                            cs = content.split("送");
                                            if (cs.length > 1) {
                                                sg = Double.parseDouble(cs[1].substring(0, cs[1].indexOf("股")));
                                            }
                                            cs = content.split("转");
                                            if (cs.length > 1) {
                                                sg += Double.parseDouble(cs[1].substring(0, cs[1].indexOf("股")));
                                            }
//                                            System.out.println(content);
//                                            System.out.println(fh + "," + sg);
                                            double price = (pKlineData.getClosePrice() + 0 - fh) / (1 + 0 + sg / 10d);
                                            bl = price / pKlineData.getClosePrice();
                                            list.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                            if (bl > 0) {
                                tbl *= bl;
                            } else {
                                KLineData rKlineData = klineData.clone();
                                rKlineData.setOpenPrice(rKlineData.getOpenPrice() * tbl);
                                rKlineData.setHighPrice(rKlineData.getHighPrice() * tbl);
                                rKlineData.setLowPrice(rKlineData.getLowPrice() * tbl);
                                rKlineData.setClosePrice(rKlineData.getClosePrice() * tbl);
                                resultList.add(0, rKlineData);
                            }
                        }
                    } else if (period.equals("300")) {
                        double tbl = 1;
                        for (int i = list.size() - 1; i >= start; i--) {
                            klineData = list.get(i);

                            double bl = -1;
                            if (i > 0) {
                                KLineData pKlineData = list.get(i - 1);
                                if (stockDividendList != null) {
                                    for (int j = 0; j < stockDividendList.size(); j++) {
                                        StockDividend stockDividend = stockDividendList.get(j);
                                        String content = stockDividend.getContent();
                                        Date sd = DateUtil.parseDate(stockDividend.getCqr(), "yyyy-MM-dd");
                                        int sdate = Integer.parseInt(stockDividend.getCqr().replaceAll("-", ""));
                                        int hYearMonth = klineData.getQuoteDate() / 100;
                                        int yearMonth = sdate / 100;
                                        if (hYearMonth == yearMonth) {
                                            double fh = 0;
                                            String[] cs = content.split("派");
                                            if (cs.length > 1) {
                                                fh = Double.parseDouble(cs[1].substring(0, cs[1].indexOf("元")));
                                                fh = fh / 10d;
                                            }
                                            double sg = 0;
                                            cs = content.split("送");
                                            if (cs.length > 1) {
                                                sg = Double.parseDouble(cs[1].substring(0, cs[1].indexOf("股")));
                                            }
                                            cs = content.split("转");
                                            if (cs.length > 1) {
                                                sg += Double.parseDouble(cs[1].substring(0, cs[1].indexOf("股")));
                                            }
//                                            System.out.println(content);
//                                            System.out.println(fh + "," + sg);
                                            double price = (pKlineData.getClosePrice() + 0 - fh) / (1 + 0 + sg / 10d);
                                            bl = price / pKlineData.getClosePrice();
                                            list.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                            if (bl > 0) {
                                tbl *= bl;
                            } else {
                                KLineData rKlineData = klineData.clone();
                                rKlineData.setOpenPrice(rKlineData.getOpenPrice() * tbl);
                                rKlineData.setHighPrice(rKlineData.getHighPrice() * tbl);
                                rKlineData.setLowPrice(rKlineData.getLowPrice() * tbl);
                                rKlineData.setClosePrice(rKlineData.getClosePrice() * tbl);
                                resultList.add(0, rKlineData);
                            }
                        }
                    } else {
                        for (int i = start; i < list.size(); i++) {
                            klineData = list.get(i);
                            resultList.add(klineData);
                        }
                    }
                }

                for (int i = 0; i < resultList.size(); i++) {
                    klineData = resultList.get(i);
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("d", klineData.getQuoteDate());
                    dataJson.put("t", klineData.getQuoteTime());
                    dataJson.put("o", StringUtil.formatNumber(klineData.getOpenPrice(), 2));
                    dataJson.put("h", StringUtil.formatNumber(klineData.getHighPrice(), 2));
                    dataJson.put("l", StringUtil.formatNumber(klineData.getLowPrice(), 2));
                    dataJson.put("c", StringUtil.formatNumber(klineData.getClosePrice(), 2));
                    dataJson.put("v", klineData.getVolume());
                    dataJson.put("a", StringUtil.formatNumber(klineData.getTurnover(), 0));
                    array.add(dataJson);
                }

                json.put("d", array);
            }
        }



        try {
            if (callback != null) {
                out.print(callback + "(" + json.toString() + ")");
            } else {
                out.print(json.toString());
            }
        } finally {
            out.close();
        }
    }

    private List<KLineData> doDay(String symbol, String period, int date) {

        KLineQueue klineQueue = null;
        MasterData master = ServerContext.getMasterMap().get(symbol);
        List<KLineData> list = null;
        if (date == 0) {
            if (period.equals("100")) {
                klineQueue = ServerContext.getDayMap().get(symbol);
            } else if (period.equals("200")) {
                klineQueue = ServerContext.getWeekMap().get(symbol);
            } else if (period.equals("300")) {
                klineQueue = ServerContext.getMonthMap().get(symbol);
            }
            if (klineQueue == null) {
                return list;
            }
            //Snapshot snapshot = (Snapshot) ShareModel.getSnapshotMap().get(symbol);
            //klineQueue.updDaySnapshot(snapshot);
            list = klineQueue.getQueue();
        }
        return list;
    }

    private List<KLineData> doMinute(String symbol, String period, int date) {
        List<KLineData> list = null;
        if (date == 0) {
            KLineQueue klineQueue = null;
            if (period.equals("001")) {
                klineQueue = ServerContext.getMinute1Map().get(symbol);
            }
            if (period.equals("005")) {
                klineQueue = ServerContext.getMinute5Map().get(symbol);
            }
            if (period.equals("015")) {
                klineQueue = ServerContext.getMinute15Map().get(symbol);
            }
            if (period.equals("030")) {
                klineQueue = ServerContext.getMinute30Map().get(symbol);
            }
            if (period.equals("060")) {
                klineQueue = ServerContext.getMinute60Map().get(symbol);
            }
            if (klineQueue == null) {
                return list;
            }
            list = klineQueue.getQueue();
        }

        return list;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
