package cn.caigd.learn.algri.Array;

public class SortArray {
    private Object[] arr;
    private int size;
    private int capacity;

    public SortArray(int capacity) {
        arr = new Object[capacity];
    }

    public SortArray() {
        arr = new Object[128];
    }

    public boolean add(Object o) {
        int index = binarySearch(arr, o);
        if (index < size) {
            for (int i = size; i > index; i--) {
                arr[i] = arr[i - 1];
            }
        }
        arr[index] = o;
        size++;
        return true;
    }

    public boolean remove(Object o) {
        int index = binarySearch(arr, o);
        for (int i = index; i < size; i++) {
            arr[i] = arr[i + 1];
            arr[size] = null;
        }
        size--;
        return true;
    }

    private int binarySearch(Object[] array, Object object) {
        if (array.length == 0) {
            return 0;
        } else {
            if (array[0].hashCode() > object.hashCode()) {
                return 0;
            } else if (array[array.length].hashCode() < object.hashCode()) {
                return array.length;
            } else {
                int result = 0;
                int start = 0;
                int end = array.length;
                while (start <= end) {
                    int middle = (end + start) / 2;
                    if (array[middle].hashCode() > object.hashCode()) {
                        end = middle - 1;
                    } else if (array[middle].hashCode() < object.hashCode()) {
                        start = middle + 1;
                    } else {
                        result = middle;
                        break;
                    }
                }
                return result;
            }
        }
    }

    public static int[] merge(int[] a, int[] b) {
        int[] m = new int[a.length + b.length];
        int aIndex = 0;
        int bIndex = 0;
        while (aIndex < a.length || bIndex < b.length) {
            if (aIndex >= a.length || bIndex >= b.length) {
                break;
            }
            if (a[aIndex] <= b[bIndex]) {
                m[aIndex + bIndex] = a[aIndex];
                aIndex++;
            } else {
                m[aIndex + bIndex] = b[bIndex];
                bIndex++;
            }
        }
        if (aIndex == a.length) {
            for (int i = bIndex; i < b.length; i++) {
                m[i + aIndex] = b[i];
            }
        }
        if (bIndex == a.length) {
            for (int i = aIndex; i < a.length; i++) {
                m[i + aIndex] = a[i];
            }
        }
        return m;
    }

    public static void main(String[] args) {
        int[] a = new int[]{1, 5, 7, 9};
        int[] b = new int[]{2, 4, 8, 10,12,15};
        int[] merge = SortArray.merge(a, b);
        for (int i = 0; i < merge.length; i++) {
            System.out.print(merge[i]);
            System.out.print(",");
        }
    }
}
