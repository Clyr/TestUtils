package com.clyr.utils.utilshelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by M S I of clyr on 2019/6/12.
 */
public class ThreadManager2 {
    //通过ThreadPoolExecutor的代理类来对线程池的管理
    private static ThreadPollProxy mThreadPollProxy;
    //单列对象
    public static ThreadPollProxy getThreadPollProxy(){
        synchronized (ThreadPollProxy.class) {
            if(mThreadPollProxy==null){
                mThreadPollProxy=new ThreadPollProxy(3,6,1000);
            }
        }
        return mThreadPollProxy;
    }
    //通过ThreadPoolExecutor的代理类来对线程池的管理
    public static class ThreadPollProxy{
        private ThreadPoolExecutor poolExecutor;//线程池执行者 ，java内部通过该api实现对线程池管理
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        public ThreadPollProxy(int corePoolSize,int maximumPoolSize,long keepAliveTime){
            this.corePoolSize=corePoolSize;
            this.maximumPoolSize=maximumPoolSize;
            this.keepAliveTime=keepAliveTime;
        }
        //对外提供一个执行任务的方法
        public void execute(Runnable r){
            if(poolExecutor==null||poolExecutor.isShutdown()){
                poolExecutor=new ThreadPoolExecutor(
                        //核心线程数量
                        corePoolSize,
                        //最大线程数量
                        maximumPoolSize,
                        //当线程空闲时，保持活跃的时间
                        keepAliveTime,
                        //时间单元 ，毫秒级
                        TimeUnit.MILLISECONDS,
                        //线程任务队列
                        new LinkedBlockingQueue<Runnable>(),
                        //创建线程的工厂
                        Executors.defaultThreadFactory());
            }
            poolExecutor.execute(r);
        }
    }
    public static void test(){
        //    1.SingleThreadExecutor(单线程)
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(()->{

        });

//        2.FixedThreadPool(固定线程)
        int num = 5;
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(num);
        fixedThreadPool.execute(()->{

        });
//        3.CachedThreadPool(带缓存)

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(()->{

        });

//        4.ScheduledThreadPool(定时执行)

        int corePoolSize =5;
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(corePoolSize);
        scheduledThreadPool.schedule(()->{

        }, 2000, TimeUnit.MILLISECONDS);

    }


    /*public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService (new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()));
    }*/

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }
    /*public ScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue());
    }*/
}
