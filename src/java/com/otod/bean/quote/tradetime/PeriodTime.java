/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.bean.quote.tradetime;

import com.otod.util.TradeTimeUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PeriodTime {

    public List<TimeNode> tradeTimes;
    public List<Integer> timeList;
    public List<Integer> periodTimeList = new ArrayList<Integer>();
    public int period;

    public static ArrayList<Integer> createTimes(List<TimeNode> tradeTimes) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int num = 0, t;
        TimeNode timeNode;
        for (int i = 0; i < tradeTimes.size(); i++) {
            num = 0;
            timeNode = tradeTimes.get(i);

            for (int j = timeNode.startTime; j < timeNode.endTime; j++) {
                if (j % 100 < 60) {
                    num++;
                }
            }
            t = timeNode.startTime;
            for (int j = 0; j <= num; j++) {
                if (t == 2400) {
                    t++;
                    continue;
                }
                list.add(t);
                t++;
                if (t % 100 >= 60) {
                    t += 40;
                }
            }
        }
        return list;
    }

    public void init() {

        timeList = createTimes(tradeTimes);

//        for (int i = 0; i < timeList.size(); i++) {
//            System.out.print(timeList.get(i) + ",");
//        }

        if (period == 1) {
            for (int i = 0; i < timeList.size(); i += period) {
                periodTimeList.add(timeList.get(i));
            }
        } else {
            int count = 0;
            for (int i = 0; i < timeList.size(); i++) {
                boolean flag = false;
                for (int j = 0; j < tradeTimes.size(); j++) {
                    if (timeList.get(i) == tradeTimes.get(j).startTime && tradeTimes.get(j).startTime != 0) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    count++;
                }
                if (count == period) {
                    count = 0;
                    periodTimeList.add(timeList.get(i));
                }
            }
//            for (int i = 0; i < timeList.size(); i += period) {
//                if (i == 0) {
//                    continue;
//                }
//                periodTimeList.add(timeList.get(i));
//                for (int j = 0; j < tradeTimes.length; j++) {
//                    if (j != (tradeTimes.length - 1) && timeList.get(i) == tradeTimes[j][1]) {
//                        i++;
//                    }
//                }
//            }
        }
//        System.out.println("");
//
//        for (int i = 0; i < periodTimeList.size(); i++) {
//            System.out.print(periodTimeList.get(i) + ",");
//        }
//        System.out.println("");
    }

    public int findTime1(int t) {
        int idx = 0;

        for (int i = 0; i < periodTimeList.size(); i++) {
            if (i != 0) {
                if (t < periodTimeList.get(i) && t >= periodTimeList.get(i - 1)) {
                    return i;
                }
            }
        }

        return idx;
    }

    public int findTime(int t) {

        int idx = 0;
        boolean flag = false;
        for (int i = 0; i < tradeTimes.size(); i++) {
            if (t >= tradeTimes.get(i).startTime && t <= tradeTimes.get(i).endTime) {
                flag = true;
            }
        }

        if (!flag) {
            return -1;
        }
        if (t >= tradeTimes.get(0).startTime) {
            int zct = t - tradeTimes.get(0).startTime;

            int zjct = 0;
            int zjch = 0;
            for (int i = 0; i < tradeTimes.size(); i++) {
                if ((i - 1) >= 0 && t >= tradeTimes.get(i).startTime && tradeTimes.get(i).startTime > tradeTimes.get(0).startTime) {
                    zjct += tradeTimes.get(i).startTime - tradeTimes.get(i - 1).endTime;
                    zjch += tradeTimes.get(i).startTime / 100 - (tradeTimes.get(i - 1).endTime) / 100;
                    if (period == 1) {
                        zjct -= 1;
                    }
                }
            }
            int ch = t / 100 - tradeTimes.get(0).startTime / 100 - zjch;
            int cm = zct - zjct - ch * 40;
            idx = cm / period;

            if (idx >= periodTimeList.size()) {
                idx = periodTimeList.size() - 1;
            }

        } else if (t < tradeTimes.get(0).startTime) {
            int zct = (t - 0) + (2400 - tradeTimes.get(0).startTime);

            int zjct = 0;
            int zjch = 0;
            for (int i = 0; i < tradeTimes.size(); i++) {
                if ((i - 1) >= 0 && tradeTimes.get(i).startTime > tradeTimes.get(0).startTime) {
                    zjct += tradeTimes.get(i).startTime - tradeTimes.get(i - 1).endTime;
                    zjch += tradeTimes.get(i).startTime / 100 - (tradeTimes.get(i - 1).endTime) / 100;
                    if (period == 1) {
                        zjct -= 1;
                    }
                } else if ((i - 1) >= 0 && t >= tradeTimes.get(i).startTime && tradeTimes.get(i).startTime < tradeTimes.get(0).startTime && tradeTimes.get(i).startTime < tradeTimes.get(i - 1).endTime) {
                    if (tradeTimes.get(i - 1).endTime == 2400) {
                        zjct += (tradeTimes.get(i).startTime - 0) + (2400 - tradeTimes.get(i - 1).endTime);
                        zjch += (tradeTimes.get(i).startTime - 0) / 100 + (2400 - (tradeTimes.get(i - 1).endTime)) / 100;
                    } else {
                        zjct += (tradeTimes.get(i).startTime - 0) + (2400 - tradeTimes.get(i - 1).endTime);
                        zjch += (tradeTimes.get(i).startTime - 0) / 100 + (2400 - (tradeTimes.get(i - 1).endTime)) / 100;
                        if (period == 1) {
                            zjct -= 1;
                        }
                    }

                } else if ((i - 1) >= 0 && t >= tradeTimes.get(i).startTime && tradeTimes.get(i).startTime < tradeTimes.get(0).startTime && tradeTimes.get(i).startTime > tradeTimes.get(i - 1).endTime) {
                    zjct += tradeTimes.get(i).startTime - tradeTimes.get(i - 1).endTime;
                    zjch += tradeTimes.get(i).startTime / 100 - tradeTimes.get(i - 1).endTime / 100;
                    if (period == 1) {
                        zjct -= 1;
                    }
                }
            }
            int ch = (t - 0) / 100 + 2400 / 100 - tradeTimes.get(0).startTime / 100 - zjch;
            int cm = zct - zjct - ch * 40;
            idx = cm / period;

            if (idx >= periodTimeList.size()) {
                idx = periodTimeList.size() - 1;
            }
        }
        return idx;
    }

    public static void main(String[] args) {
        List<TimeNode> tradeTimes = new ArrayList<TimeNode>();
        TimeNode timeNode = new TimeNode();
        timeNode.startTime = 2100;
        timeNode.endTime = 2400;
        tradeTimes.add(timeNode);

        timeNode = new TimeNode();
        timeNode.startTime = 0;
        timeNode.endTime = 230;
        tradeTimes.add(timeNode);

        timeNode = new TimeNode();
        timeNode.startTime = 900;
        timeNode.endTime = 1300;
        tradeTimes.add(timeNode);


        timeNode = new TimeNode();
        timeNode.startTime = 1330;
        timeNode.endTime = 1530;
        tradeTimes.add(timeNode);

        PeriodTime periodTime = new PeriodTime();
        periodTime.tradeTimes = tradeTimes;
        periodTime.period = 1;
        periodTime.init();

        int idx = periodTime.findTime(1138);
        if (idx >= 0) {
            System.out.println(periodTime.findTime(1138) + "||" + periodTime.periodTimeList.get(idx));
        } else {
            System.out.println("没有找到该时间");
        }
    }
}
