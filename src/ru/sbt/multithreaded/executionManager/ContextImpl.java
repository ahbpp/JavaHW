package ru.sbt.multithreaded.executionManager;

import java.util.ArrayList;
import java.util.List;

public class ContextImpl implements Context {

    private Runnable[] tasks;
    private Runnable callback;
    private List<Thread> threadsList = new ArrayList<>();

    private  int completedCount;
    private  int failedCount;
    private  int interruptedCount;
    private boolean isFirst = true;

    public ContextImpl(Runnable callback, Runnable...tasks) {
        this.callback = callback;
        this.tasks = tasks;
        startThreads();
    }

    private void startThreads() {

        for (Runnable task : tasks) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!Thread.currentThread().isInterrupted()) {
                        try {
                            task.run();
                            synchronized (this) {
                                completedCount++;
                            }
                        } catch (Exception e) {
                            synchronized (this) {
                                failedCount++;
                            }
                        }
                    } else {
                        synchronized (this) {
                            interruptedCount++;
                        }
                    }
                    synchronized (this) {
                        if (isFinished() && isFirst) {
                            isFirst = false;
                            new Thread(callback).start();
                        }
                    }
                }

            });
            threadsList.add(thread);
            thread.start();
        }

    }

    @Override
    public int getCompletedTaskCount() {
        return completedCount;
    }

    @Override
    public int getFailedTaskCount() {
        return failedCount;
    }

    @Override
    public int getInterruptedTaskCount() {
        return interruptedCount;
    }

    @Override
    public void interrupt() {
        for (Thread thread : threadsList) {
            thread.interrupt();
        }
    }

    @Override
    public synchronized boolean isFinished() {
        int done = getCompletedTaskCount() + getFailedTaskCount() + getInterruptedTaskCount();
        return (done == tasks.length);
    }
}
