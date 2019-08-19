package cn.caigd.learn.sort;

import java.util.Random;

public class Sorter {
    public static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        int total=1000000;
        int[] arr = new int[total];
        for (int i = 0; i < total; i++) {
            arr[i] = new Random().nextInt();
        }
        long a = System.currentTimeMillis();
        MergeBU.sort(arr);
        System.out.println(System.currentTimeMillis() - a);
        for (int i = 0; i < total; i++) {
            arr[i] = new Random().nextInt();
        }
        long b = System.currentTimeMillis();
        Quick.sort(arr);
        System.out.println(System.currentTimeMillis() - b);
    }
}
