package cn.caigd.learn.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SelectorDemo {
    public static void selector() throws Exception {
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("https://www.baidu.com",443));
        channel.configureBlocking(false);
        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(new String("hello world"));
        while (true) {
            int readChannels = selector.select();
            if (readChannels == 0) continue;
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    System.out.println(key.attachment());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception{
        selector();
    }
}
