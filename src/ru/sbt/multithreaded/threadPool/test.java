package ru.sbt.multithreaded.threadPool;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<Runnable> workers = new ArrayList<>();
        for(int i=0; i < 100; i++) {
            int finalI = i;
            Runnable runnable =
                    () -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Runnable running " + finalI + " " + Thread.currentThread());};
            workers.add(runnable);
        }

        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2, 50);
        scalableThreadPool.start();
        System.out.println("Tkekek");
        for (Runnable worker : workers) {
            ThreadLocalRandom.current().nextInt(20, 1000 + 1);
            scalableThreadPool.execute(worker);
        }
    }
}
