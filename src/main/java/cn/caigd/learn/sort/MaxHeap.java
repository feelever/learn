package cn.caigd.learn.sort;

import java.util.Arrays;

public class MaxHeap {
    protected int[] a;
    protected int size;

    public MaxHeap() {
    }

    public MaxHeap(int[] a) {
        build(a);
    }

    public int parent(int i) {
        return (i - 1) / 2;
    }

    public int left(int i) {
        return 2 * i + 1;
    }

    public int right(int i) {
        return 2 * i + 2;
    }

    public void maxHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int largest = i;
        if (l < size - 1 && a[l] > a[i]) {
            largest = l;
        }
        if (r < size - 1 && a[r] > a[largest]) {
            largest = r;
        }
        if (largest != i) {
            int temp = a[i];
            a[i] = a[largest];
            a[largest] = temp;
            this.maxHeapify(largest);
        }
    }

    public void build(int[] a) {
        this.a = a;
        this.size = a.length;
        for (int i = parent(size - 1); i >= 0; i--) {
            maxHeapify(i);
        }
    }

    public void heapsort(int[] a) {
        build(a);
        int step = 1;
        for (int i = a.length - 1; i > 0; i--) {
            int temp = a[i];
            a[i] = a[0];
            a[0] = temp;
            size--;
            System.out.println("step:" + (step++) + Arrays.toString(a));
            maxHeapify(0);
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{5,3,2,4,1,6,8,7};
        MaxHeap maxHeap = new MaxHeap();
        maxHeap.heapsort(a);
        for (int i : a) {
            System.out.print(i+" ");
        }
    }
}
