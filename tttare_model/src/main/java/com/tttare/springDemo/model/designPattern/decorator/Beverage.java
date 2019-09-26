package com.tttare.springDemo.model.designPattern.decorator;

/**
 * ClassName: Beverage <br/>
 * Description: <br/>
 * date: 2019/9/24 21:06<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */

public abstract class Beverage {
    //父类作为一个抽象类呈现
    String descriptionString = "未知饮料";

    //类型描述父类实现
    public String getDescription() {
        return descriptionString;
    }

    //子类需要实现的抽象方法
    public abstract double cost();
}
