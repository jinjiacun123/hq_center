/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.snapshot.ForexSnapshot;
import com.otod.bean.quote.snapshot.Snapshot;
import com.otod.bean.quote.snapshot.StockSnapshot;
import com.otod.util.StringUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class QuoteListResource extends HttpServlet {

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
        String pRowNum = request.getParameter("rownum");
        String pPageNum = request.getParameter("pageid");
        String pSortPos = request.getParameter("sortpos");
        String pSortType = request.getParameter("sorttype");
        String pGroupCode = request.getParameter("groupcode");
        //System.out.println("groupCode" + pGroupCode);
        if (pGroupCode == null) {
            pGroupCode = "";
        }

        try {
            // pGroupCode=new String(pGroupCode.getBytes("Unicode"),"GBK");
            pGroupCode = new String(pGroupCode.getBytes("ISO-8859-1"), "UTF-8");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int rowNum;
        int pageNum;
        if (pRowNum == null || pRowNum.equals("")) {
            rowNum = 25;
        } else {
            rowNum = Integer.parseInt(pRowNum);
        }

        if (pPageNum == null || pPageNum.equals("")) {
            pageNum = 1;
        } else {
            pageNum = Integer.parseInt(pPageNum);
        }

        if (pSortPos == null) {
            pSortPos = "";
        }

        if (pSortType == null) {
            pSortType = "";
        }
        StringBuffer valuebuffer = new StringBuffer("");

        List<String> symList = ServerContext.getGroupMap().get(pGroupCode);

        StockSnapshot forexSnapshot = null;
        String symbol;

        List<ForexSnapshot> snapshotList = new ArrayList<ForexSnapshot>();
        if (symList == null) {
            symList = new ArrayList<String>();
            for (Map.Entry<String, MasterData> entryset : ServerContext.getMasterMap().entrySet()) {
                symList.add((String) entryset.getKey());
            }
        }




        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (rowNum <= 0) {
            rowNum = 25;
        }
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Snapshot> entryset : ServerContext.getSnapshotMap().entrySet()) {
            forexSnapshot = (StockSnapshot) entryset.getValue();
            if (forexSnapshot != null) {
                symbol = forexSnapshot.getSymbol();

                int decimal = 2;
                if (forexSnapshot.getExchCode().equals("FX")) {
                    decimal = 4;
                }
                valuebuffer.append(forexSnapshot.getSymbol() + ",");
                valuebuffer.append(forexSnapshot.getCnName() + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getLastPrice(), decimal) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getChange(), decimal) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getChangeRate(), 2) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getBid1Price(), decimal) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getAsk1Price(), decimal) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getVolume(), 0) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getTurnover(), 0) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getOpenPrice(), decimal) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getHighPrice(), decimal) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getLowPrice(), decimal) + ",");
                valuebuffer.append(StringUtil.formatNumber(forexSnapshot.getpClose(), decimal) + ",");
                valuebuffer.append(forexSnapshot.getQuoteTime());
                valuebuffer.append("\n");

            }
        }

        PrintWriter out = response.getWriter();
        try {
            out.print(valuebuffer);
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
