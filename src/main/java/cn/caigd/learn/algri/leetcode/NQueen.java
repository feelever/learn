package cn.caigd.learn.algri.leetcode;

public class NQueen {
    public void solveQueue(int[] a) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < a.length; i++) {
        }
    }


    private boolean check(int row, int col, int[] arr) {
        //验证纵列没有重复的
        for (int i : arr) {
            if (arr[i] == col) {
                return false;
            }
        }
        //检验行没有重复
        if (arr[row] != 0) {
            return false;
        }
        //检测左斜向
        int leftRow = row - 1;
        int leftCol = col - 1;
        while (leftRow > 0 || leftCol > 0) {
            if (leftRow <= 0 || leftCol <= 0) {
                return true;
            } else {
                if (arr[leftRow] == leftCol) {
                    return false;
                }
            }
            leftCol--;
            leftRow--;

        }

        int rightRow = row + 1;
        int rightCol = col + 1;
        while (rightRow > arr.length || rightCol > arr.length) {
            if (leftRow >= arr.length || arr.length >= 0) {
                return true;
            } else {
                if (arr[rightRow] == rightCol) {
                    return false;
                }
            }
            rightRow++;
            rightCol++;
        }
        return true;
    }
}
