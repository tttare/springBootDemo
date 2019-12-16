package com.tttare.springDemo.javaseTest;

/**
 * ClassName: Z <br/>
 * Description: <br/>
 * date: 2019/10/12 9:58<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
class X{
    Y y=new Y();
    public X(){
        System.out.print("X");
    }
}
class Y{
    public Y(){
        System.out.print("Y");
    }
}
public class Z extends X{
    Y y=new Y();
    public Z(){
        System.out.print("Z");
    }
    public static void main(String[] args) {
        new Z();
    }
}
