package ru.sbt.multithreaded.get;

import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private final Object lockFirst = new Object();
    private volatile boolean isFirst = true;
    private volatile boolean isComputed;
    private volatile RuntimeException exception = null;
    private volatile T result;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {
        if (!isFirst) return get_result();
        if (doFirst()) return compute();
        return get_result();
    }

    private boolean doFirst() {
        synchronized (lockFirst) {
            if (isFirst) {
                isFirst = false;
                return true;
            }
            return false;
        }
    }

    private T compute() {
        try {
            result = callable.call();
        } catch (Exception e) {
            exception = new TaskException("Exception during compute callable result");
        }
        isComputed = true;
        synchronized (this) {
            notifyAll();
        }
        return get_result();
    }

    private T get_result() {
        if (!isComputed) {
            synchronized (this) {
                while (!isComputed) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Exception:" + e);
                    }
                }
            }
        }
        if (exception != null) throw exception;
        return result;
    }
}