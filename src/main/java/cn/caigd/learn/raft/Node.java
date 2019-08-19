package cn.caigd.learn.raft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Node {

    private int time;

    public void convert() {
        if (time == 0) {
            this.state = State.CANDIDATE;
        }
    }

    private String value;
    private State state;


}
