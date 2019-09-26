package com.tttare.springDemo.model.designPattern.decorator;

/**
 * ClassName: Test01 <br/>
 * Description: <br/>
 * date: 2019/9/24 21:15<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class Test01 {

    public static void  main(String[] args){
        /*//不加调料的HouseBlend
        Beverage beverage = new HouseBlend();
        System.out.println(beverage.getDescription()+"     $"+beverage.cost());

        //加入调料的HouseBlend
        Beverage beverage2 = new HouseBlend();
        beverage2=new Mocha(beverage2);
        beverage2=new Mocha(beverage2);
        System.out.println(beverage2.getDescription()+"    $"+beverage2.cost());
*/

        //用装饰者模式来简化嵌套查询


    }
}
