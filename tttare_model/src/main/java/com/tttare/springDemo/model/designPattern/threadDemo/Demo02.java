package com.tttare.springDemo.model.designPattern.threadDemo;

/**
 * ClassName: Demo02 <br/>
 * Description: <br/>
 * date: 2019/9/26 20:33<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class Demo02 {

    static class service{
        private String usernameParam;
        private String passwordParam;
        private String anyString= new String();

        public void setUsernamePassword(String username,String password){
            try {
                String anyString= new String();
                synchronized (anyString) {
                    //  synchronized 里的参数 叫对象监视器
                    //   *************************  会造成阻塞(同步运行)  *********************
                    //  synchronized(this) 和 synchronized方法 效果相同:同一时间只有一个线程可以执行synchronized同步方法中的代码
                    //  synchronized static和synchronized(Class)是对类的所有对象实例起作用的,造成阻塞

                    //   *************************  不会造成阻塞(异步运行)  *********************
                    //  如上,synchronized(对象),每次进入synchronized代码块的对象监视器地址都不同,
                    //  锁非this对象具有一定的优点：如果一个类中有很多个synchronized方法，这时虽然能实现同步，但会受到阻塞，所以影响消息；但如果使用同步代码块锁非this对象，则synchronized(非this)代码块的程序与同步方法是异步的。不与其他锁this同步方法争抢this锁，则可以大大提高运行效率。


                    //  *************同步与异步 取决于  锁是否只有一把 *****************
                    //  synchronized方法  synchronized(this) synchronized(Class) 都会造成一把锁来管理方法 造成阻塞

                    //   但 synchronized(new Object()) 就会产生很多新锁,
                    System.out.println("线程名称为： "+Thread.currentThread().getName()+"在 "+System.currentTimeMillis()+" 进入同步代码块");
                    usernameParam=username;
                    Thread.sleep(3000);
                    passwordParam=password;
                    System.out.println("线程名称为： "+Thread.currentThread().getName()+"在 "+System.currentTimeMillis()+" 离开同步代码块");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    static class ThreadA extends Thread {

        private service service;

        public ThreadA(service service) {
            super();
            this.service = service;
        }

        @Override
        public void run() {
            service.setUsernamePassword("a", "aa");
        }
    }

    static class ThreadB extends Thread {

        private service service;

        public ThreadB(service service) {
            super();
            this.service = service;
        }

        @Override
        public void run() {
            service.setUsernamePassword("b", "bb");
        }
    }

    public static void main(String[] args) {
        service service =new service();
        ThreadA a= new ThreadA(service);
        a.setName("A");
        a.start();
        ThreadB b=new ThreadB(service);
        b.setName("B");
        b.start();
    }
}
