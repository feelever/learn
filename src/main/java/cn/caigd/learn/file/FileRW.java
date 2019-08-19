package cn.caigd.learn.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileRW {
    private static String word2048;

    static {
        for (int i = 0; i < 128; i++) {
            word2048 += "12345678";
        }
    }

    public static void writeBuffer(File file) throws IOException {
        long a = System.currentTimeMillis();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        int i = 1024 * 1024;
        while (i > 0) {
            //  word2048为字符串常量，刚好4800个字节
            writer.write(word2048);
            i--;
        }
        writer.close();
        fos.close();
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void writeBuffer2(File file) throws Exception {
        long a = System.currentTimeMillis();
        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fc = fos.getChannel();
        int times = 128;
        byte[] datas = word2048.getBytes();
        ByteBuffer bbuf = ByteBuffer.allocate(4800 * times);
        int i = 1024 * 8 * 4;
        while (i > 0) {
            for (int j = 0; j < times; j++) {
                bbuf.put(datas);
            }
            bbuf.flip();
            fc.write(bbuf);
            bbuf.clear();
            i--;
        }
        System.out.println(System.currentTimeMillis() - a);

    }

    public static void writeBuffer3(File file) throws Exception {
        long a = System.currentTimeMillis();
        //  必须采用RandomAccessFile，并且是rw模式
        RandomAccessFile acf = new RandomAccessFile(file, "rw");
        FileChannel fc = acf.getChannel();
        byte[] bs = word2048.getBytes();
        int len = bs.length * 1000;
        long offset = 0;
        int i = 2000000;
        while (i > 0) {
            MappedByteBuffer mbuf = fc.map(FileChannel.MapMode.READ_WRITE, offset, len);
            for (int j = 0; j < 1000; j++) {
                mbuf.put(bs);
            }
            offset = offset + len;
            i = i - 1000;
        }
        fc.close();
        System.out.println(System.currentTimeMillis()-a);
    }


    public static void main(String[] args) throws Exception {
        File file = new File("D:\\log.txt");
        writeBuffer3(file);
    }
}
