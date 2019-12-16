package com.tttare.springDemo.javaseTest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ClassName: ModelTest <br/>
 * Description: <br/>
 * date: 2019/10/11 22:16<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
@Setter
@ToString
public class ModelTest {

    private String name;

    private int age;

    public ModelTest() {

    }
    public ModelTest(String name, int age) {
        this.name = name;
        this.age = age;
    }


}
