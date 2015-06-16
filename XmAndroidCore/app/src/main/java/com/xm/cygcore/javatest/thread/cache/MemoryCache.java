package com.xm.cygcore.javatest.thread.cache;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by wm on 15/6/16.
 */
public class MemoryCache<V, T> {

    class ExpCompute implements Computable<String, BigInteger> {

        @Override
        public BigInteger compute(String args) {
            return new BigInteger(args);
        }
    }


    class MemCache1 implements Computable<V, T> {

        private Computable<V, T> c;
        private Map<V, T> cache = new HashMap<V, T>();

        public MemCache1(Computable<V, T> c) {
            this.c = c;
        }

        /**
         * 计算，要同步，第一种，方法同步，很粗粒度，会很阻塞，如果操作费时
         *
         * @param args
         * @return
         */
        @Override
        public synchronized T compute(V args) {
            T result = cache.get(args);
            if (result == null) {
                result = c.compute(args);
                cache.put(args, result);
            }

            return result;
        }
    }

    class MemCache2 implements Computable<V, T> {
        private Computable<V, T> c;
        private Map<V, T> cache = new ConcurrentHashMap<V, T>(); // 里面的操作，多段锁，

        public MemCache2(Computable<V, T> c) {
            this.c = c;
        }

        /**
         * 计算，要同步，第二种，这个能减少同步阻塞，
         * 但是问题是，如果多线程同时，访问计算时，（计算很耗时），那么会造成重复计算问题
         *
         * @param args
         * @return
         */
        @Override
        public synchronized T compute(V args) {
            T result = cache.get(args);
            if (result == null) { // 在这里的判断会重复，多个thread都会判断位null
                result = c.compute(args);
                cache.put(args, result);
            }

            return result;
        }
    }


    class MemCache3 implements Computable<V, T> {

        private Map<V, Future<T>> cache = new ConcurrentHashMap<V, Future<T>>();
        private Computable<V, T> c;

        public MemCache3(Computable<V, T> c) {
            this.c = c;
        }

        @Override
        public T compute(final V args) {

            Future<T> future = cache.get(args);

            if (future == null) {
                Callable<T> callable = new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return c.compute(args);
                    }
                };

                FutureTask<T> task = new FutureTask<T>(callable);
                future = task;
                cache.put(args, future);
                task.run(); // 这里才执行，如果操作会阻塞，那么这里才会阻塞，但是已经将计算放入到了cache，所以出现重复计算的概率很低的
            }

            try {
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    class MemCache4 implements Computable<V, T> {

        private ConcurrentHashMap<V, Future<T>> cache = new ConcurrentHashMap<V, Future<T>>();
        private Computable<V, T> c;

        public MemCache4(Computable<V, T> c) {
            this.c = c;
        }

        @Override
        public T compute(final V args) {

            Future<T> future = cache.get(args);

            if (future == null) {
                Callable<T> callable = new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return c.compute(args);
                    }
                };

                FutureTask<T> task = new FutureTask<T>(callable);
                future = task;
//                cache.put(args, future);
                cache.putIfAbsent(args, future); // 同步检查
                task.run(); //
            }

            try {
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
