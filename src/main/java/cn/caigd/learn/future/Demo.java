package cn.caigd.learn.future;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Demo {
    public static void main(String[] args) {
        List<CompletableFuture<Integer>> futures = Lists.newArrayList();
        for (int i = 0; i < 1000; i++) {
            CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 10);
            futures.add(completableFuture);
        }
        //CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync();

    }
}
