package cn.caigd.learn.sort;

public class MergeBU extends Merge{
    /**
     * 自底向上归并排序
     * 外层for循环控制子序列长度
     * 内层for循环控制子序列合并
     * 归并排序算法复杂度O(N²)
     *
     * @param integers
     * @return
     */
    public static void sort(int[] integers) {
        for (int i = 1; i < integers.length; i = i + i) {
            for (int j = 0; j < integers.length - i; j = j + 2 * i) {
                Merge.merge(integers, j, j + i - 1, Math.min(j + 2 * i - 1, integers.length - 1));
            }
        }
    }

    public static int[] split(int[] integers) {
        for (int i = 1; i < integers.length; i = i + i) {
            for (int j = 0; j < i; j++) {
                System.out.print(integers[j]);
            }
            for (int j = 0; j < integers.length - i; j = j + 2 * i) {

            }
            System.out.println();
        }
        return null;
    }

    public static void main(String[] args) {
        int[] integers = new int[]{2, 2, 1, 5, 4, 9, 6, 8, 7};
        sort(integers);
        System.out.println(integers);
    }
}