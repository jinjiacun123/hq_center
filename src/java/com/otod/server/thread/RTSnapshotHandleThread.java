/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.server.thread;

import com.otod.bean.ServerContext;
import com.otod.bean.quote.exchange.ExchangeData;
import com.otod.bean.quote.kline.KLineData;
import com.otod.bean.quote.kline.KLineQueue;
import com.otod.bean.quote.master.MasterData;
import com.otod.bean.quote.minute.MinuteData;
import com.otod.bean.quote.minute.MinuteQueue;
import com.otod.bean.quote.snapshot.ForexSnapshot;
import com.otod.bean.quote.snapshot.Snapshot;
import com.otod.bean.quote.snapshot.SnapshotQueue;
import com.otod.bean.quote.snapshot.StockSnapshot;
import com.otod.bean.quote.tick.TickData;
import com.otod.bean.quote.tick.TickQueue;
import com.otod.bean.quote.tradetime.PeriodTime;
import com.otod.util.ApplicationConstant;
import com.otod.util.DateUtil;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class RTSnapshotHandleThread extends Thread {

    @Override
    public void run() {
        Snapshot snapshot = null;
        Snapshot hSnapshot = null;
        MasterData masterData = null;
        ExchangeData exchangeData = null;
        while (true) {
            try {
                snapshot = ServerContext.getRtSnapshotQueue().take();
                masterData = ServerContext.getMasterMap().get(snapshot.getSymbol());

                if (masterData == null) {
                    continue;
                }

                if (!ServerContext.isAuthorizeFlag()) {
                    return;
                }

                snapshot.setTradeDate(masterData.tradeDate);

                hSnapshot = ServerContext.getSnapshotMap().get(snapshot.getSymbol());
                if (hSnapshot == null) {
                    hSnapshot = snapshot;
                    hSnapshot.updateSnapshot(snapshot);
                    ServerContext.getSnapshotMap().put(hSnapshot.getSymbol(), hSnapshot);
//                    doTick(snapshot.getSymbol());
//                    doCalMinute(snapshot.getSymbol());

//                    ServerContext.getQuoteToDBQueue().add(snapshot);

                } else {
                    if (!masterData.isTrade) {
                        continue;
                    }


                    PeriodTime periodTime = ServerContext.getPeriodTimeMap().get(masterData.tradeTimeKey + "_minite1");
                    if (periodTime == null) {
                        periodTime = new PeriodTime();
                        periodTime.tradeTimes = masterData.tradeTimes;
                        periodTime.period = 1;
                        periodTime.init();
                        ServerContext.getPeriodTimeMap().put(masterData.tradeTimeKey + "_minite1", periodTime);
                    }
                    int idx = periodTime.findTime(snapshot.getQuoteTime() / 100);
                    if (idx < 0) {
                        continue;
                    }
//                    doTick(snapshot.getSymbol());
                    hSnapshot.updateSnapshot(snapshot);
//                    ServerContext.getQuoteToDBQueue().add(hSnapshot);
                    if (snapshot.getLastPrice() == 0) {
                        continue;
                    }
//                    System.out.println(snapshot.getSymbol()+"||"+((FuturesSnapshot)snapshot).getVarietyID());

                    doCalMinute(snapshot.getSymbol());
                    doCalMinuteKLine(snapshot.getSymbol(), ApplicationConstant.MINUTE1);
                    doCalMinuteKLine(snapshot.getSymbol(), ApplicationConstant.MINUTE5);
                    doCalMinuteKLine(snapshot.getSymbol(), ApplicationConstant.MINUTE15);
                    doCalMinuteKLine(snapshot.getSymbol(), ApplicationConstant.MINUTE30);
                    doCalMinuteKLine(snapshot.getSymbol(), ApplicationConstant.MINUTE60);
//                    doCalMinuteKLine(snapshot.getSymbol(), ApplicationConstant.MINUTE4H);
                    doDayKLine(snapshot.getSymbol());
                    doWeekKLine(snapshot.getSymbol());
                    doMonthKLine(snapshot.getSymbol());
                    doSnapshotList(snapshot.getSymbol());
                    snapshot = null;

                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void doTick(String symbol) {
        ForexSnapshot snapshot = (ForexSnapshot) ServerContext.getSnapshotMap().get(symbol);
        TickQueue tickQueue = ServerContext.getTickMap().get(symbol);
        if (tickQueue == null) {
            tickQueue = new TickQueue();
            tickQueue.setSymbol(snapshot.getSymbol());
            tickQueue.setpClose(snapshot.getpClose());
            ServerContext.getTickMap().put(tickQueue.getSymbol(), tickQueue);
        }
        TickData tickData = new TickData();
        tickData.setTime(snapshot.getQuoteTime());
        tickData.setPrice(snapshot.getLastPrice());
        tickData.setVolume(snapshot.getLastVolume());
        tickQueue.getQueue().add(tickData);
        while (tickQueue.getQueue().size() > 20) {
            tickQueue.getQueue().remove(0);
        }
    }

    private void doCalMinute(String symbol) {
        MinuteQueue minQueue = ServerContext.getMinuteMap().get(symbol);
        StockSnapshot snapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(symbol);
        if (minQueue == null) {
            minQueue = new MinuteQueue();
            minQueue.setSymbol(symbol);
            minQueue.setpClose(snapshot.getpClose());
            ServerContext.getMinuteMap().put(snapshot.getSymbol(), minQueue);
        }
        MinuteData minData = null;
        int hm = (int) (snapshot.getQuoteTime() / 100);
        if (minQueue.getQueue().isEmpty()) {
            minData = new MinuteData();
            minData.setSymbol(snapshot.getSymbol());
            minData.setTradeDate(snapshot.getTradeDate());
            minData.setQuoteDate(snapshot.getQuoteDate());
            minData.setQuoteTime(hm);
            minData.setpClose(snapshot.getpClose());
            minData.setClosePrice(snapshot.getLastPrice());
            minData.setVolume(snapshot.getLastVolume());
            minData.setTurnover(snapshot.getLastTurnover());
            minQueue.getQueue().add(minData);
            // 提交任务同步到数据库
            if (minData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                minData.setDataType(ApplicationConstant.DB_DATA);
                minData.setDbOperType(ApplicationConstant.DB_SAVE);
                ServerContext.getChartToDBQueue().add(minData.clone());
            }
        } else {
            MinuteData pMinuteData = minQueue.getQueue().get(minQueue.getQueue().size() - 1);

            if (pMinuteData.getQuoteTime() == hm) {
                pMinuteData.setClosePrice(snapshot.getLastPrice());
                pMinuteData.setVolume(pMinuteData.getVolume() + snapshot.getLastVolume());
                pMinuteData.setTurnover(pMinuteData.getTurnover() + snapshot.getLastTurnover());
            } else {
                // 转分钟提交任务更新最后一分钟数据到数据库
                if (pMinuteData.getDataType() == ApplicationConstant.DB_DATA) {
                    pMinuteData.setDataType(ApplicationConstant.DB_DATA);
                    pMinuteData.setDbOperType(ApplicationConstant.DB_UPDATE);
                    ServerContext.getChartToDBQueue().add(pMinuteData.clone());
                }
                minData = new MinuteData();
                minData.setSymbol(snapshot.getSymbol());
                minData.setTradeDate(snapshot.getTradeDate());
                minData.setQuoteDate(snapshot.getQuoteDate());
                minData.setQuoteTime(hm);
                minData.setpClose(snapshot.getpClose());
                minData.setClosePrice(snapshot.getLastPrice());
                minData.setVolume(snapshot.getLastVolume());
                minData.setTurnover(snapshot.getLastTurnover());
                minQueue.getQueue().add(minData);
                // 提交任务同步到数据库
                if (minData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                    minData.setDataType(ApplicationConstant.DB_DATA);
                    minData.setDbOperType(ApplicationConstant.DB_SAVE);
                    ServerContext.getChartToDBQueue().add(minData.clone());
                }
            }
        }
    }

    private void doCalMinuteKLine(String symbol, int period) {
        KLineQueue klineQueue = null;
        StockSnapshot snapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(symbol);
        if (period == ApplicationConstant.MINUTE1) {
            klineQueue = ServerContext.getMinute1Map().get(snapshot.getSymbol());
            if (klineQueue == null) {
                klineQueue = new KLineQueue();
                klineQueue.setSymbol(snapshot.getSymbol());
                ServerContext.getMinute1Map().put(snapshot.getSymbol(), klineQueue);
            }
        } else if (period == ApplicationConstant.MINUTE5) {
            klineQueue = ServerContext.getMinute5Map().get(snapshot.getSymbol());
            if (klineQueue == null) {
                klineQueue = new KLineQueue();
                ServerContext.getMinute5Map().put(snapshot.getSymbol(), klineQueue);
            }
        } else if (period == ApplicationConstant.MINUTE15) {
            klineQueue = ServerContext.getMinute15Map().get(snapshot.getSymbol());
            if (klineQueue == null) {
                klineQueue = new KLineQueue();
                ServerContext.getMinute15Map().put(snapshot.getSymbol(), klineQueue);
            }
        } else if (period == ApplicationConstant.MINUTE30) {
            klineQueue = ServerContext.getMinute30Map().get(snapshot.getSymbol());
            if (klineQueue == null) {
                klineQueue = new KLineQueue();
                ServerContext.getMinute30Map().put(snapshot.getSymbol(), klineQueue);
            }
        } else if (period == ApplicationConstant.MINUTE60) {
            klineQueue = ServerContext.getMinute60Map().get(snapshot.getSymbol());
            if (klineQueue == null) {
                klineQueue = new KLineQueue();
                ServerContext.getMinute60Map().put(snapshot.getSymbol(), klineQueue);
            }
        }
        MasterData masterData = ServerContext.getMasterMap().get(symbol);
        String periodKey = masterData.tradeTimeKey + "_minite" + period;
        PeriodTime periodTime = ServerContext.getPeriodTimeMap().get(periodKey);
        // 生成了本周期所有的时间点
        if (periodTime == null) {
            periodTime = new PeriodTime();
            periodTime.tradeTimes = masterData.tradeTimes;
            periodTime.period = period;
            periodTime.init();
            ServerContext.getPeriodTimeMap().put(periodKey, periodTime);
        }

        // 查找当前时间属于哪个周期
        int idx = periodTime.findTime(snapshot.getQuoteTime() / 100);
        int hm = 0;// HHmm时间
        if (idx >= 0) {
            hm = periodTime.periodTimeList.get(idx);
        } else {
            return;
        }
        int date = snapshot.getQuoteDate();
        // 计算时间周期遇到凌晨跨天的时候，下一个周期应该是下一个天的数据
        if (hm == 0 && period > 1) {
            Date hDate = DateUtil.parseDate(String.valueOf(date), "yyyyMMdd");
            int year = Integer.parseInt(DateUtil.formatDate(hDate, "yyyy"));
            int month = Integer.parseInt(DateUtil.formatDate(hDate, "MM"));
            int day = Integer.parseInt(DateUtil.formatDate(hDate, "dd"));
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DATE, day);
            cal.add(Calendar.DATE, 1);
            Date fDate = cal.getTime();
            date = Integer.parseInt(DateUtil.formatDate(fDate, "yyyyMMdd"));
        }

        KLineData klineData;

        if (klineQueue.getQueue().isEmpty()) {
            klineData = new KLineData();
            klineData.setSymbol(snapshot.getSymbol());
            klineData.setQuoteDate(snapshot.getQuoteDate());
            klineData.setQuoteTime(hm);
            klineData.setOpenPrice(snapshot.getLastPrice());
            klineData.setHighPrice(snapshot.getLastPrice());
            klineData.setLowPrice(snapshot.getLastPrice());
            klineData.setClosePrice(snapshot.getLastPrice());
            klineData.setVolume(snapshot.getLastVolume());
            klineData.setTurnover(snapshot.getLastTurnover());
            klineQueue.getQueue().add(klineData);
            // 提交任务同步到数据库
            if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                klineData.setDataType(ApplicationConstant.DB_DATA);
                klineData.setPeriod(period);
                klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                ServerContext.getChartToDBQueue().add(klineData.clone());
            }
        } else {
            KLineData pKLineData = klineQueue.getQueue().get(klineQueue.getQueue().size() - 1);
            // 计算分钟K线的时间对比是否属于同一周期范围内，在同一周期更新数据，不在同一周期新增一笔数据
            if (pKLineData.getQuoteTime() == hm) {

                pKLineData.setHighPrice(Math.max(snapshot.getLastPrice(), pKLineData.getHighPrice()));
                pKLineData.setLowPrice(Math.min(snapshot.getLastPrice(), pKLineData.getLowPrice()));
                pKLineData.setClosePrice(snapshot.getLastPrice());
                pKLineData.setVolume(pKLineData.getVolume() + snapshot.getLastVolume());
                pKLineData.setTurnover(pKLineData.getTurnover() + snapshot.getLastTurnover());

//                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
//                    pKLineData.setPeriod(period);
//                    pKLineData.setDataType(ApplicationConstant.DB_DATA);
//                    pKLineData.setDbOperType(ApplicationConstant.DB_UPDATE);
//                    ServerContext.getChartToDBQueue().add(pKLineData.clone());
//                }

            } else {

                // 转周期K线提交任务更新最后一分钟数据到数据库
                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
                    pKLineData.setPeriod(period);
                    pKLineData.setDataType(ApplicationConstant.DB_DATA);
                    pKLineData.setDbOperType(ApplicationConstant.DB_UPDATE);
                    ServerContext.getChartToDBQueue().add(pKLineData.clone());
                }

                klineData = new KLineData();
                klineData.setSymbol(snapshot.getSymbol());
                klineData.setQuoteDate(snapshot.getQuoteDate());
                klineData.setQuoteTime(hm);
                klineData.setOpenPrice(snapshot.getLastPrice());
                klineData.setHighPrice(snapshot.getLastPrice());
                klineData.setLowPrice(snapshot.getLastPrice());
                klineData.setClosePrice(snapshot.getLastPrice());
                klineData.setVolume(snapshot.getLastVolume());
                klineData.setTurnover(snapshot.getLastTurnover());
                klineQueue.getQueue().add(klineData);


                // 提交任务同步到数据库
                if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                    klineData.setDataType(ApplicationConstant.DB_DATA);
                    klineData.setPeriod(period);
                    klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                    ServerContext.getChartToDBQueue().add(klineData.clone());
                }
            }
        }
    }

    private void doDayKLine(String symbol) {
        StockSnapshot snapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(symbol);
        KLineQueue klineQueue = ServerContext.getDayMap().get(snapshot.getSymbol());
        MasterData masterData = ServerContext.getMasterMap().get(snapshot.getSymbol());
        if (klineQueue == null) {
            klineQueue = new KLineQueue();
            klineQueue.setSymbol(snapshot.getSymbol());
            ServerContext.getDayMap().put(symbol, klineQueue);
        }
        if (klineQueue.getQueue().isEmpty()) {
            KLineData klineData = new KLineData();
            klineData.setSymbol(snapshot.getSymbol());
            klineData.setQuoteDate(masterData.tradeDate);
            klineData.setOpenPrice(snapshot.getOpenPrice());
            klineData.setHighPrice(snapshot.getHighPrice());
            klineData.setLowPrice(snapshot.getLowPrice());
            klineData.setClosePrice(snapshot.getLastPrice());
            klineData.setVolume(snapshot.getVolume());
            klineData.setTurnover(snapshot.getTurnover());
            klineQueue.getQueue().add(klineData);
            // 提交任务同步到数据库
            if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                klineData.setDataType(ApplicationConstant.DB_DATA);
                klineData.setPeriod(ApplicationConstant.DAY);
                klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                ServerContext.getChartToDBQueue().add(klineData.clone());
            }
        } else {
            KLineData pKLineData = klineQueue.getQueue().get(klineQueue.getQueue().size() - 1);
            if (pKLineData.getQuoteDate() == snapshot.getTradeDate()) {
                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
                    if (pKLineData.getPreQuoteDate() == 0) {
                        pKLineData.setPreQuoteDate(pKLineData.getQuoteDate());
                    }
                }
                pKLineData.setQuoteDate(masterData.tradeDate);
                pKLineData.setOpenPrice(snapshot.getOpenPrice());
                pKLineData.setHighPrice(snapshot.getHighPrice());
                pKLineData.setLowPrice(snapshot.getLowPrice());
                pKLineData.setClosePrice(snapshot.getLastPrice());
                pKLineData.setVolume(snapshot.getVolume());
                pKLineData.setTurnover(snapshot.getTurnover());


//                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
//                    pKLineData.setPeriod(ApplicationConstant.DAY);
//                    pKLineData.setDataType(ApplicationConstant.DB_DATA);
//                    pKLineData.setDbOperType(ApplicationConstant.DB_UPDATE);
//                    ServerContext.getChartToDBQueue().add(pKLineData.clone());
//                }

            } else {
                // 转周期K线提交任务更新最后一分钟数据到数据库
                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
                    pKLineData.setPeriod(ApplicationConstant.DAY);
                    pKLineData.setDataType(ApplicationConstant.DB_DATA);
                    pKLineData.setDbOperType(ApplicationConstant.DB_UPDATE);
                    ServerContext.getChartToDBQueue().add(pKLineData.clone());
                }

                KLineData klineData = new KLineData();
                klineData.setSymbol(snapshot.getSymbol());
                klineData.setQuoteDate(masterData.tradeDate);
                klineData.setOpenPrice(snapshot.getOpenPrice());
                klineData.setHighPrice(snapshot.getHighPrice());
                klineData.setLowPrice(snapshot.getLowPrice());
                klineData.setClosePrice(snapshot.getLastPrice());
                klineData.setVolume(snapshot.getVolume());
                klineData.setTurnover(snapshot.getTurnover());
                klineQueue.getQueue().add(klineData);
                // 提交任务同步到数据库
                if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                    klineData.setDataType(ApplicationConstant.DB_DATA);
                    klineData.setPeriod(ApplicationConstant.DAY);
                    klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                    ServerContext.getChartToDBQueue().add(klineData.clone());
                }
            }
        }
    }

    private void doWeekKLine(String symbol) {
        StockSnapshot snapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(symbol);
        KLineQueue klineQueue = ServerContext.getWeekMap().get(snapshot.getSymbol());
        MasterData masterData = ServerContext.getMasterMap().get(snapshot.getSymbol());
        if (klineQueue == null) {
            klineQueue = new KLineQueue();
            klineQueue.setSymbol(snapshot.getSymbol());
            ServerContext.getWeekMap().put(symbol, klineQueue);
        }
        if (klineQueue.getQueue().isEmpty()) {
            KLineData klineData = new KLineData();
            klineData.setSymbol(snapshot.getSymbol());
            klineData.setQuoteDate(masterData.tradeDate);
            klineData.setOpenPrice(snapshot.getOpenPrice());
            klineData.setHighPrice(snapshot.getHighPrice());
            klineData.setLowPrice(snapshot.getLowPrice());
            klineData.setClosePrice(snapshot.getLastPrice());
            klineData.setVolume(snapshot.getVolume());
            klineData.setTurnover(snapshot.getTurnover());
            klineQueue.getQueue().add(klineData);
            // 提交任务同步到数据库
            if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                klineData.setDataType(ApplicationConstant.DB_DATA);
                klineData.setPeriod(ApplicationConstant.WEEK);
                klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                ServerContext.getChartToDBQueue().add(klineData.clone());
            }
        } else {
            KLineData pKLineData = klineQueue.getQueue().get(klineQueue.getQueue().size() - 1);

            Calendar hCalendar = Calendar.getInstance();
            hCalendar.setTime(DateUtil.parseDate(String.valueOf(pKLineData.getQuoteDate()), "yyyyMMdd"));
            int hWeekOfYear = hCalendar.get(Calendar.WEEK_OF_YEAR);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.parseDate(String.valueOf(snapshot.getTradeDate()), "yyyyMMdd"));
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

            if (weekOfYear == hWeekOfYear) {
                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
                    if (pKLineData.getPreQuoteDate() == 0) {
                        pKLineData.setPreQuoteDate(pKLineData.getQuoteDate());
                    }
                }
                pKLineData.setQuoteDate(masterData.tradeDate);
//                pKLineData.setOpenPrice(snapshot.getOpenPrice());
                pKLineData.setHighPrice(Math.max(pKLineData.getHighPrice(), snapshot.getHighPrice()));
//                pKLineData.setHighPrice(snapshot.getHighPrice());
                pKLineData.setLowPrice(Math.min(pKLineData.getLowPrice(), snapshot.getLowPrice()));
//                pKLineData.setLowPrice(snapshot.getLowPrice());
                pKLineData.setClosePrice(snapshot.getLastPrice());
                pKLineData.setVolume(snapshot.getVolume());
                pKLineData.setTurnover(snapshot.getTurnover());


//                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
//                    pKLineData.setPeriod(ApplicationConstant.WEEK);
//                    pKLineData.setDataType(ApplicationConstant.DB_DATA);
//                    pKLineData.setDbOperType(ApplicationConstant.DB_UPDATE);
//                    ServerContext.getChartToDBQueue().add(pKLineData.clone());
//                }

            } else {
                // 转周期K线提交任务更新最后一分钟数据到数据库
                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
                    pKLineData.setPeriod(ApplicationConstant.WEEK);
                    pKLineData.setDataType(ApplicationConstant.DB_DATA);
                    pKLineData.setDbOperType(ApplicationConstant.DB_UPDATE);
                    ServerContext.getChartToDBQueue().add(pKLineData.clone());
                }

                KLineData klineData = new KLineData();
                klineData.setSymbol(snapshot.getSymbol());
                klineData.setQuoteDate(masterData.tradeDate);
                klineData.setOpenPrice(snapshot.getOpenPrice());
                klineData.setHighPrice(snapshot.getHighPrice());
                klineData.setLowPrice(snapshot.getLowPrice());
                klineData.setClosePrice(snapshot.getLastPrice());
                klineData.setVolume(snapshot.getVolume());
                klineData.setTurnover(snapshot.getTurnover());
                klineQueue.getQueue().add(klineData);
                // 提交任务同步到数据库
                if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                    klineData.setDataType(ApplicationConstant.DB_DATA);
                    klineData.setPeriod(ApplicationConstant.WEEK);
                    klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                    ServerContext.getChartToDBQueue().add(klineData.clone());
                }
            }
        }
    }

    private void doMonthKLine(String symbol) {
        StockSnapshot snapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(symbol);
        KLineQueue klineQueue = ServerContext.getMonthMap().get(snapshot.getSymbol());
        MasterData masterData = ServerContext.getMasterMap().get(snapshot.getSymbol());
        if (klineQueue == null) {
            klineQueue = new KLineQueue();
            klineQueue.setSymbol(snapshot.getSymbol());
            ServerContext.getMonthMap().put(symbol, klineQueue);
        }
        if (klineQueue.getQueue().isEmpty()) {
            KLineData klineData = new KLineData();
            klineData.setSymbol(snapshot.getSymbol());
            klineData.setQuoteDate(masterData.tradeDate);
            klineData.setOpenPrice(snapshot.getOpenPrice());
            klineData.setHighPrice(snapshot.getHighPrice());
            klineData.setLowPrice(snapshot.getLowPrice());
            klineData.setClosePrice(snapshot.getLastPrice());
            klineData.setVolume(snapshot.getVolume());
            klineData.setTurnover(snapshot.getTurnover());
            klineQueue.getQueue().add(klineData);
            // 提交任务同步到数据库
            if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                klineData.setDataType(ApplicationConstant.DB_DATA);
                klineData.setPeriod(ApplicationConstant.MONTH);
                klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                ServerContext.getChartToDBQueue().add(klineData.clone());
            }
        } else {
            KLineData pKLineData = klineQueue.getQueue().get(klineQueue.getQueue().size() - 1);
            int hYearMonth = pKLineData.getQuoteDate() / 100;
            int yearMonth = snapshot.getQuoteDate() / 100;

            if (hYearMonth == yearMonth) {
                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
                    if (pKLineData.getPreQuoteDate() == 0) {
                        pKLineData.setPreQuoteDate(pKLineData.getQuoteDate());
                    }
                }
                pKLineData.setQuoteDate(masterData.tradeDate);
                pKLineData.setHighPrice(Math.max(pKLineData.getHighPrice(), snapshot.getHighPrice()));
                pKLineData.setLowPrice(Math.min(pKLineData.getLowPrice(), snapshot.getLowPrice()));
                pKLineData.setClosePrice(snapshot.getLastPrice());
                pKLineData.setVolume(snapshot.getVolume());
                pKLineData.setTurnover(snapshot.getTurnover());

            } else {
                // 转周期K线提交任务更新最后一分钟数据到数据库
                if (pKLineData.getDataType() == ApplicationConstant.DB_DATA) {
                    pKLineData.setPeriod(ApplicationConstant.MONTH);
                    pKLineData.setDataType(ApplicationConstant.DB_DATA);
                    pKLineData.setDbOperType(ApplicationConstant.DB_UPDATE);
                    ServerContext.getChartToDBQueue().add(pKLineData.clone());
                }

                KLineData klineData = new KLineData();
                klineData.setSymbol(snapshot.getSymbol());
                klineData.setQuoteDate(masterData.tradeDate);
                klineData.setOpenPrice(snapshot.getOpenPrice());
                klineData.setHighPrice(snapshot.getHighPrice());
                klineData.setLowPrice(snapshot.getLowPrice());
                klineData.setClosePrice(snapshot.getLastPrice());
                klineData.setVolume(snapshot.getVolume());
                klineData.setTurnover(snapshot.getTurnover());
                klineQueue.getQueue().add(klineData);
                // 提交任务同步到数据库
                if (klineData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                    klineData.setDataType(ApplicationConstant.DB_DATA);
                    klineData.setPeriod(ApplicationConstant.MONTH);
                    klineData.setDbOperType(ApplicationConstant.DB_SAVE);
                    ServerContext.getChartToDBQueue().add(klineData.clone());
                }
            }
        }
    }

     private void doSnapshotList(String symbol) {
        StockSnapshot snapshot = (StockSnapshot) ServerContext.getSnapshotMap().get(symbol);
        SnapshotQueue snapshotQueue = ServerContext.getListMap().get(snapshot.getSymbol());
        
        MasterData masterData = ServerContext.getMasterMap().get(snapshot.getSymbol());
        if (snapshotQueue == null) {
            snapshotQueue = new SnapshotQueue();
            snapshotQueue.setSymbol(snapshot.getSymbol());
            ServerContext.getListMap().put(symbol, snapshotQueue);
        }
        if (snapshotQueue.getQueue().isEmpty()) {
            Snapshot SnapshotData = new Snapshot();
            SnapshotData.setSymbol(snapshot.getSymbol());
            SnapshotData.setQuoteDate(masterData.tradeDate);
            SnapshotData.setOpenPrice(snapshot.getOpenPrice());
            SnapshotData.setHighPrice(snapshot.getHighPrice());
            SnapshotData.setLowPrice(snapshot.getLowPrice());
        //    SnapshotData.setClosePrice(snapshot.getLastPrice());
            SnapshotData.setVolume(snapshot.getVolume());
            SnapshotData.setTurnover(snapshot.getTurnover());
            snapshotQueue.getQueue().add(SnapshotData);
            // 提交任务同步到数据库
            if (SnapshotData.getDataType() == ApplicationConstant.MEMORY_DATA) {
                SnapshotData.setDataType(ApplicationConstant.DB_DATA);
                SnapshotData.setPeriod(ApplicationConstant.LIST);
                SnapshotData.setDbOperType(ApplicationConstant.DB_SAVE);
                ServerContext.getChartToDBQueue().add(SnapshotData.clone());
            }
        } else {
           
        }
    }
}
