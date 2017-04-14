/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.StockDividend;

import com.otod.bean.quote.kline.KLineData;
import com.otod.bean.quote.kline.KLineQueue;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.snapshot.StockSnapshot;
import com.otod.util.DateUtil;
import com.otod.util.StringUtil;
import com.otod.util.Help;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 *
 * @author admin
 */
public class LSortServlet extends HttpServlet{
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
        String number = request.getParameter("number");
        String from = request.getParameter("from");
        String column = request.getParameter("column");//SORT_M-�ɽ���,SORT_RAISE-�ǵ���,SORT_AMPLITUDE-���,SORT_TURNOVERRATE-������,SORT_EARMING-��ӯ��
        String index  = request.getParameter("index");
        String way    = request.getParameter("way");//0-����,1-����
        String callback = request.getParameter("callback");
        String market   = request.getParameter("market");//0-����a��Ʊ,1-����b��Ʊ,2-�Ϻ�a��Ʊ,3-�Ϻ�b��Ʊ
        
        /*
        if (symbol == null || symbol.equals("")) {
            return;
        }
        if (period == null || period.equals("")) {
            period = "100";
        }
        if (from == null || from.equals("")) {
            from = "pc";
        }
        if (fq == null || fq.equals("")) {
            fq = "0";
        }
    */
        String[] Markets = null;
        
        if(column == null || column.equals("")){
            column = "SORT_M";
        }
        
        if(index == null || index.equals("")){
            index = "1";
        }
        if(number == null || number.equals("") ){
            number = "10";
        }
        if(market == null || market.equals("") ){
            market = "SH_A";
        }else{
            Markets = market.split(",");
        }
        if(way == null || way.equals("")){
            way = "0";
        }
        
        
        int date = 0;        
        int decimal = 2;
        int iWay = 0;
        Map<String,Double> myMap = null;
        List<Map.Entry<String, Double>> list = null;
        
        //1-�ɽ���,2-�ǵ���,3-���,4-������,5-��ӯ��
        if(Markets != null){
            if(Markets.length>0){
                myMap = ServerContext.getMarketList().get(Markets[0]+"_"+column);
                for(int i=1; i < Markets.length; i++){
                    myMap.putAll(ServerContext.getMarketList().get(Markets[i]+"_"+column));
                }
            }
        }
        else{
            myMap  = ServerContext.getMarketList().get(market+"_"+column);
        }
        iWay = Integer.parseInt(way);
        /*
        switch(iColumn){
            case 1:{
                myMap  = ServerContext.getMarketList().get(iMarket).get(iColumn-1);
            }break;
            case 2:{
                myMap  = ServerContext.getListSortRaiseMap();
            }break;
            case 3:{
                myMap  = ServerContext.getListSortAmplitudeMap();
            }break;
            case 4:{
                myMap  = ServerContext.getListSortTurnoverRateMap();
            }break;
            case 5:{
                myMap  = ServerContext.getListSortEarmingMap();
            }break;
        }
        */
        if(myMap != null){
            if(iWay == 0){
                list = Help.sort_by_double(myMap);
            }else{
                list = Help.sort_by_double_ex(myMap);
            }
        }
        else
            return;
        
        JSONArray array = new JSONArray();
        PrintWriter out = response.getWriter();
        StringBuffer valueBuffer = new StringBuffer();

        StockSnapshot stockSnapshot = null;   
        int i = 0, j = 0;
        i = Integer.parseInt(index);
        i--;
        if(i<=0){
            i=0;
        }else{
            i = (i-1)* Integer.parseInt(number);
        }
         for (; j<Integer.parseInt(number) && i< list.size(); i++,j++) {
            //if(i>10)
             //       break;
            stockSnapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(list.get(i).getKey().toString());
            if (stockSnapshot != null) {
                JSONObject json = new JSONObject();
                json.put("symbol", list.get(i).getKey().toString());
                json.put("name", stockSnapshot.cnName);               
                json.put("bid1price", stockSnapshot.bidQueue.get(0).price);
                json.put("bid1volume", stockSnapshot.bidQueue.get(0).volume);
                json.put("bid2price", stockSnapshot.bidQueue.get(1).price);
                json.put("bid2volume", stockSnapshot.bidQueue.get(1).volume);
                json.put("bid3price", stockSnapshot.bidQueue.get(2).price);
                json.put("bid3volume", stockSnapshot.bidQueue.get(2).volume);
                json.put("bid4price", stockSnapshot.bidQueue.get(3).price);
                json.put("bid4volume", stockSnapshot.bidQueue.get(3).volume);
                json.put("bid5price", stockSnapshot.bidQueue.get(4).price);
                json.put("bid5volume", stockSnapshot.bidQueue.get(4).volume);
                json.put("ask1price", stockSnapshot.askQueue.get(4).price);
                json.put("ask1volume", stockSnapshot.askQueue.get(4).volume);
                json.put("ask2price", stockSnapshot.askQueue.get(3).price);
                json.put("ask2volume", stockSnapshot.askQueue.get(3).volume);
                json.put("ask3price", stockSnapshot.askQueue.get(2).price);
                json.put("ask3volume", stockSnapshot.askQueue.get(2).volume);
                json.put("ask4price", stockSnapshot.askQueue.get(1).price);
                json.put("ask4volume", stockSnapshot.askQueue.get(1).volume);
                json.put("ask5price", stockSnapshot.askQueue.get(0).price);
                json.put("ask5volume", stockSnapshot.askQueue.get(0).volume);                
                json.put("change", StringUtil.formatNumber(stockSnapshot.change, decimal));
                json.put("changerate", StringUtil.formatNumber(stockSnapshot.changeRate, 2) + "%");
                json.put("open", stockSnapshot.getOpenPrice());
                json.put("high", stockSnapshot.getHighPrice());
                json.put("low", stockSnapshot.getLowPrice());
                json.put("close", stockSnapshot.getLastPrice());
                json.put("pclose", stockSnapshot.pClose);
                json.put("volume", StringUtil.formatNumber(stockSnapshot.getVolume(),0));
                json.put("turnover", StringUtil.formatNumber(stockSnapshot.getTurnover(),0));
                json.put("turnrate", StringUtil.formatNumber(stockSnapshot.getTurnoverRate(), 2));
                json.put("earning", StringUtil.formatNumber(stockSnapshot.getEarming(), 2));
                json.put("time", stockSnapshot.getQuoteTime());
                array.add(json);
            }

        }
        /*
        JSONObject json = new JSONObject();
        json.put("record_count", list.size());
        array.add(json);
        */
        JSONObject root = new JSONObject();
        root.put("data", array);
        root.put("record_count", list.size());
          valueBuffer.append(root.toString()); 

        try {
            if (callback != null) {
                out.print(callback + "(" + valueBuffer + ")");
            } else {
                out.print(valueBuffer);
            }
        } finally {
            out.close();
        }
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
