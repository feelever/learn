package cn.caigd.learn.dp.easy;

public class LargestSubArray {
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];//dp[i] means the maximum subarray ending with A[i];
        dp[0] = nums[0];
        int max = dp[0];
        for (int i = 1; i < n; i++) {
            dp[i] = nums[i] + (dp[i - 1] > 0 ? dp[i - 1] : 0);//当前估分为前一个值的非负值加上当前值，为负代表故考虑前面的了，不为负则累加
            max = Math.max(max, dp[i]);
            System.out.println(dp[i-1]+"=="+dp[i]+"=="+max);
        }

        return max;
    }

    public static void main(String[] args) {
        LargestSubArray largestSubArray = new LargestSubArray();
        int[] a = new int[]{-2, -1, -3, -4, -1,-5,-2, -1, -2, -4};
        largestSubArray.maxSubArray(a);
    }
}
