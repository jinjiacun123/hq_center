/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TradeTimeUtil {

    public static ArrayList<Integer> createTimes(int[][] tradeTimes) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        int num = 0, t;
        for (int i = 0; i < tradeTimes.length; i++) {
            num = 0;
            for (int j = tradeTimes[i][0]; j < tradeTimes[i][1]; j++) {
                if (j % 100 < 60) {
                    num++;
                }
            }
            if(i==0)
            t = tradeTimes[i][0];
            else
            t=tradeTimes[i][0]+1;
            
            for (int j = 0; j <= num; j++) {
                list.add(t);
                t++;
                if (t % 100 >= 60) {
                    t += 40;
                }
            }
        }
        return list;
    }
}
