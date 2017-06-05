/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.dao;

import com.otod.bean.quote.tick.TickData;
import com.otod.db.Connector;
import com.otod.util.DateUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TickDao extends ParentDao{
    public void save(TickData data) {
        String sql = "insert into tick (symbol,cn_name,exch_code,`date_time`,"
                + "                     ask1_price,ask1_volume,bid1_price,"
                + "                     bid1_volume,open_price,high_price,"
                + "                     low_price,last_price,p_close,volume,turnover) "
                + "                     values(?,?,?,?,"
                + "                     ?,?,?,"
                + "                     ?,?,?,"
                + "                     ?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, data.getSymbol());
            ps.setString(2, data.getCnName());
            ps.setString(3, data.getExchCode());
            String tmp_time = String.format("%08d",data.getDate())+" "+String.format("%06d",data.getTime());
            ps.setString(4, DateUtil.formatDate(DateUtil.parseDate(tmp_time, "yyyyMMdd HHmmss"),"yyyy-MM-dd HH:mm:ss"));
            ps.setDouble(5, data.getAsk1Price());
            ps.setDouble(6, data.getAsk1Volume());
            ps.setDouble(7, data.getBid1Price());
            ps.setDouble(8, data.getBid1Volume());
            ps.setDouble(9,  data.getOpenPrice());
            ps.setDouble(10, data.getHighPrice());
            ps.setDouble(11, data.getLowPrice());
            ps.setDouble(12, data.getLastPrice());
            ps.setDouble(13, data.getpClose());
            ps.setDouble(14, data.getVolume());
            ps.setDouble(15, data.getTurnover());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Connector.close(null, ps);
        }
    }
    
    public boolean batchSave(List<TickData> list) {
        boolean bl = false;
        String sql = "insert into tick (symbol,cn_name,exch_code,`date_time`,"
                + "                     ask1_price,ask1_volume,bid1_price,"
                + "                     bid1_volume,open_price,high_price,"
                + "                     low_price,last_price,p_close,volume,turnover) "
                + "                     values(?,?,?,?,"
                + "                     ?,?,?,"
                + "                     ?,?,?,"
                + "                     ?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (TickData data : list) {
                ps.setString(1, data.getSymbol());
                ps.setString(2, data.getCnName());
                ps.setString(3, data.getExchCode());
                String tmp_time = String.format("%08d",data.getDate())+" "+String.format("%06d",data.getTime());
                ps.setString(4, DateUtil.formatDate(DateUtil.parseDate(tmp_time, "yyyyMMdd HHmmss"),"yyyy-MM-dd HH:mm:ss"));
                ps.setDouble(5, data.getAsk1Price());
                ps.setDouble(6, data.getAsk1Volume());
                ps.setDouble(7, data.getBid1Price());
                ps.setDouble(8, data.getBid1Volume());
                ps.setDouble(9,  data.getOpenPrice());
                ps.setDouble(10, data.getHighPrice());
                ps.setDouble(11, data.getLowPrice());
                ps.setDouble(12, data.getLastPrice());
                ps.setDouble(13, data.getpClose());
                ps.setDouble(14, data.getVolume());
                ps.setDouble(15, data.getTurnover());
                ps.addBatch();
            }
            bl = ps.executeBatch().length != 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Connector.close(null, ps);
        }
        return bl;
    }
    
    public boolean batchSaveString(List<TickData> list) {
        boolean bl = false;
        // ��ʱʱ��  
        Long begin = new Date().getTime();  
        // sqlǰ׺  
        String prefix = "insert into tick (symbol,cn_name,exch_code,`date_time`,"
                +                          "ask1_price,ask1_volume,bid1_price,"
                +                          "bid1_volume,open_price,high_price,"
                +                          "low_price,last_price,p_close,volume,turnover) "
                +                          "values"; 
        PreparedStatement ps = null;
        Connection con = null;
        try {  
            con = getConnector().getConnection();
            // ����sql��׺  
            StringBuffer suffix = new StringBuffer();  
            // ��������Ϊ���Զ��ύ  
            //conn.setAutoCommit(false);  
            // Statement st = conn.createStatement();  
            // ����st��pst�����Щ  
            ps = con.prepareStatement("");  
            for (TickData data : list) {
                String tmp_time = String.format("%08d",data.getDate())+" "+String.format("%06d",data.getTime());
                    // ����sql��׺  
                    suffix.append("('"
                            + data.getSymbol()   +"','"
                            + data.getCnName()   +"','"
                            + data.getExchCode() + "','"
                            + DateUtil.formatDate(DateUtil.parseDate(tmp_time, "yyyyMMdd HHmmss"),"yyyy-MM-dd HH:mm:ss") + "','"
                            + data.getAsk1Price() + "','"
                            + data.getAsk1Volume()+ "','"
                            + data.getBid1Price()+ "','"
                            + data.getBid1Volume()+ "','"
                            + data.getOpenPrice()+ "','"
                            + data.getHighPrice()+ "','"
                            + data.getLowPrice()+ "','"
                            + data.getLastPrice()+ "','"
                            + data.getpClose()+ "','"
                            + data.getVolume()+ "',"
                            + data.getTurnover()
                            +"),");  
            }  
            
            
            // ��������sql  
            String sql = prefix + suffix.substring(0, suffix.length() - 1);  
            // ���ִ��sql  
            ps.addBatch(sql);  
            // ִ�в���  
            //ps.executeBatch();  
            // �ύ����  
                
            // �����һ����ӵ�����  
            bl = ps.executeBatch().length != 0;
            // ͷ������  
            ps.close();  
            con.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        // ����ʱ��  
        //Long end = new Date().getTime();  
        // ��ʱ  
        //System.out.println("cast : " + (end - begin) / 1000 + " ms");
        return true;
    }
}
