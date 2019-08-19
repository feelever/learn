package cn.caigd.learn.btrace;

import com.google.common.collect.Maps;

public class NumberUtil {
 
    public int sum(){
        int result = 0;
        for(int i = 0; i< 100; i++){
            result += i * i;
        }
        return result;
    }
 
    public static void main(String[] args){
        String str ="1";
        String str2 ="1";
        Maps.newHashMap();
        System.out.println(str.hashCode());
        System.out.println(str2.hashCode());

    }
}