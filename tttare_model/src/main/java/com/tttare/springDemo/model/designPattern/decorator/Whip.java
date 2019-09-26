package com.tttare.springDemo.model.designPattern.decorator;

/**
 * ClassName: Whip <br/>
 * Description: <br/>
 * date: 2019/9/24 21:12<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */

public class Whip extends CondimentDecorator{
    Beverage beverage;
    double myCost = 20;

    public Whip(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ",Whip";
    }

    @Override
    public double cost() {
        return myCost + beverage.cost();
    }

}
