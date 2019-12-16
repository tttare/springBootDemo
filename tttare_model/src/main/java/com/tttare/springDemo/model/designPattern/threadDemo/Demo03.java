package com.tttare.springDemo.model.designPattern.threadDemo;

/**
 * ClassName: Demo03 <br/>
 * Description: synchronized 的同步差异<br/>
 * date: 2019/9/26 23:09<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class Demo03 {


    // 情况一  new 一个SynchronizedDemo,两个线程来调用method1
    public static void test01() {
        SynchronizedDemo sysc = new SynchronizedDemo();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc.method01();
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc.method01();
            }
        });
        a.start();
        b.start();
    }

    // 情况二  new 一个SynchronizedDemo,两个线程来调用method2(synchronized方法)
    // 结果 : 方法只能不能同步执行
    // 结论 : 被synchronized修饰的方法 同一时间,只能被一个线程调用
    public static void test02() {
        SynchronizedDemo sysc = new SynchronizedDemo();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc.method02();
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc.method02();
            }
        });
        a.start();
        b.start();
    }

    // 情况三  new 两个SynchronizedDemo,两个线程来调用method2(synchronized方法)
    // 结果 : 方法只能同步执行
    // 结论 : synchronized方法 :当多个线程,调用多个实例时,方法不会阻塞
    public static void test03() {
        SynchronizedDemo sysc1 = new SynchronizedDemo();
        SynchronizedDemo sysc2 = new SynchronizedDemo();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc1.method02();
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc2.method02();
            }
        });
        a.start();
        b.start();
    }

    // 情况三 :使用synchronized(this),分别用一个实例和多个实例,多线程调用method03()
    // 结果 : 同一个对象,同一时间,只能被一个线程调用
    // 补充 : synchronized(Class) ,类锁将导致该类所有实例的该代码块,同一时间,只能被一个线程调用,
    //      任何类的class,都是此效果,synchronized(String.class)与synchronized(SynchronizedDemo.class)效果一致

    // 对象锁测试
    //  结论一 : 固定对象锁(常量字符串):该类所有实例的method03方法,同一时间,只能被一个线程调用
    //  结论二 : 非固定对象对象锁(method06):锁失效
    public static void test04() {
        //SynchronizedDemo sys = new SynchronizedDemo();
        SynchronizedDemo sysc1 = new SynchronizedDemo();
        SynchronizedDemo sysc2 = new SynchronizedDemo();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc1.method04();
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc1.method41();
            }
        });
        a.start();
        b.start();
    }


    // synchronized(this) 对其他synchronized方法的阻塞
    // 多个对象实例方别调用 不会阻塞
    // 同一个对象 同一时间 只能有一个线程加入synchronized(this)代码块,
    // 即使访问不同的synchronized(this)代码块,多个代码块之间形成了阻塞
    public static void test05() {
        //SynchronizedDemo sys = new SynchronizedDemo();
        SynchronizedDemo sysc1 = new SynchronizedDemo();
        SynchronizedDemo sysc2 = new SynchronizedDemo();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc1.method02();
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                sysc2.method07();
            }
        });
        a.start();
        b.start();
    }


    public static void main(String[] args) {
        //test01();
        //test02();
        //test03();
        //test04();
        test05();
    }

    //  *************************************************
    //  现象一:同一对象,同一时刻,代码块只能由一个线程访问
    //  现象二:该类所有实例,同一时刻,代码块只能由一个线程访问
    //  现象三:异步,未实现同步操作

    //  关于synchronized关键字的一些结论
    //   synchronized() 和 synchronized方法
    //   *********对线程加同步锁,产生的现象不同,主要是钥匙(监控对象)的多少*******
    //   synchronized(this)和synchronized方法钥匙都只有一个,那就是对象自身;
    //   但是如果多new几个对象,那就形成的多个对象对应的代码块都有自己的钥匙,出现现象一

    //   synchronized(class) 和 synchronized(固定对象)
    //   很明显,内存中的钥匙始终只有一把,无论new多少个对象,钥匙也只会有一把
    //   即使多个线程方别访问多个对象的同步代码块,也得争抢一把钥匙,出现现象二

    //   synchronized(非固定对象),每个线程进入代码块前,都获得了一个新的钥匙,故synchronized失效

    //   多个synchronized代码块之间的阻塞,多个synchronized代码块,
    //   如果用的同一个钥匙,同一时间,也只能有一个线程进入代码块
    //   synchronized方法与synchronized方法(this)一样,会形成阻塞
    //   synchronized==synchronized(this).他俩都是用this对象做的锁
}
