package com.otod.util;

import java.util.ResourceBundle;

public class Config {

    //数据库配置文件资源包
    private static ResourceBundle rb_db;
    private static ResourceBundle rb_config;
    private static ResourceBundle rb_market;

    static {
        try {
            //设置数据库属性文件资源包文件名称
            rb_db = ResourceBundle.getBundle("db");
            rb_config = ResourceBundle.getBundle("config");
            rb_market = ResourceBundle.getBundle("market");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public final static String JDBC_DRIVERCLASSNAME = rb_db.getString("jdbc.driverClassName");
    public final static String JDBC_URL = rb_db.getString("jdbc.url");
    public final static String JDBC_USERNAME = rb_db.getString("jdbc.username");
    public final static String JDBC_PASSWORD = rb_db.getString("jdbc.password");
    public final static String DBF_PATH      = rb_config.getString("dbfPath");
    public final static String OUT_FILE      = rb_config.getString("outfile");
    public final static String FILE_SPLITE = rb_config.getString("fileSplite");
    public final static String DBF_SH      = rb_config.getString("dbfSh");
    public final static String DBF_SZ      = rb_config.getString("dbfSz");
    
    public final static int TYPE_SH = 0;
    public final static int TYPE_SZ = 1;

    public final static String MARKET_SH = rb_market.getString("sh");  
    public final static String MARKET_SZ = rb_market.getString("sz");
    /**
     *market type
     */
    /*
    public static enum MARKET_LIST {
        SH_A,//0-深圳a股票
        SH_B,//1-深圳b股票
        SZ_A,//3-上海a股票
        SZ_B //4-上海b股票
    };
    */
    
    /**
     * sort type
     */
    public static enum SORT_LIST{
        SORT_M,           //成交额
        SORT_UPDOWN,      //
        SORT_RAISE,       //涨跌幅
        SORT_AMPLITUDE,   //振幅
        SORT_TURNOVERRATE,//换手率
        SORT_EARMING      //市盈率
    };
}
