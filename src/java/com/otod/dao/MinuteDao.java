/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.dao;

import com.otod.bean.quote.minute.MinuteData;
import com.otod.db.Connector;
import com.otod.util.ApplicationConstant;
import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MinuteDao extends ParentDao {

    public List<MinuteData> getBySymbolAndDate(String symbol, int trade_date) {
        List<MinuteData> list = new ArrayList<MinuteData>();
        String sql = "select * from minute where symbol=? and trade_date=? order by quote_date DESC,quote_time DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, symbol);
            ps.setInt(2, trade_date);
            rs = ps.executeQuery();
            while (rs.next()) {
                MinuteData minuteData = new MinuteData();
                minuteData.setDataType(ApplicationConstant.DB_DATA);
                minuteData.setId(rs.getInt("id"));
                minuteData.setSymbol(rs.getString("symbol"));
                minuteData.setTradeDate(rs.getInt("trade_date"));
                minuteData.setQuoteDate(rs.getInt("quote_date"));
                minuteData.setQuoteTime(rs.getInt("quote_time"));
                minuteData.setpClose(rs.getDouble("p_close"));
                minuteData.setClosePrice(rs.getDouble("close_price"));
                minuteData.setVolume(rs.getDouble("volume"));
                minuteData.setTurnover(rs.getDouble("turnover"));
                list.add(0, minuteData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connector.close(rs, ps);
        }
        return list;
    }

    public void save(MinuteData data) {
        String sql = "insert into minute (symbol,trade_date,quote_date,quote_time,p_close,close_price,volume,turnover) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, data.getSymbol());
            ps.setInt(2, data.getTradeDate());
            ps.setInt(3, data.getQuoteDate());
            ps.setInt(4, data.getQuoteTime());
            ps.setDouble(5, data.getpClose());
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

    public boolean batchSave(List<MinuteData> list) {
        boolean bl = false;
        String sql = "insert into minute (symbol,trade_date,quote_date,quote_time,p_close,close_price,volume,turnover) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (MinuteData data : list) {
                ps.setString(1, data.getSymbol());
                ps.setInt(2, data.getTradeDate());
                ps.setInt(3, data.getQuoteDate());
                ps.setInt(4, data.getQuoteTime());
                ps.setDouble(5, data.getpClose());
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

    public void update(MinuteData data) {
        String sql = "update minute set close_price=?,volume=?,turnover=? where symbol=? and quote_date=? and quote_time=?";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, data.getClosePrice());
            ps.setDouble(2, data.getVolume());
            ps.setDouble(3, data.getTurnover());
            ps.setString(4, data.getSymbol());
            ps.setInt(5, data.getQuoteDate());
            ps.setInt(6, data.getQuoteTime());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Connector.close(null, ps);
        }
    }

    public boolean batchUpdate(List<MinuteData> list) {
        boolean bl = false;
        String sql = "update minute set close_price=?,volume=?,turnover=? where symbol=? and quote_date=? and quote_time=?";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (MinuteData data : list) {
                ps.setDouble(1, data.getClosePrice());
                ps.setDouble(2, data.getVolume());
                ps.setDouble(3, data.getTurnover());
                ps.setString(4, data.getSymbol());
                ps.setInt(5, data.getQuoteDate());
                ps.setInt(6, data.getQuoteTime());
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
