package cn.caigd.learn.dp;

import java.util.Arrays;

public class CountCoins {
    //状态转移方程为当前amount的旧值，也就是少了新增的coin所提供的值+有了该coin之后amount扣减掉该coin的那部分的数量，因为需要累加然后继续迭代；
    public static int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;//dp[i]代表amount=i的时候组合数量
        for (int coin : coins) {
            for (int i = 1; i <= amount; i++) {
                if (i >= coin) {
                    //随着硬币的数量增加，需要更新当前的组合数量值，更新方法为
                    //旧的值+当前amount扣除了coin之后的组合数，比如i=3;coin=3;则统计为dp[3]+dp[0]；当i=4;dp[4]=dp[4]+dp[1];dp[6]=dp[6]+dp[3];这个dp[3]是刷新之后的值；也就是i必须为从小到大统计；
                    //二维数组统计会更清晰，少去迭代覆盖，但是空间复杂度上升；
                    dp[i] = dp[i] + dp[i - coin];
                }
            }
        }
        return dp[amount];
    }

    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num : nums)
            sum += num;
        if (sum % 2 == 1)
            return false;
        int mid = sum / 2;
        boolean[] dp = new boolean[mid + 1];
        dp[0] = true;
        for (int num : nums) {
            for (int i = mid; i >= 0; i--) {
                if (i >= num) {
                    dp[i] = dp[i] || dp[i - num];
                }
            }
        }
        return dp[mid];
    }

    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int i = 1; i <= coins.length; i++) {
            for (int j = 1; j <= amount; j++) {
                if (j >= coins[i - 1])
                    //状态转移方程为当前节点的统计与已有的少一个硬币的时候的数量+1进行比对
                    dp[j] = Math.min(dp[j], dp[j - coins[i - 1]] + 1);
            }
        }
        return dp[amount] == amount + 1 ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        CountCoins countCoins = new CountCoins();
        System.out.println(countCoins.coinChange(new int[]{1, 2, 5}, 11));
        System.out.println(countCoins.canPartition(new int[]{1,6,5,3,5}));
    }
}
