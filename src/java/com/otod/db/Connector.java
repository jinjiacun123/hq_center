package com.otod.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connector {

    protected Connection con = null;

    public Connection getConnection() {
        return con;
    }

    public void close() {
        try {
//            System.out.println("close:"+con.toString());
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
        }
    }

    public static void close(ResultSet rs, PreparedStatement ps) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
        }
    }

    public static void close(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
        }
    }
}
