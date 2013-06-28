package com.fileshare.concurrency;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Jan Paw
 *         Date: 6/8/13
 */

public class Parallel {
    private final static int NUM_CORES = Runtime.getRuntime().availableProcessors();

    public static <T> void For(final Iterable<T> elements, final Operation<T> operation) {
        For(NUM_CORES, elements, operation);
    }

    public static <T> void For(final int threadsNumber, final Iterable<T> elements, final Operation<T> operation) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadsNumber, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
        forkJoinPool.invokeAll(createCallable(elements, operation));
    }

    private static <T> Collection<Callable<Void>> createCallable(final Iterable<T> elements, final Operation<T> operation) {
        List<Callable<Void>> callable = new LinkedList<>();
        for (final T elem : elements) {
            callable.add(new Callable<Void>() {

                @Override
                public Void call() {
                    operation.perform(elem);
                    return null;
                }
            });
        }

        return callable;
    }

    public static interface Operation<T> {
        public void perform(T pParameter);
    }
}