package com.tttare.springDemo.model.designPattern.decorator;

/**
 * ClassName: CondimentDecorator <br/>
 * Description: <br/>
 * date: 2019/9/24 21:08<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public abstract class CondimentDecorator extends Beverage {

    // 调料材料子类必须实现的抽象方法
    public abstract String getDescription();


}
