package cn.caigd.learn.string;

public class KMP {
    // a, b 分别是主串和模式串；n, m 分别是主串和模式串的长度。
    public static int kmp(char[] mainStr, int mainLength, char[] pattern, int patternLength) {
        int[] next = getNexts(pattern, patternLength);
        int matchLength = 0;
        for (int i = 0; i < mainLength; ++i) {
            while (matchLength > 0 && mainStr[i] != pattern[matchLength]) { // 一直找到 a[i] 和 b[j]
                matchLength = next[matchLength - 1] + 1;
            }
            if (mainStr[i] == pattern[matchLength]) {
                ++matchLength;
            }
            if (matchLength == patternLength) { // 找到匹配模式串的了
                return i - patternLength + 1;
            }
        }
        return -1;
    }

    // b 表示模式串，m 表示模式串的长度,倒序获取最近的同款字符
    private static int[] getNexts(char[] strArr, int length) {
        int[] next = new int[length];
        next[0] = -1;
        int matchIndex = -1;
        for (int index = 1; index < length; ++index) {
            while (matchIndex != -1 && strArr[matchIndex + 1] != strArr[index]) {
                matchIndex = next[matchIndex];
            }
            if (strArr[matchIndex + 1] == strArr[index]) {
                ++matchIndex;
            }
            next[index] = matchIndex;
        }
        return next;
    }


    public static void main(String[] args) {
        char[] chars = new char[6];
        chars[0] = '3';
        chars[1] = '1';
        chars[2] = '2';
        chars[3] = '3';
        chars[4] = '4';
        chars[5] = '3';

        char[] pattern = new char[3];
        pattern[0] = '3';
        pattern[1] = '4';
        pattern[2] = '3';
        System.out.println(KMP.kmp(chars, 6, pattern, 3));

    }
}
