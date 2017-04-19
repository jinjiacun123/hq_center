package com.otod.db;

import com.otod.util.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MysqlConnector extends Connector {

    private static InitialContext ctx = null;
    private static DataSource ds = null;

    /*
    static {
        if (ctx == null) {
            try {
                ctx = new InitialContext();

                ds = (DataSource) ctx.lookup("java:comp/env/LiuWeiStockWebServer");
            } catch (NamingException ex) {
                Logger.getLogger(MysqlConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
*/

    public static InitialContext getCtx() {
        return ctx;
    }

    public static void setCtx(InitialContext ctx) {
        MysqlConnector.ctx = ctx;
    }

    public static DataSource getDs() {
        return ds;
    }

    public static void setDs(DataSource ds) {
        MysqlConnector.ds = ds;
    }

    @Override
    public Connection getConnection() {
        try {
            if (con == null) {
                String driverClassName = Config.JDBC_DRIVERCLASSNAME;
                String url = Config.JDBC_URL;// 连接地址
                String user = Config.JDBC_USERNAME;// 用户名
                String password = Config.JDBC_PASSWORD;// 密码
                try {
                    Class.forName(driverClassName);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MysqlConnector.class.getName()).log(Level.SEVERE, null, ex);
                }
                con = DriverManager.getConnection(url, user, password);
                //con = DriverManager.getConnection("jdbc:mysql://192.168.1.233:3306/quote_gp","root","root");
                System.out.println("数据库连接成功!");                
            }
            /*
            if (con == null) {
                con = MysqlConnector.getDs().getConnection();
            }
*/
//            System.out.println(con.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库连接失败:"+e.getSQLState());
        }
        
        return con;
    }
}
