package cn.caigd.learn.interview;

public class FtpServer {
    public static int FindMaxOfArray(int[] arr){
        int max = -1;
        int i = 0, j = arr.length - 1, mid =(arr.length-1)/2;
        while(j - i > 1){
            //舍弃前半部分
            if(arr[mid] > arr[i] && arr[mid] > arr[j]){
                i = mid;
            }
            //舍弃后半部分
            else if(arr[mid] < arr[i] && arr[mid] < arr[j]){
                j = mid;
            }
            if(mid == (i+j)/2)
                break;
            mid = (i+j)/2;
        }
        if(arr[i] > arr[j])
            max = arr[i];
        else
            max = arr[j];
        return max;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            System.out.println(FtpServer.FindMaxOfArray(new int[]{1,5,6,8,7,6,4,5,6,9,8,4,2,5,7,3,1}));
        }

    }



}
