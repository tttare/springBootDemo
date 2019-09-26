package com.tttare.springDemo.model.designPattern.threadDemo;

/**
 * ClassName: SynchronizedDemo <br/>
 * Description: <br/>
 * date: 2019/9/26 23:11<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class SynchronizedDemo {

    public void commenMethod(){
        for(int i=0;i<10;i++){
            System.out.println("******"+Thread.currentThread().getName()+"数字为="+i);
        }
    }

    // 无锁
    public void method01(){
        commenMethod();
    }

    // 同步方法
    public synchronized void method02(){
        commenMethod();
    }

    // synchronized(this)  this锁
    public  void method03(){
        synchronized(this){
            commenMethod();
        }

    }

    // synchronized(Class)  类锁
    public  void method04(){
        synchronized(String.class){
            commenMethod();
        }
    }

    //对象锁(固定对象)
    public void method05(){
        synchronized("test"){
            commenMethod();
        }
    }

    //对象锁(每次调用方法,都实例化监控对象)
    public  void method06(){
        String str = new String("test");  //每次new 产生新的内存地址 (synchronized失效)
        //String str = "test";  //指向固定地址,与method05效果一致
        synchronized(str){
            commenMethod();
        }
    }

    public static void main(String[] args) {
        /*Class<String> stringClass = String.class;
        Class<SynchronizedDemo> synchronizedDemoClass = SynchronizedDemo.class;*/
        String a ="sss";
        String b ="sss";
        System.out.println(a == b);
    }
}
