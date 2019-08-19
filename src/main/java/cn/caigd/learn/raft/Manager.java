package cn.caigd.learn.raft;

import com.google.common.collect.Lists;

import java.util.List;

public class Manager {

    public List<Node> init(int size) {
        List<Node> nodes = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            nodes.add(Node.builder().state(State.FOLLOWER).build());
        }
        return nodes;
    }
}
