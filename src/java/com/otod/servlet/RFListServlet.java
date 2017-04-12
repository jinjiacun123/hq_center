/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.snapshot.StockSnapshot;
import com.otod.util.StringUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
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
public class RFListServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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
        String index  = request.getParameter("index");
        String callback = request.getParameter("callback");
        String way      = request.getParameter("way");
        
        int iWay = Integer.parseInt(way);
        ConcurrentHashMap<String,Double> StopMap = null;
        if(iWay == 1)
            StopMap = ServerContext.getRaiseStopMap();        
        else
            StopMap = ServerContext.getFallStopMap();
        int size = StopMap.size();
        String[] codeList = new String[size];
        StopMap.keySet().toArray(codeList);
        StockSnapshot stockSnapshot = null; 
        int j = 0;int decimal = 2;
        int i = 0;
        JSONArray array = new JSONArray();
        PrintWriter out = response.getWriter();
        StringBuffer valueBuffer = new StringBuffer();
        for (;  i<size; i++,j++) {
            //if(i>10)
             //       break;
            stockSnapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(codeList[i]);
            if (stockSnapshot != null) {
                JSONObject json = new JSONObject();
                json.put("symbol", codeList[i]);
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
                json.put("turnrate", StringUtil.formatNumber(stockSnapshot.getTurnoverRate(), 0));
                json.put("earning", StringUtil.formatNumber(stockSnapshot.getEarming(), 0));
                json.put("time", stockSnapshot.getQuoteTime());
                array.add(json);
            }

        }

          valueBuffer.append(array.toString()); 

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
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
