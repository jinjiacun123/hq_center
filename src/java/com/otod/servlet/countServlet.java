/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.servlet;

import com.otod.bean.ServerContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class countServlet extends HttpServlet {

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
        long begin = System.currentTimeMillis();
        response.setContentType("text/html;charset=UTF-8");
        String callback = request.getParameter("callback");
        /*
        String market = request.getParameter("market");
        String callback = request.getParameter("callback");
        String column      = request.getParameter("column");
        */
        Map<String, Map<String, String>> countMap = ServerContext.getCountMap();
        
        /*
        if(market == null || market.equals("")){
            market = "SH_A";
        }
        if(column == null || column.equals("")){
            column = "FLAT_PAN";
        }
        */
        String[] marketList = {
           "SH_A",
           "SZ_A",
           "SZ_MIDDLE",
           "SZ_ACCOUNT"
        };
        String[] columnList = {
           "UP_PAN",
           "FLAT_PAN",
           "DOWN_PAN"
       };
        
        JSONArray array = new JSONArray();
        PrintWriter out = response.getWriter();
        StringBuffer valueBuffer = new StringBuffer();
        JSONObject root = new JSONObject();
        JSONObject json = null;
        int marketListLength = marketList.length;
        int columnListLength = columnList.length;
        
        String prefix = "";
        String key    = "";
        for(int i = 0; i< marketListLength; i++){
            json = new JSONObject();
            for(int j = 0; j< columnListLength; j++){
                prefix = marketList[i] + "_" + columnList[j];
                json.put(columnList[j], countMap.get(prefix).size());
            }
            root.put(marketList[i], json);
        }
       // array.add(root);
        
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
        long end = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式        
        System.out.println(df.format(new Date())+":interface:count,use_time:"+(end-begin));
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
