package com.tttare.springDemo.javaseTest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * ClassName: Test01 <br/>
 * Description: <br/>
 * date: 2019/10/11 10:41<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public abstract class Test01 implements InterfaceTest{


    @Override
    public void method01() throws NullPointerException {
        String s="";
        int[][] a=new int[6][];
    }

    //验证hashCode的 重复key
    public static void method02(){
        ModelTest m1 = new ModelTest("m1",11);
        ModelTest m2 = new ModelTest("m2",22);

        HashMap<ModelTest, String> map = new HashMap<>();
        map.put(m1,"a");
        map.put(m2,"b");
        m1.setName("newName");
        map.put(m1,"c");
        //Lambda表达式，代码简单易懂
        map.forEach((k,v) ->{
            System.out.println(k +","+ v);
        });

    }

    public static void main(String[] args){
        /*String[] arr = {"4","7","5"};
        List<String> ints = new ArrayList(Arrays.asList(arr));
        ints.add("8");
        ints.add(0,"0");
        ints.remove(0);
        ints.stream().forEach(System.out::print);*/
        //method02();
        /*String str1="aaa,bbb";
        System.out.println(str1.replace(",","|"));
        System.out.println(str1);
        System.out.println(str1.replace(",","|"));
        System.out.println(str1.substring(2));
        System.out.println(str1);*/
        /*String str =
                "";
        System.out.print(str.length());
        System.out.print(str.split(",").length);
        int  []a[]=new int[10][10];*/
        int arr [] =new int[5];
        System.out.print(arr[0]);
    }

    public final void f1() {}

    protected abstract void f2();

    static final void fq(){}

}
