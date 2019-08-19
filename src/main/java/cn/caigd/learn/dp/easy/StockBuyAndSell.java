package cn.caigd.learn.dp.easy;

public class StockBuyAndSell {
    public int maxProfit(int[] prices) {
        int maxCur = 0, maxSoFar = 0;
        for (int i = 1; i < prices.length; i++) {
            maxCur = Math.max(0, maxCur += prices[i] - prices[i - 1]);
            maxSoFar = Math.max(maxCur, maxSoFar);
        }
        return maxSoFar;
    }

    public static void main(String[] args) {
        StockBuyAndSell stockBuyAndSell = new StockBuyAndSell();
        int i = stockBuyAndSell.maxProfit(new int[]{1, 3, 4, 2, 7, 1, 5, 2, 8});
        System.out.println(i);
    }
}
