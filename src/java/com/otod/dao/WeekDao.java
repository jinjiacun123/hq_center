/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.dao;

import com.otod.bean.quote.kline.KLineData;
import com.otod.db.Connector;
import com.otod.util.ApplicationConstant;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeekDao extends ParentDao {

    public List<KLineData> getBySymbol(String symbol, int num) {
        List<KLineData> list = new ArrayList<KLineData>();
        String sql = "select * from week where symbol=? order by quote_date desc limit ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, symbol);
            ps.setInt(2, num);
            rs = ps.executeQuery();
            while (rs.next()) {
                KLineData klineData = new KLineData();
                klineData.setDataType(ApplicationConstant.DB_DATA);
                klineData.setId(rs.getInt("id"));
                klineData.setSymbol(rs.getString("symbol"));
                klineData.setQuoteDate(rs.getInt("quote_date"));
                klineData.setOpenPrice(rs.getDouble("open_price"));
                klineData.setHighPrice(rs.getDouble("high_price"));
                klineData.setLowPrice(rs.getDouble("low_price"));
                klineData.setClosePrice(rs.getDouble("close_price"));
                klineData.setVolume(rs.getDouble("volume"));
                klineData.setTurnover(rs.getDouble("turnover"));
                list.add(0, klineData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connector.close(rs, ps);
        }
        return list;
    }

    public List<KLineData> getBySymbol(String symbol, int startDate, int endDate) {
        List<KLineData> list = new ArrayList<KLineData>();
        String sql = "select * from week where symbol=? and quote_date>=? and quote_date<=? order by quote_date desc";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, symbol);
            ps.setInt(2, startDate);
            ps.setInt(3, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                KLineData klineData = new KLineData();
                klineData.setDataType(ApplicationConstant.DB_DATA);
                klineData.setId(rs.getInt("id"));
                klineData.setSymbol(rs.getString("symbol"));
                klineData.setQuoteDate(rs.getInt("quote_date"));
                klineData.setOpenPrice(rs.getDouble("open_price"));
                klineData.setHighPrice(rs.getDouble("high_price"));
                klineData.setLowPrice(rs.getDouble("low_price"));
                klineData.setClosePrice(rs.getDouble("close_price"));
                klineData.setVolume(rs.getDouble("volume"));
                klineData.setTurnover(rs.getDouble("turnover"));
                list.add(0, klineData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connector.close(rs, ps);
        }
        return list;
    }

    public void save(KLineData data) {
        String sql = "insert into week (symbol,quote_date,open_price,high_price,low_price,close_price,volume,turnover) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, data.getSymbol());
            ps.setInt(2, data.getQuoteDate());
            ps.setDouble(3, data.getOpenPrice());
            ps.setDouble(4, data.getHighPrice());
            ps.setDouble(5, data.getLowPrice());
            ps.setDouble(6, data.getClosePrice());
            ps.setDouble(7, data.getVolume());
            ps.setDouble(8, data.getTurnover());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Connector.close(null, ps);
        }
    }

    public boolean batchSave(List<KLineData> list) {
        boolean bl = false;
        String sql = "insert into week (symbol,quote_date,open_price,high_price,low_price,close_price,volume,turnover) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (KLineData data : list) {
                ps.setString(1, data.getSymbol());
                ps.setInt(2, data.getQuoteDate());
                ps.setDouble(3, data.getOpenPrice());
                ps.setDouble(4, data.getHighPrice());
                ps.setDouble(5, data.getLowPrice());
                ps.setDouble(6, data.getClosePrice());
                ps.setDouble(7, data.getVolume());
                ps.setDouble(8, data.getTurnover());
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

    public void update(KLineData data) {
        String sql = "update week set quote_date=?,open_price=?,high_price=?,low_price=?,close_price=?,volume=?,turnover=? where symbol=? and quote_date=?";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, data.getQuoteDate());
            ps.setDouble(2, data.getOpenPrice());
            ps.setDouble(3, data.getHighPrice());
            ps.setDouble(4, data.getLowPrice());
            ps.setDouble(5, data.getClosePrice());
            ps.setDouble(6, data.getVolume());
            ps.setDouble(7, data.getTurnover());
            ps.setString(8, data.getSymbol());
            ps.setInt(9, data.getPreQuoteDate());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Connector.close(null, ps);
        }
    }

    public boolean batchUpdate(List<KLineData> list) {
        boolean bl = false;
        String sql = "update week set quote_date=?,open_price=?,high_price=?,low_price=?,close_price=?,volume=?,turnover=? where symbol=? and quote_date=?";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (KLineData data : list) {
                ps.setInt(1, data.getQuoteDate());
                ps.setDouble(2, data.getOpenPrice());
                ps.setDouble(3, data.getHighPrice());
                ps.setDouble(4, data.getLowPrice());
                ps.setDouble(5, data.getClosePrice());
                ps.setDouble(6, data.getVolume());
                ps.setDouble(7, data.getTurnover());
                ps.setString(8, data.getSymbol());
                ps.setInt(9, data.getPreQuoteDate());
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
}
