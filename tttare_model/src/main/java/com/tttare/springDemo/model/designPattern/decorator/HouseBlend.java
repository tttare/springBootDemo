package com.tttare.springDemo.model.designPattern.decorator;

/**
 * ClassName: HouseBlend <br/>
 * Description: <br/>
 * date: 2019/9/24 21:09<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */

public class HouseBlend extends Beverage {

    public HouseBlend() {
        descriptionString = "House Blend Coffee";
    }

    @Override
    public double cost() {
        // TODO Auto-generated method stub
        return 5;
    }

}
