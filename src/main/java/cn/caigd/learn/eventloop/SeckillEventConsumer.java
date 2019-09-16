package cn.caigd.learn.eventloop;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.EventHandler;

/**
 * 消费者(秒杀处理器)
 * 创建者 科帮网
 */
public class SeckillEventConsumer implements EventHandler<SeckillEvent> {


    public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) throws Exception {
        System.out.println(JSON.toJSONString(seckillEvent));
    }
}
