package com.tttare.springDemo.model.designPattern.decorator;

/**
 * ClassName: Mocha <br/>
 * Description: <br/>
 * date: 2019/9/24 21:11<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */

public class Mocha extends CondimentDecorator {
    Beverage beverage;
    double myCost = 20;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ",Mocha";
    }

    @Override
    public double cost() {
        return myCost + beverage.cost();
    }
}
