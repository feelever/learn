package cn.caigd.learn.algri.Array;

public class DynamicArray {
    private Object[] o;
    private int capacity;
    private float factor;
    private int size;

    public DynamicArray(int capacity, float factor) {
        o = new Object[capacity];
        this.factor = factor;
    }

    public DynamicArray(int capacity) {
        o = new Object[capacity];
        factor = 0.75f;
    }

    public DynamicArray() {
        o = new Object[8];
    }

    public void add(Object obj) {
        if (o.length >= capacity * 0.75) {
            if (capacity > Integer.MAX_VALUE >> 1) {
                capacity = Integer.MAX_VALUE - 1;
            } else {
                capacity = capacity << 1;
            }
            Object[] newO = new Object[capacity];
            for (int i = 0; i < o.length; i++) {
                newO[i] = o[i];
            }
            o = newO;
        }
        o[o.length - 1] = obj;
    }

    public Object get(int index) {
        return o[index];
    }

    public boolean remove(int index) {
        o[index] = null;
        return true;
    }

    public static void main(String[] args) {
        int a = 1;
        System.out.println(a << 1);
    }
}
