package com.tttare.springDemo.javaseTest;

/**
 * ClassName: Derived <br/>
 * Description: <br/>
 * date: 2019/10/12 10:31<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
class Base
{
    public void methodOne()
    {
        System.out.print("A");
        methodTwo();
    }

    public void methodTwo()
    {
        System.out.print("B");
    }
}

public class Derived extends Base
{
    public void methodOne()
    {
        super.methodOne();
        System.out.print("C");
    }

    public void methodTwo()
    {
        super.methodTwo();
        System.out.print("D");
    }

    public static void main(String[] args){
        Base b = new Derived();
        b.methodOne();
        double v = (short) 10 / 10.2 * 2;
    }
}
