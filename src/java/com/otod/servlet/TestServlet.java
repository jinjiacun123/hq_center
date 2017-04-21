/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.kline.KLineData;
import com.otod.dao.DayDao;
import com.otod.db.Connector;
import com.otod.db.MysqlConnector;
import com.otod.util.ApplicationConstant;
import com.otod.util.Help;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class TestServlet extends HttpServlet {

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
        String market = request.getParameter("market");
        String column = request.getParameter("column");
        
        String[] Market = market.split(",");
        
        Map<String,Double> myMap = new HashMap<String,Double>();
        List<Map.Entry<String, Double>> list = null;
        PrintWriter out = response.getWriter(); 
        for(int i=0; i< Market.length; i++){
            myMap.putAll(ServerContext.getMarketList().get(Market[i]+"_"+column));
        }
        //myMap.putAll(ServerContext.getMarketList().get(market+"_"+column));
        list = Help.sort_by_double_ex(myMap);
        for(int i = 0; i < list.size(); i++){
            out.println(list.get(i).getKey()+":"+list.get(i).getValue());
        }
        /*
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            DayDao dayDao = new DayDao();
            dayDao.setConnector(connector);
            String symbol = "SH600000";
            List<KLineData> list = dayDao.getBySymbol(symbol, ApplicationConstant.KLINE_NUMBER);
            PrintWriter out = response.getWriter();

            try {
                if (list != null) {
                    out.print(list.size());
                }
            } finally {
                out.close();
            }
        } catch (Exception ex) {
            System.out.println("test servlet");
            ex.printStackTrace();
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
        */
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
