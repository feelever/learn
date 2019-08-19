package cn.caigd.learn.crypto;

public class AESTest {
    public static void main(String[] args) throws Exception {
        AES aes = new AES_CBC();
        String str = aes.encrypt("hello world");
        System.out.println(str);
        System.out.println(aes.decrypt(str));

    }
}
