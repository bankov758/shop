package org.example.helpers;

public class NumberHelper {

    private NumberHelper() {}

    public static double round(double value, int precision) {
        return Math.round(value * Math.pow(10.0, precision)) / Math.pow(10.0, precision);
    }

}
