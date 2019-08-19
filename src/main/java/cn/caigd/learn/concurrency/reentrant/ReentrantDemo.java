package cn.caigd.learn.concurrency.reentrant;

public class ReentrantDemo implements Runnable {
    public synchronized void get() {
        System.out.println(Thread.currentThread().getName());
        set();
    }

    public synchronized void set() {
        System.out.println(Thread.currentThread().getName());
    }

    @Override
    public void run() {
        get();
    }

    public static void main(String[] args) {
        ReentrantDemo rt = new ReentrantDemo();
        for (int i = 0; i < 1000; i++) {
            new Thread(rt).start();
        }
    }
}
