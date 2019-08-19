package cn.caigd.learn.algri.LinkList;

public class SingleList {
    class SingleNode {
        private SingleNode next;
        private int value;
    }

    private SingleNode head;
    private SingleNode tail;

    public SingleList(SingleNode head) {
        this.head = head;
        this.tail = head;
    }

    public void add(SingleNode node) {
        SingleNode singleNode = tail;
        singleNode.next = node;
        tail = node;
    }

    public void reverse(SingleNode head) {
        SingleNode pre = null;
        SingleNode next;
        while (head.next != null) {
            next = head.next;
            head.next = pre;
            pre = head;
            head = next;

        }
    }
}
