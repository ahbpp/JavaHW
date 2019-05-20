package ru.sbt.multithreaded.get;

import ru.sbt.multithreaded.ThreadPool.FixedThreadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

public class test {
    public static void main(String[] args) {
        Callable<Integer> callableObj = () -> { System.out.println("Runnable running " + Thread.currentThread());
                                                Thread.sleep(100);
                                                return 20; };
        Task<Integer> task = new Task<Integer>(callableObj);

        int threadCount = 5;
        List<Thread> workers = new ArrayList<>(threadCount);
        Runnable runnable =
                () -> { System.out.println(task.get());};
        for (int i = 0; i < threadCount; i++) {
            workers.add(new Thread(runnable));
        }
        for (Thread worker : workers) {
            worker.start();
        }
    }
}