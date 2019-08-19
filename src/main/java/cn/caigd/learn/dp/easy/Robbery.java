package cn.caigd.learn.dp.easy;

public class Robbery {
    public int rob(int[] nums) {
        int sum = 0;
        int i = 0;
        while (i <= nums.length) {
            if (i >= nums.length - 1) {
                return sum;
            }
            if (nums[i] > nums[i + 1]) {
                sum += nums[i];
                i += 2;
            } else {
                sum += nums[i + 1];
                i += 3;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int[] a = new int[]{1,2,3,1};
        System.out.println(new Robbery().rob(a));
    }
}
