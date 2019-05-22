package ru.sbt.multithreaded.executionManager;

public interface ExecutionManager {
    Context execute(Runnable callback, Runnable... tasks);
}

