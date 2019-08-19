package cn.caigd.learn.sort;

public class Insertion {
    public static void sort(int[] a) {
        sort(a, 0, a.length - 1);
    }

    public static void sort(int[] a, int lo, int hi) {
        int len = hi - lo + 1;
        for (int i = 0; i < len; i++) {
            int temp = a[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (a[j] > temp) {
                    a[j + 1] = a[j];
                } else {
                    break;
                }
            }
            a[j + 1] = temp;
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{2, 3, 5, 1, 4, 6};
        Insertion.sort(a);
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }
}
