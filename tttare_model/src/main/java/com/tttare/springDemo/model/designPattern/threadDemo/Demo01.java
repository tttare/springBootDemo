package com.tttare.springDemo.model.designPattern.threadDemo;

/**
 * ClassName: Demo01 <br/>
 * Description: <br/>
 * date: 2019/9/26 20:07<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class Demo01 {
    public void doLongTimeTask(){
        // 使用synchronized声明的方法在 某些情况下是有弊端的，比如A线程调用同步的方法执行一个长时间的任务，
        // 那么B线程就必须等待比较长的时间才能执行，这种情况可以使用synchronized代码块去优化代码执行时间，
        // 也就是通常所说的减少锁的粒度。
        try {

            System.out.println("当前线程开始：" + Thread.currentThread().getName() +
                    ", 正在执行一个较长时间的业务操作，其内容不需要同步");
            Thread.sleep(2000);

            synchronized(this){
                //  同步锁 这波 使用的是对象锁
                System.out.println("当前线程：" + Thread.currentThread().getName() +
                        ", 执行同步代码块，对其同步变量进行操作");
                Thread.sleep(1000);
            }
            System.out.println("当前线程结束：" + Thread.currentThread().getName() +
                    ", 执行完毕");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Demo01 otz = new Demo01();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                otz.doLongTimeTask();
            }
        },"t1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                otz.doLongTimeTask();
            }
        },"t2");
        t1.start();
        t2.start();

    }

}
