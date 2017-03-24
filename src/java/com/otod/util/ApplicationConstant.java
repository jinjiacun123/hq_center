/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;

/**
 *
 * @author Administrator
 */
public class ApplicationConstant {

    /**
     * ******** K线间隔时间常量 *********
     */
    public final static int MINUTE1 = 1;//1分钟
    public final static int MINUTE3 = 3;//3分钟
    public final static int MINUTE5 = 5;//5分钟
    public final static int MINUTE10 = 10;//10分钟
    public final static int MINUTE15 = 15;//15分钟
    public final static int MINUTE30 = 30;//30分钟
    public final static int MINUTE60 = 60;//60分钟
    public final static int MINUTE4H = 240;//240分钟
    public final static int DAY = 100;//日线
    public final static int WEEK = 200;//日线
    public final static int MONTH = 300;//日线
    public final static int KLINE_NUMBER = 300;
    public final static int LIST         = 1;
    /**
     * ******** 数据库、内存标志位 *********
     */
    public final static int MEMORY_DATA = 0;//代表内存数据
    public final static int DB_DATA = 1;//代表数据库数据
    /**
     * ******** 数据库操作标志位 *********
     */
    public final static int DB_SAVE = 1;//代表要进行数据库插入操作
    public final static int DB_UPDATE = 2; //代表要进入数据库更新操作
    /**
     * 证券类型
     */
    public static String Stock = "01";//股票
    public static String Index = "02";//指数
    public static String Futures = "03";//期货
    public static String Forex = "04";//外汇
    public static String Fund = "05";//基金
    public static String Bond = "06";//债券
}
