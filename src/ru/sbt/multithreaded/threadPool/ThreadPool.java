package ru.sbt.multithreaded.threadPool;

public interface ThreadPool {
    void start();

    void execute(Runnable runnable);
}