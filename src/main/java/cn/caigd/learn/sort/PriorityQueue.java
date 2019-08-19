package cn.caigd.learn.sort;

import java.util.Arrays;

public class PriorityQueue extends MaxHeap {
    public PriorityQueue() {
        super();
    }

    public PriorityQueue(int[] a) {
        super(a);
    }

    public int max() {
        return a[0];
    }

    public int extractMax() {
        if (size < 1) {
            System.out.println("error max heap");
        }
        int max = a[0];
        a[0] = a[size - 1];
        size--;
        super.maxHeapify(0);
        return max;
    }

    public void increaseKey(int i, int key) {
        if (key < a[i]) {
            System.out.println("new key must be greater than older one");
        }
        a[i] = key;
        while (i > 0 && a[parent(i)] < a[i]) {
            int temp = a[i];
            a[i] = a[parent(i)];
            a[parent(i)] = temp;
            i = parent(i);
        }
    }

    private void insert(int key) {
        size++;
        a[size - 1] = Integer.MAX_VALUE;
        increaseKey(size - 1, key);
    }

    public static void main(String[] args) {
        int[] a = new int[]{5, 4, 3, 1, 9, 6, 8, 7};
        System.out.println(Arrays.toString(a));
        PriorityQueue pq = new PriorityQueue();
        pq.build(a);
        pq.increaseKey(2, 100);
        pq.insert(33);
        System.out.println(Arrays.toString(a));
    }

}
