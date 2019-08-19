package cn.caigd.learn.concurrency.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class UnreentrantLock {
    private AtomicReference<Thread> owner = new AtomicReference<>();

    public void lock() {
        Thread current = Thread.currentThread();
        System.out.println(current.getName());
        for (; ; ) {
            if (!owner.compareAndSet(null, current)) {
                return;
            }
        }
    }

    public void unlock() throws Exception{
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName());
        owner.compareAndSet(thread, null);
    }

    public static void main(String[] args) throws Exception{
        UnreentrantLock unreentrantLock = new UnreentrantLock();
        new Thread(() -> {
            unreentrantLock.lock();
            unreentrantLock.lock();
        }).start();
        TimeUnit.SECONDS.sleep(100);
    }
}
