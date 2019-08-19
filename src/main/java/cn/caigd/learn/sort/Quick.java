package cn.caigd.learn.sort;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;
import java.util.Stack;

public class Quick {
    public static void sort(int[] a) {
        sortWithLoop(a, 0, a.length - 1);
    }

    /**
     * 普通快排
     *
     * @param array
     * @param lo
     * @param hi
     * @return
     */
    public static int partition(int[] array, int lo, int hi) {
        ArrayUtils.swap(array, lo, new Random().nextInt(array.length - 1));//处理随机因子;
        int pivot = array[lo];
        int j = lo;
        for (int i = j + 1; i <= hi; i++) {
            if (array[i] < pivot) {
                j++;
                ArrayUtils.swap(array, i, j);
            }
        }
        ArrayUtils.swap(array, lo, j);
        return j;
    }

    /**
     * 双路快排方式
     *
     * @param array
     * @param lo
     * @param hi
     * @return
     */
    public static int partition2(int[] array, int lo, int hi) {
        ArrayUtils.swap(array, lo, (int) (Math.random() * (hi - lo + 1)) + lo);
        int v = array[lo];
        int i = lo + 1;
        int j = hi;
        while (true) {
            while (i <= hi && array[i] < v) {
                i++;
            }
            while (j >= lo + 1 && array[j] > v) {
                j--;
            }
            if (i > j) {
                break;
            }
            ArrayUtils.swap(array, i, j);
            i++;
            j--;
        }
        ArrayUtils.swap(array, lo, j);
        return j;
    }

    /**
     * 三路快排
     *
     * @param arr
     * @param l
     * @param r
     */
    public static void sort3Way(int[] arr, int l, int r) {
        if (l >= r) return;
        int pivot = arr[l];
        int lt = l;     // arr[l+1...lt] < pivot
        int gt = r + 1; // arr[gt...r] > pivot
        int i = l + 1;    // arr[lt+1...i) == pivot
        while (i < gt) {
            if (arr[i] < pivot) {
                ArrayUtils.swap(arr, i, lt + 1);
                i++;
                lt++;
            } else if (arr[i] > pivot) {
                ArrayUtils.swap(arr, i, gt - 1);
                gt--;
            } else { // arr[i] == pivot
                i++;
            }
        }
        Sorter.swap(arr, l, lt);
        sort3Way(arr, l, lt - 1);
        sort3Way(arr, gt, r);
    }

    /**
     * 非递归快排
     *
     * @param a
     * @param lo
     * @param hi
     */
    public static void sortWithLoop(int[] a, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        Stack<Integer> stack = new Stack<>();
        stack.push(lo);
        stack.push(hi);
        while (!stack.isEmpty()) {
            int right = stack.pop();
            int left = stack.pop();
            if (left < right) {
                int pivot = partition2(a, left, right);
                stack.push(left);
                stack.push(pivot);
                stack.push(pivot + 1);
                stack.push(right);
            }
        }
    }

    public static void sort(int[] array, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int index = partition2(array, lo, hi);
        sort(array, lo, index - 1);
        sort(array, index + 1, hi);
    }

    public static void main(String[] args) {
        int[] a = new int[]{5, 2, 1, 4, 3, 7, 6};
        //ArrayUtils.isSorted(a);
        Quick.sort(a, 0, 6);
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]);
        }
    }
}
