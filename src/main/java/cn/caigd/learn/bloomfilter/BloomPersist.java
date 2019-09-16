package cn.caigd.learn.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.*;

public class BloomPersist {
    public static void input() throws Exception {
        BloomFilter<Integer> filter = BloomFilter.create(
                Funnels.integerFunnel(),
                500,
                0.01);

        //导入数据到filter
        for (int i = 0; i < 100; i++) {
            filter.put(i);
        }

        //数据持久化到本地
        File f = new File("d:" + File.separator + "test2");
        OutputStream out = null;
        out = new FileOutputStream(f);

        try {
            filter.writeTo(out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //测试验证
        for (int i = 0; i < 10; i++) {
            boolean result = filter.mightContain(i);

            if (result) {
                System.out.println("i = " + i + " " + result);
            }
        }
    }

    public static void output() throws Exception {
        BloomFilter<Integer> filter = BloomFilter.create(
                Funnels.integerFunnel(),
                500,
                0.01);

        //将之前持久化的数据加载到Filter
        File f = new File("d:" + File.separator + "test2");
        InputStream in = null;
        in = new FileInputStream(f);
        try {
            filter = BloomFilter.readFrom(in, Funnels.integerFunnel());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //测试验证
        for (int i = 0; i < 10; i++) {
            boolean result = filter.mightContain(i);

            if (result) {
                System.out.println("i = " + i + " " + result);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        BloomPersist.input();
        BloomPersist.output();

    }
}