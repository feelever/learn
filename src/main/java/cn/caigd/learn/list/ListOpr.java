package cn.caigd.learn.list;

public class ListOpr {

    public static Node reverse(Node head) {
        Node p = head;
        Node q = head.next;
        Node r;
        p.next = null;

        while (q != null) {
            r = q.next;
            q.next = p;
            p = q;
            q = r;
        }
        head = p;
        return head;
    }

    public static void main(String[] args) {
        int count = 9;
        Node t = new Node(1);
        Node x = t;
        for (int i = 2; i <= count; i++) {
            x = (x.next = new Node(i));
            System.out.print(x.val + " ");
        }
        System.out.println();
        t = reverse(t);
        while (t != null) {
            System.out.print(t.val + " ");
            t = t.next;
        }
    }

    public static class Node {
        int val;
        Node next;

        Node(int v) {
            val = v;
        }
    }
}