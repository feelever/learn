package cn.caigd.learn.nio;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class PipeDemo {
    private void pipe() throws Exception {
        Pipe pipe = Pipe.open();
        Pipe.SinkChannel sink = pipe.sink();
        String newData = "New String to write to file..." + System.currentTimeMillis();

        ByteBuffer buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());

        buf.flip();

        while(buf.hasRemaining()) {
            sink.write(buf);
        }
        Pipe.SourceChannel source = pipe.source();
    }
}
