package cn.caigd.learn.concurrency.reentrant;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest extends Thread {
    public static ReentrantLock lock = new ReentrantLock();
    public   int i = 0;

    public ReentrantLockTest(String name) {
        super.setName(name);
    }

    //    @Override
//    public void run() {
//        for (int j = 0; j < 100000000; j++) {
//            lock.lock();
//            try {
//                //System.out.println(this.getName() + " " + i);
//                i++;
//            } finally {
//                lock.unlock();
//            }
//        }
//    }
    @Override
    public void run() {
        synchronized (this) {
            for (int j = 0; j < 1000000; j++)
                i++;
        }
    }

    public  synchronized void adder() {
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        long a = System.currentTimeMillis();
        ReentrantLockTest test1 = new ReentrantLockTest("thread1");
        ReentrantLockTest test2 = new ReentrantLockTest("thread2");
        test1.start();
        test2.start();
        //test1.join();
        //test2.join();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(test2.i);
        System.out.println(System.currentTimeMillis() - a);

    }
}
