package com.tttare.springDemo.model;

import java.util.concurrent.*;

/**
 * ClassName: TreadPoolFactory <br/>
 * Description: 单例线程池对象工具类.参考博客:https://www.cnblogs.com/CarpenterLee/p/9558026.html<br/>
 * date: 2019/9/23 20:18<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class TreadPoolFactory {

    private volatile static ExecutorService executorService;

    // getDefaultTreadPool
    public static ExecutorService getDefaultTreadPool(){
        //创建有界任务队列的线程池 避免线程耗尽
        /*参数一: 线程池长期维持的线程数，即使线程处于Idle状态，也不会回收
        参数二:线程数上限
        参数三:超过corePoolSize的线程的idle时长，超过这个时间，多余的线程会被回收
        参数四:任务的排队队列
        参数五:拒绝策略 任务队列满时 执行策略*/
        if(null == executorService){
            executorService = new ThreadPoolExecutor(2, 4,
                    0, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(512), // 使用有界队列，避免OOM
                    new ThreadPoolExecutor.DiscardPolicy());
            //AbortPolicy:抛出RejectedExecutionException
            //DiscardPolicy 什么也不做，直接忽略
            //DiscardOldestPolicy 丢弃执行队列中最老的任务，尝试为当前提交的任务腾出位置
            //CallerRunsPolicy直接由提交任务者执行这个任务
        }
        return executorService;
    }

    //线程池 操作示例
    //Future  线程池异常处理类  异常将由 future.get() 时传递给调用者
    /*Future<Object> future = executorService.submit(new Callable<Object>() {
        @Override
        public Object call() throws Exception {
            System.out.print("---------1----------------");
            return null;
            //throw new RuntimeException("exception in call~");// 该异常会在调用Future.get()时传递给调用者
        }
    });*/

    //异常抛出方法
    /*try {
            Object result = future.get();
        } catch (InterruptedException e1) {
            // interrupt
            e1.printStackTrace();
        } catch (ExecutionException e2) {
            // exception in Callable.call()
            e2.printStackTrace();
        }*/

    //  ***********************************
    //submit()向线程池提交任务后会返回一个Future，调用V Future.get()方法能够阻塞等待执行结果，
    // V get(long timeout, TimeUnit unit)方法可以指定等待的超时时间。
    //V Future.get(long timeout, TimeUnit unit)方法可以指定等待的超时时间，超时未完成会抛出TimeoutException
    //多任务的超时处理
    /*public void testLatch(ExecutorService executorService, List<Runnable> tasks)
            throws InterruptedException{

        CountDownLatch latch = new CountDownLatch(tasks.size());
        for(Runnable r : tasks){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        r.run();
                    }finally {
                        latch.countDown();// countDown
                    }
                }
            });
        }
        latch.await(10, TimeUnit.SECONDS); // 指定超时时间
    }*/
}
