/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.kline.KLineData;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.minute.MinuteData;
import com.otod.bean.quote.minute.MinuteQueue;
import com.otod.dao.DayDao;
import com.otod.dao.Minute1Dao;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
public class MinuteServlet extends HttpServlet {

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
        String callback = request.getParameter("callback");
        String from = request.getParameter("from");
        StringBuffer valueBuffer = new StringBuffer();
        if (symbol == null || symbol.equals("")) {
            return;
        }
        if (from == null || from.equals("")) {
            from = "mobile";
        }
        MinuteQueue minuteQueue = ServerContext.getMinuteMap().get(symbol);
        if (minuteQueue == null) {
            return;
        }
        MinuteData minuteData = null;
        MasterData masterData = ServerContext.getMasterMap().get(symbol);
        if (minuteQueue.getpClose() == 0) {
            minuteQueue.setpClose(minuteQueue.getQueue().get(minuteQueue.getQueue().size() - 1).getClosePrice());
        }

        JSONObject json = new JSONObject();
        if (from.equals("pc")) {
            json.put("symbol", masterData.symbol);
            json.put("name", masterData.cnName);
            json.put("pclose", minuteQueue.getpClose());
            JSONArray array = new JSONArray();
            for (int i = 0; i < minuteQueue.getQueue().size(); i++) {
                minuteData = minuteQueue.getQueue().get(i);
                JSONObject dataJson = new JSONObject();
                dataJson.put("time", minuteData.getQuoteTime());
                dataJson.put("close", minuteData.getClosePrice());
                dataJson.put("volume", minuteData.getVolume());
                array.add(dataJson);
            }
            json.put("data", array);
        } else if (from.equals("mobile")) {
            json.put("s", masterData.symbol);
            json.put("n", masterData.cnName);
            json.put("p", minuteQueue.getpClose());
            JSONArray array = new JSONArray();
            for (int i = 0; i < minuteQueue.getQueue().size(); i++) {
                minuteData = minuteQueue.getQueue().get(i);
                JSONObject dataJson = new JSONObject();
                dataJson.put("t", minuteData.getQuoteTime());
                dataJson.put("c", minuteData.getClosePrice());
                dataJson.put("v", minuteData.getVolume());
                dataJson.put("a", minuteData.getTurnover());
                array.add(dataJson);
            }
            json.put("d", array);
        }

        PrintWriter out = response.getWriter();
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
