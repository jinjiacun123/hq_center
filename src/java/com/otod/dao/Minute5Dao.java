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

public class Minute5Dao extends ParentDao {

    public List<KLineData> getBySymbol(String symbol, int count) {
        List<KLineData> list = new ArrayList<KLineData>();
        String sql = "select  * from minute5 where symbol=? order by quote_date desc,quote_time desc limit ? ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, symbol);
            ps.setInt(2, count);
            rs = ps.executeQuery();
            while (rs.next()) {
                KLineData klineData = new KLineData();
                klineData.setDataType(ApplicationConstant.DB_DATA);
                klineData.setId(rs.getInt("id"));
                klineData.setSymbol(rs.getString("symbol"));
                klineData.setQuoteDate(rs.getInt("quote_date"));
                klineData.setQuoteTime(rs.getInt("quote_time"));
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
        String sql = "select * from minute5 where symbol=? and quote_date>=? and quote_date<=? order by quote_date desc,quote_time desc";
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
                klineData.setQuoteTime(rs.getInt("quote_time"));
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
        String sql = "insert into minute5 (symbol,quote_date,quote_time,open_price,high_price,low_price,close_price,volume,turnover) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, data.getSymbol());
            ps.setInt(2, data.getQuoteDate());
            ps.setInt(3, data.getQuoteTime());
            ps.setDouble(4, data.getOpenPrice());
            ps.setDouble(5, data.getHighPrice());
            ps.setDouble(6, data.getLowPrice());
            ps.setDouble(7, data.getClosePrice());
            ps.setDouble(8, data.getVolume());
            ps.setDouble(9, data.getTurnover());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Connector.close(null, ps);
        }
    }

    public boolean batchSave(List<KLineData> list) {
        boolean bl = false;
        String sql = "insert into minute5 (symbol,quote_date,quote_time,open_price,high_price,low_price,close_price,volume,turnover) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (KLineData data : list) {
                ps.setString(1, data.getSymbol());
                ps.setInt(2, data.getQuoteDate());
                ps.setInt(3, data.getQuoteTime());
                ps.setDouble(4, data.getOpenPrice());
                ps.setDouble(5, data.getHighPrice());
                ps.setDouble(6, data.getLowPrice());
                ps.setDouble(7, data.getClosePrice());
                ps.setDouble(8, data.getVolume());
                ps.setDouble(9, data.getTurnover());
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
        String sql = "update minute5 set open_price=?,high_price=?,low_price=?,close_price=?,volume=?,turnover=? where symbol=? and quote_date=? and quote_time=?";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, data.getOpenPrice());
            ps.setDouble(2, data.getHighPrice());
            ps.setDouble(3, data.getLowPrice());
            ps.setDouble(4, data.getClosePrice());
            ps.setDouble(5, data.getVolume());
            ps.setDouble(6, data.getTurnover());
            ps.setString(7, data.getSymbol());
            ps.setInt(8, data.getQuoteDate());
            ps.setInt(9, data.getQuoteTime());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Connector.close(null, ps);
        }
    }

    public boolean batchUpdate(List<KLineData> list) {
        boolean bl = false;
        String sql = "update minute5 set open_price=?,high_price=?,low_price=?,close_price=?,volume=?,turnover=? where symbol=? and quote_date=? and quote_time=?";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (KLineData data : list) {
                ps.setDouble(1, data.getOpenPrice());
                ps.setDouble(2, data.getHighPrice());
                ps.setDouble(3, data.getLowPrice());
                ps.setDouble(4, data.getClosePrice());
                ps.setDouble(5, data.getVolume());
                ps.setDouble(6, data.getTurnover());
                ps.setString(7, data.getSymbol());
                ps.setInt(8, data.getQuoteDate());
                ps.setInt(9, data.getQuoteTime());
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
