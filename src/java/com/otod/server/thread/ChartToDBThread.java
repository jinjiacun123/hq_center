/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.thread;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.kline.KLineData;
import com.otod.bean.quote.minute.MinuteData;
import com.otod.dao.DayDao;
import com.otod.dao.Minute15Dao;
import com.otod.dao.Minute1Dao;
import com.otod.dao.Minute30Dao;
import com.otod.dao.Minute5Dao;
import com.otod.dao.Minute60Dao;
import com.otod.dao.MinuteDao;
import com.otod.dao.MonthDao;
import com.otod.dao.WeekDao;
import com.otod.db.Connector;
import com.otod.db.MysqlConnector;

import com.otod.util.ApplicationConstant;
import java.sql.Connection;

/**
 *
 * @author Administrator
 */
public class ChartToDBThread extends Thread {

    public ChartToDBThread() {
    }

    @Override
    public void run() {
        // K线行情或分时行情对象
        Object data;
        while (true) {
            try {
                // 获取K线DB队列中的行情对象
                data = ServerContext.getChartToDBQueue().take();
//                System.out.println("图表存库size:"+ServerContext.getChartToDBQueue().size());
                //如果当前服务器为非主服务器状态，则不同步到数据
//                if (ServerContext.isServerActive() == false) {
//                    continue;
//                }

                // 如果此对象为K线行情对象
                if (data instanceof KLineData) {
                    // k线数据库处理
                    doKLine((KLineData) data);
                } else if (data instanceof MinuteData) {// 如果此对象为分时行情对象
                    // 分时数据数库处理
                    doMinute((MinuteData) data);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Description: 分时数据数库处理
     *
     * @Version1.0 2014-12-8 下午03:45:19
     * @param minuteData 分时行情数据
     */
    public void doMinute(MinuteData minuteData) {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            // 分时行情表数据库操作对象
            MinuteDao minuteDao = new MinuteDao();
            minuteDao.setConnector(connector);

            if (minuteData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                // 如果此分时行情的数据库操作标识为”保存“，则向分时行情表中插入此数据
                minuteDao.save(minuteData);
            } else if (minuteData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                // 如果此分时行情的数据库操作标识为”更新“，则向分时行情表中更新此数据
                minuteDao.update(minuteData);
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }

    }

    /**
     * Description: k线数据库处理
     *
     * @Version1.0 2014-12-8 下午03:45:28
     * @param klineData K线行情数据
     */
    public void doKLine(KLineData klineData) {
        Connector connector = null;
        try {
            connector = new MysqlConnector();
            // 对不同K线周期的数据进行存库或者更新数据库
            if (klineData.getPeriod() == ApplicationConstant.MINUTE1) {
                // 对5分线数据处理
                Minute1Dao minute1Dao = new Minute1Dao();
                minute1Dao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此5分行情的数据库操作标识为”保存“，则向5分行情表中插入此数据
                    minute1Dao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此5分行情的数据库操作标识为”更新“，则向5分行情表中更新此数据
                    minute1Dao.update(klineData);
                }
            } else if (klineData.getPeriod() == ApplicationConstant.MINUTE5) {
                // 对5分线数据处理
                Minute5Dao minute5Dao = new Minute5Dao();
                minute5Dao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此5分行情的数据库操作标识为”保存“，则向5分行情表中插入此数据
                    minute5Dao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此5分行情的数据库操作标识为”更新“，则向5分行情表中更新此数据
                    minute5Dao.update(klineData);
                }

            } else if (klineData.getPeriod() == ApplicationConstant.MINUTE15) {
                // 对15分线数据处理
                Minute15Dao minute15Dao = new Minute15Dao();
                minute15Dao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此15分行情的数据库操作标识为”保存“，则向15分行情表中插入此数据
                    minute15Dao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此15分行情的数据库操作标识为”更新“，则向15分行情表中更新此数据
                    minute15Dao.update(klineData);
                }
            } else if (klineData.getPeriod() == ApplicationConstant.MINUTE30) {
                // 对30分线数据处理
                Minute30Dao minute30Dao = new Minute30Dao();
                minute30Dao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此30分行情的数据库操作标识为”保存“，则向30分行情表中插入此数据
                    minute30Dao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此30分行情的数据库操作标识为”更新“，则向30分行情表中更新此数据
                    minute30Dao.update(klineData);
                }
            } else if (klineData.getPeriod() == ApplicationConstant.MINUTE60) {
                // 对30分线数据处理
                Minute60Dao minute60Dao = new Minute60Dao();
                minute60Dao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此30分行情的数据库操作标识为”保存“，则向30分行情表中插入此数据
                    minute60Dao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此30分行情的数据库操作标识为”更新“，则向30分行情表中更新此数据
                    minute60Dao.update(klineData);
                }
            } else if (klineData.getPeriod() == ApplicationConstant.DAY) {
                // 对日线数据处理
                DayDao dayDao = new DayDao();
                dayDao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此日行情的数据库操作标识为”保存“，则向日行情表中插入此数据
                    dayDao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此日行情的数据库操作标识为”保存“，则向日行情表中插入此数据
                    dayDao.update(klineData);
                }
            } else if (klineData.getPeriod() == ApplicationConstant.WEEK) {
                // 对周线数据处理
                WeekDao weekDao = new WeekDao();
                weekDao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此日行情的数据库操作标识为”保存“，则向日行情表中插入此数据
                    weekDao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此日行情的数据库操作标识为”保存“，则向日行情表中插入此数据
                    weekDao.update(klineData);
                }
            } else if (klineData.getPeriod() == ApplicationConstant.MONTH) {
                // 对月线数据处理
                MonthDao monthDao = new MonthDao();
                monthDao.setConnector(connector);
                if (klineData.getDbOperType() == ApplicationConstant.DB_SAVE) {
                    // 如果此日行情的数据库操作标识为”保存“，则向日行情表中插入此数据
                    monthDao.save(klineData);
                } else if (klineData.getDbOperType() == ApplicationConstant.DB_UPDATE) {
                    // 如果此日行情的数据库操作标识为”保存“，则向日行情表中插入此数据
                    monthDao.update(klineData);
                }
            }
        } finally {
            if (connector != null) {
                connector.close();
            }
        }
    }
}