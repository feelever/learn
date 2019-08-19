package cn.caigd.learn.nio.nioserver.http;

import cn.caigd.learn.nio.nioserver.IMessageReader;
import cn.caigd.learn.nio.nioserver.IMessageReaderFactory;

/**
 * Created by jjenkov on 18-10-2015.
 */
public class HttpMessageReaderFactory implements IMessageReaderFactory {

    public HttpMessageReaderFactory() {
    }

    @Override
    public IMessageReader createMessageReader() {
        return new HttpMessageReader();
    }
}
