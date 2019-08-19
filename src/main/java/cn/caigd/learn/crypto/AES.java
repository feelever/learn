package cn.caigd.learn.crypto;

public interface AES {
    String encrypt(String sSrc) throws Exception;

    String decrypt(String sSrc) throws Exception;
}
