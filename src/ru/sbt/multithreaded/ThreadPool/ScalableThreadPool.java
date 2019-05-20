package ru.sbt.multithreaded.ThreadPool;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {

    private final Queue<Runnable> tasksQueue = new ArrayDeque<Runnable>();
    private volatile int currentWorkedThread = 0;
    private volatile int currentPerformedThread = 0;
    private final Object queueLock = new Object();
    private final int minThread;
    private final int maxThread;

    public ScalableThreadPool(int minThread, int maxThread) {
        this.minThread = minThread;
        this.maxThread = maxThread;
    }

    @Override
    public void start() {
        for (int i = 0; i < minThread; i++) {
            new ScalableThreadPool.ScalableThread().start();
            currentPerformedThread++;
        }
    }

    public void execute(Runnable runnable) {
        synchronized (queueLock) {
            tasksQueue.add(runnable);
            if (currentWorkedThread + tasksQueue.size() >= currentPerformedThread  && currentPerformedThread < maxThread) {
                currentPerformedThread++;
                System.out.println("Perfoemed: " + currentPerformedThread);
                new ScalableThreadPool.ScalableThread().start();
            } else queueLock.notify();
        }
    }

    public class ScalableThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (queueLock) {
                    if(tasksQueue.isEmpty() && currentPerformedThread > minThread) {
                        currentPerformedThread--;
                        System.out.println("Exit: " + Thread.currentThread());
                        System.out.println("Perfoemed: " + currentPerformedThread);
                        break;
                    }
                }
                Runnable task;
                synchronized (queueLock) {
                    while (tasksQueue.isEmpty()) {
                        try {
                            queueLock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("thread was interrupted");
                        }
                    }
                    currentWorkedThread++;
                    task = tasksQueue.poll();
                }
                try {
                    task.run();
                    currentWorkedThread--;
                } catch (Exception e) {
                    System.out.println("Run error");
                }

            }
        }
    }
}