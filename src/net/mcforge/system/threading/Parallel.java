package net.mcforge.system.threading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class will help with running Parallel tasks
 * such as Parallel for loops, and parallel programming.
 * @author MCForgeTeam
 *
 */
public class Parallel {
    
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    
    private static final ExecutorService PROCESS = Executors.newFixedThreadPool(MAX_THREADS);
    
    private static LinkedList<ParallelCache> linked_cache = new LinkedList<ParallelCache>();
    
    /**
     * Loop through a {@link Collection} of <E> using multiple thread pools. The max number of
     * threads used will equal the amount of available processors defined in {@link Runtime#availableProcessors()}
     * @param list
     *            An {@link Iterable} array to loop through.
     * @param method
     *              The {@link ParallelRunner} to call.
     */
    public static <E> void parallelFor(Iterable<E> list, ParallelRunner<E> method) {
        parallelFor(list, method, MAX_THREADS);
    }
    
    /**
     * Loop through a {@link Collection} of <E> using multiple thread pools.
     * @param list
     *            An {@link Iterable} array to loop through.
     * @param method
     *              The {@link ParallelRunner} to call.
     * @param threads
     *               The max number of threads to use.
     */
    public static <E> void parallelFor(Iterable<E> list, final ParallelRunner<E> method, int threads) {
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        ArrayList<Future<?>> tasks = new ArrayList<Future<?>>();
        try {
            //exec.invokeAll(createCallables(list, method));
            int i = 0;
            for (final E item : list) {
                final int temp = i;
                tasks.add(exec.submit(new Runnable() {
                    @Override
                    public void run() {
                        method.run(item, temp);
                    }
                }));
                i++;
            }
            for (Future<?> f : tasks) {
                if (!f.isDone()) {
                    try {
                        f.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            exec.shutdown();
            tasks.clear();
        }
    }
    
    public static Future<?> execute(Runnable r) {
        long ID = Thread.currentThread().getId();
        Future<?> toreturn = PROCESS.submit(r);
        ParallelCache c = new ParallelCache();
        c.ParentID = ID;
        c.task = toreturn;
        linked_cache.add(c);
        return toreturn;
    }
    
    public static void waitForComplete() {
        long ID = Thread.currentThread().getId();
        Iterator<ParallelCache> temp = linked_cache.iterator();
        while (temp.hasNext()) {
            ParallelCache c = temp.next();
            if (c.ParentID == ID) {
                c.waitForTask();
                temp.remove();
            }
        }
    }
    
    private static class ParallelCache {
        private Future<?> task;
        private long ParentID;
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ParallelCache) {
                ParallelCache c = (ParallelCache)obj;
                return c.ParentID == ParentID && c.task == task;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return (int)ParentID;
        }
        
        @Override
        public String toString() {
            return "Thread: " + ParentID;
        }
        
        public void waitForTask() {
            if (!task.isDone()) {
                try {
                    task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public interface ParallelRunner<E> {    
        public void run(E object, int index);
    }
}
