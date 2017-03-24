/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.tradetime.TimeNode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
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
 * @author Administrator
 */
public class MasterListServlet extends HttpServlet {

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
        String pGroupCode = request.getParameter("groupcode");
        String callback = request.getParameter("callback");
        if (pGroupCode == null) {
            pGroupCode = "";
        }

        StringBuffer valueBuffer = new StringBuffer();
        List<String> symList = null;
        if (pGroupCode.equals("")) {
            symList = new ArrayList<String>();
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                String symbol = (String) entryset.getKey();
                symList.add(symbol);
            }
        }
        if (symList == null) {
            return;
        }
        List<TimeNode> tradeTimes = null;
        MasterData masterData;
        TimeNode timeNode;

        JSONArray array = new JSONArray();
        for (int i = 0; i < symList.size(); i++) {
            String symbol = symList.get(i);
            masterData = ServerContext.getMasterMap().get(symbol);
            tradeTimes = masterData.tradeTimes;
            String tStr = "";
            for (int j = 0; j < tradeTimes.size(); j++) {
                timeNode = tradeTimes.get(j);
                if (j == (tradeTimes.size() - 1)) {
                    tStr += timeNode.startTime + "~" + timeNode.endTime;
                } else {
                    tStr += timeNode.startTime + "~" + timeNode.endTime + "|";
                }
            }
            JSONObject json = new JSONObject();
            json.put("symbol", masterData.symbol);
            json.put("outsymbol", masterData.outSymbol);
            json.put("name", masterData.cnName);
            json.put("exchange", masterData.exchCode);
            json.put("tradetime", tStr);
            array.add(json);
        }
        valueBuffer.append(array.toString());

        PrintWriter out = response.getWriter();
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
