/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.dao;

import com.otod.bean.quote.snapshot.ForexSnapshot;
import com.otod.db.Connector;
import com.otod.util.DateUtil;
import com.otod.util.StringUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ForexSnapshotDao extends ParentDao {

    public boolean delAll() {
        String sql = "delete from snapshot";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            return ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        } finally {
            Connector.close(rs, ps);
        }
        return false;
    }

    public List<ForexSnapshot> getList() {
        ForexSnapshot snapshot;
        String sql = "select * from snapshot";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ForexSnapshot> list = new ArrayList<ForexSnapshot>();
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                snapshot = new ForexSnapshot();
                snapshot.setSymbol(rs.getString("symbol"));
                snapshot.setCnName(rs.getString("cn_name"));
                snapshot.setExchCode(rs.getString("exch_code"));
                snapshot.setQuoteDate(Integer.parseInt(DateUtil.formatDate(new Date(rs.getTimestamp("date_time").getTime()), "yyyyMMdd")));
                snapshot.setQuoteTime(Integer.parseInt(DateUtil.formatDate(new Date(rs.getTimestamp("date_time").getTime()), "HHmmss")));
                snapshot.setAsk1Price(rs.getDouble("ask1_price"));
                snapshot.setAsk1Price(rs.getInt("ask1_volume"));
                snapshot.setBid1Price(rs.getDouble("bid1_price"));
                snapshot.setBid1Volume(rs.getInt("bid1_volume"));
                snapshot.setOpenPrice(rs.getDouble("open_price"));
                snapshot.setHighPrice(rs.getDouble("high_price"));
                snapshot.setLowPrice(rs.getDouble("low_price"));
                snapshot.setLastPrice(rs.getDouble("last_price"));
                snapshot.setpClose(rs.getDouble("p_close"));
                snapshot.setVolume(rs.getDouble("volume"));
                snapshot.setTurnover(rs.getDouble("turnover"));
                list.add(snapshot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connector.close(rs, ps);
        }
        return list;
    }

    public List<ForexSnapshot> getList(String symbol, Date start, Date end) {
        ForexSnapshot snapshot;
        String sql = "select * from snapshot where symbol=? and date_time>? and date_time<?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ForexSnapshot> list = new ArrayList<ForexSnapshot>();
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, symbol);
            ps.setTimestamp(2, new Timestamp(start.getTime()));
            ps.setTimestamp(3, new Timestamp(end.getTime()));
            rs = ps.executeQuery();
            while (rs.next()) {
                snapshot = new ForexSnapshot();
                snapshot.setSymbol(rs.getString("symbol"));
                snapshot.setCnName(rs.getString("cn_name"));
                snapshot.setExchCode(rs.getString("exch_code"));
                snapshot.setQuoteDate(Integer.parseInt(DateUtil.formatDate(new Date(rs.getTimestamp("date_time").getTime()), "yyyyMMdd")));
                snapshot.setQuoteTime(Integer.parseInt(DateUtil.formatDate(new Date(rs.getTimestamp("date_time").getTime()), "HHmmss")));
                snapshot.setAsk1Price(rs.getDouble("ask1_price"));
                snapshot.setAsk1Price(rs.getInt("ask1_volume"));
                snapshot.setBid1Price(rs.getDouble("bid1_price"));
                snapshot.setBid1Volume(rs.getInt("bid1_volume"));
                snapshot.setOpenPrice(rs.getDouble("open_price"));
                snapshot.setHighPrice(rs.getDouble("high_price"));
                snapshot.setLowPrice(rs.getDouble("low_price"));
                snapshot.setLastPrice(rs.getDouble("last_price"));
                snapshot.setpClose(rs.getDouble("p_close"));
                snapshot.setVolume(rs.getDouble("volume"));
                snapshot.setTurnover(rs.getDouble("turnover"));
                list.add(snapshot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connector.close(rs, ps);
        }
        return list;
    }

    public boolean saveBatch(List<ForexSnapshot> list) throws SQLException {
        boolean bl = false;
        String sql = "insert into snapshot (symbol,cn_name,exch_code,date_time,ask1_price,ask1_volume,bid1_price,bid1_volume,open_price,high_price,low_price,last_price,p_close,volume,turnover) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (ForexSnapshot snapshot : list) {
                ps.setString(1, snapshot.getSymbol());
                ps.setString(2, snapshot.getCnName());
                ps.setString(3, snapshot.getExchCode());
                Date dateTime = DateUtil.parseDate("" + snapshot.getQuoteDate() + " " + StringUtil.completeByBefore("" + snapshot.getQuoteTime(), 6, "0"), "yyyyMMdd HHmmss");
                Timestamp timeStamp = new Timestamp(dateTime.getTime());
                ps.setTimestamp(4, timeStamp);
                ps.setDouble(5, snapshot.getAsk1Price());
                ps.setInt(6, snapshot.getAsk1Volume());
                ps.setDouble(7, snapshot.getBid1Price());
                ps.setInt(8, snapshot.getBid1Volume());
                ps.setDouble(9, snapshot.getOpenPrice());
                ps.setDouble(10, snapshot.getHighPrice());
                ps.setDouble(11, snapshot.getLowPrice());
                ps.setDouble(12, snapshot.getLastPrice());
                ps.setDouble(13, snapshot.getpClose());
                ps.setDouble(14, snapshot.getVolume());
                ps.setDouble(15, snapshot.getTurnover());
                ps.addBatch();
            }
            bl = ps.executeBatch().length != 0;
        } finally {
            Connector.close(null, ps);
        }
        return bl;
    }

    public boolean updateBatch(List<ForexSnapshot> list) {
        boolean bl = false;
        String sql = "update snapshot set cn_name=?,date_time=?,ask1_price=?,ask1_volume=?,bid1_price=?,bid1_volume=?,open_price=?,high_price=?,low_price=?,last_price=?,p_close=?,p_settlement_price=?,upper_limit_price=?,lower_limit_price=?,volume=?,turnover=?,open_interest=?,p_open_interest=? where symbol=?";
        PreparedStatement ps = null;
        Connection con = null;
        try {
            con = getConnector().getConnection();
            ps = con.prepareStatement(sql);
            for (ForexSnapshot snapshot : list) {
                ps.setString(1, snapshot.getCnName());
                Date dateTime = DateUtil.parseDate("" + snapshot.getQuoteDate() + " " + StringUtil.completeByBefore("" + snapshot.getQuoteTime(), 6, "0"), "yyyyMMdd HHmmss");
                Timestamp timeStamp = new Timestamp(dateTime.getTime());
                ps.setTimestamp(2, timeStamp);
                ps.setDouble(3, snapshot.getAsk1Price());
                ps.setInt(4, snapshot.getAsk1Volume());
                ps.setDouble(5, snapshot.getBid1Price());
                ps.setInt(6, snapshot.getBid1Volume());
                ps.setDouble(7, snapshot.getOpenPrice());
                ps.setDouble(8, snapshot.getHighPrice());
                ps.setDouble(9, snapshot.getLowPrice());
                ps.setDouble(10, snapshot.getLastPrice());
                ps.setDouble(11, snapshot.getpClose());
                ps.setDouble(12, snapshot.getVolume());
                ps.setDouble(13, snapshot.getTurnover());
                ps.setString(14, snapshot.getSymbol());
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
