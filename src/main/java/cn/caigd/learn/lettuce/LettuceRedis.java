package cn.caigd.learn.lettuce;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.rx.RedisStringReactiveCommands;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;

public class LettuceRedis {
    private static RedisClient client = RedisClient.create("redis://192.168.0.166:6379");

    public static void main(String[] args) throws Exception {
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStringReactiveCommands<String, String> reactive = connection.reactive();
        for (int i = 0; i < 1000000; i++) {
            reactive.get("hello").subscribe(new Action1<String>() {
                @Override
                public void call(String value) {
                    System.out.println(value);
                }
            });

        }
        TimeUnit.MILLISECONDS.sleep(1000);
    }

}
