/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.util;

/**
 *
 * @author Administrator
 */
public class IndicatorUtil {

    public static double[] calLimits(int start, int end, double[]... args) {
        double[] limits = new double[2];
        limits[0] = Double.MAX_VALUE;
        limits[1] = Double.MIN_VALUE;

        //System.out.println(args.length);
        int off;
        for (int i = 0; i < (end - start); i++) {
            off = start + i;
            for (int j = 0; j < args.length; j++) {
                if (args[j][off] != 0) {
                    limits[0] = Math.min(limits[0], args[j][off]);
                    limits[1] = Math.max(limits[1], args[j][off]);

                }

            }
        }
        return limits;
    }

    public static double[] calPriceLimits(int start, int end, double[]... args) {
        double[] limits = new double[2];
        limits[0] = Double.MAX_VALUE;
        limits[1] = Double.MIN_VALUE;

        //System.out.println(args.length);
        int off;
        for (int i = 0; i < (end - start); i++) {
            off = start + i;
            for (int j = 0; j < args.length; j++) {
                if (args[j][off] != 0) {
                    limits[0] = Math.min(limits[0], args[j][off]);
                    limits[1] = Math.max(limits[1], args[j][off]);
                }
            }
        }
        return limits;
    }

    public static double[] REF(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        for (int i = 0; i < datas.length; i++) {
            if (i >= p) {
                outdatas[i] = datas[i - p];
            } else {
                outdatas[i] = datas[i];
            }
        }
        return outdatas;
    }

    public static double[] MA(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        for (int i = 0; i < datas.length; i++) {
            {
                if (i > p) {
                    double sum = 0;
                    for (int j = 0; j < p; j++) {
                        sum += datas[i - j];
                    }
                    outdatas[i] = sum / p;
                }
            }
        }
        return outdatas;
    }

    public static double[] SMA(double[] datas, int p1, int p2) {
        double[] outdatas = new double[datas.length];
        if (datas.length > 0) {
            outdatas[0] = datas[0];
            for (int i = 1; i < datas.length; i++) {
                outdatas[i] = (p2 * datas[i] + (p1 - p2) * outdatas[i - 1]) / p1;

            }
        }
        return outdatas;
    }

    public static double[] SUM(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        double sum = 0;
        for (int i = 0; i < datas.length; i++) {
            sum = 0;
            if (i > p) {
                for (int j = i; j > (i - p); j--) {
                    sum += datas[j];
                }
                outdatas[i] = sum / p;
            }

        }
        return outdatas;
    }

    public static double[] EMA(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        if (datas.length > 0) {
            double k1;
            double k2;
            k1 = (double) 2 / (p + 1);
            k2 = 1 - k1;
            // k2 = (double) (p - 1) / (p + 1);
            outdatas[0] = datas[0];
            for (int i = 1; i < datas.length; i++) {
                outdatas[i] = k1 * datas[i] + k2 * outdatas[i - 1];
            }
        }
        return outdatas;
    }

    public static double[] LLV(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        int i = 0;
        int size = datas.length;
        double min;
        for (i = 0; i < size; i++) {
            if (i > p) {
                min = Double.MAX_VALUE;
                for (int j = 0; j < p; j++) {
                    min = Math.min(min, datas[i - j]);
                }
                outdatas[i] = min;
            }

        }
        return outdatas;
    }

    public static double[] HHV(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        int i = 0;
        int size = datas.length;

        double max;
        for (i = 0; i < size; i++) {
            if (i > p) {
                max = Double.MIN_VALUE;
                for (int j = 0; j < p; j++) {
                    max = Math.max(max, datas[i - j]);
                }
                outdatas[i] = max;
            }

        }
        return outdatas;
    }

    public static double[] AVEDEV(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        int i = 0;
        int size = datas.length;
        for (i = 0; i < size; i++) {
            if (i > p) {
                double avg = 0;
                for (int j = 0; j < p; j++) {
                    avg += datas[i - (p - j)];
                }
                avg /= p;
                double total = 0;
                for (int j = 0; j < p; j++) {
                    total += Math.abs(datas[i - (p - j)] - avg);
                }
                outdatas[i] = total / p;
            }
        }
        return outdatas;
    }

    public static double[] STD(double[] datas, int p) {
        double[] outdatas = new double[datas.length];
        int i = 0;
        int size = datas.length;
        int count = 0;
        double sum = 0;
        double[] datas_ma = IndicatorUtil.MA(datas, p);

        for (i = 0; i < size; i++) {
            if (i > p) {
                sum = 0;
                count = 0;
                for (int j = 0; j < p; j++) {
                    count++;
                    //System.out.println(datas[i - j]+"||"+datas_ma[i]);
                    sum += Math.pow((datas[i - j] - datas_ma[i]), 2);
                }
                outdatas[i] = Math.sqrt(sum / (count - 1));
            }
        }
        return outdatas;
    }
}
