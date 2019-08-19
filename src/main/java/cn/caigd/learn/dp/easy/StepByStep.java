package cn.caigd.learn.dp.easy;

public class StepByStep {
    public int climbStairs(int n) {
        int[] dp = new int[n+1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    public static void main(String[] args) {
        StepByStep stepByStep = new StepByStep();
        System.out.println(stepByStep.climbStairs(11));
    }
}
