package ru.sbt.multithreaded.executionManager;

import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {

        Runnable callback = () -> System.out.println("Call!");

        List<Runnable> runnables = new ArrayList<>();
        for(int i=0; i < 10; i++) {
            int finalI = i;
            Runnable runnable =
                    () -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Runnable running " + finalI + " " + Thread.currentThread());};
            runnables.add(runnable);
        }


        ExecutionManager executionManager = new ExecutionManagerImpl();

        executionManager.execute(callback, runnables.get(0), runnables.get(1), runnables.get(2), runnables.get(3), runnables.get(4));

    }

}
