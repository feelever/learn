package cn.caigd.learn.lstm;

import com.google.common.collect.Maps;

import java.util.Map;

public class LstmCalculator {
    public static double sigmoid(double value) {
        return 1 / 1 + Math.pow(Math.E, (-1 * value));
    }

    public double derivate(double value) {
        return value * (1 - value);
    }

    public static void main(String[] args) {
        int binaryDim = 8;
        Map<Integer, Byte> int2Binary = Maps.newHashMap();
        Double LargestNumber = Math.pow(2, binaryDim);


    }
}
