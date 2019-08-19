package cn.caigd.learn.sort;

public class Bubble {
    public static void sort(int[] a) {
        for (int i = 1; i < a.length - 1; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j] < a[j - 1])
                    Sorter.swap(a, j, j - 1);
                else
                    break;
            }
        }
        return;
    }
    public static void sort(int[] a,int lo ,int hi) {
        for (int i = lo; i < hi; i++) {
            for (int j = i; j > 0; j--) {
                if (a[j] < a[j - 1])
                    Sorter.swap(a, j, j - 1);
                else
                    break;
            }
        }
        return;
    }

    public static void main(String[] args) {
        int[] a = new int[]{2, 3, 5, 1, 4, 6};
        Insertion.sort(a);
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }
}
