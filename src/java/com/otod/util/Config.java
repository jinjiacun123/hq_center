package com.otod.util;

import java.util.ResourceBundle;

public class Config {

    //数据库配置文件资源包
    private static ResourceBundle rb_db;
    private static ResourceBundle rb_config;

    static {
        try {
            //设置数据库属性文件资源包文件名称
            rb_db = ResourceBundle.getBundle("db");
            rb_config = ResourceBundle.getBundle("config");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public final static String JDBC_DRIVERCLASSNAME = rb_db.getString("jdbc.driverClassName");
    public final static String JDBC_URL = rb_db.getString("jdbc.url");
    public final static String JDBC_USERNAME = rb_db.getString("jdbc.username");
    public final static String JDBC_PASSWORD = rb_db.getString("jdbc.password");
    public final static String DBF_PATH = rb_config.getString("dbfpath");
}
