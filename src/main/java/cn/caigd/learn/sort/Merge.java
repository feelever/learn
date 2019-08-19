package cn.caigd.learn.sort;

public class Merge {
    private static int[] aux;
    public static void sort(int[] a) {
        aux = new int[a.length];
        sort(a, 0, a.length - 1);
    }

    private static void sort(int[] a, int lo, int high) {
        if (high <= lo) {
            return;
        }
        int mid = lo + (high - lo) / 2;
        sort(a, lo, mid);
        sort(a, mid + 1, high);
        merge(a, lo, mid, high);
    }

    public static void merge(int[] integers, int min, int mid, int max) {
        int i = min, j = mid + 1;
        for (int k = min; k < max; k++){
            aux[k] = integers[k];
        }
        for (int k = min; k <= max; k++) {
            if (i > mid) {
                integers[k] = aux[j++];
            } else if (j > max) {
                integers[k] = aux[i++];
            } else if (aux[i] < aux[j]) {
                integers[k] = aux[i++];
            } else {
                integers[k] = aux[j++];
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{3, 4, 5, 7, 2, 6};
        Merge.sort(a);
    }
}
