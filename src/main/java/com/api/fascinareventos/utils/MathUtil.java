package com.api.fascinareventos.utils;

public class MathUtil {

    public static Double RoundDecimal(double value, int decimals) {
        double dc = Math.pow(10, decimals);
        return Math.round(value * dc) / dc;
    }

    public static Integer RoundMulti(double value, int multiple) {
        double sum = value / multiple;
        return Math.toIntExact(Math.round(sum) * multiple);
    }
}
