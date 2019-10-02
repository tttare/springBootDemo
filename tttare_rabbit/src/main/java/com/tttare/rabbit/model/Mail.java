package com.tttare.rabbit.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * ClassName: Mail <br/>
 * Description: <br/>
 * date: 2019/9/28 21:38<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
@Setter
public class Mail implements Serializable {

    private static final long serialVersionUID = -8140693840257585779L;
    private String mailId;
    private String country;
    private Double weight;


    public Mail() {
    }

    public Mail(String mailId, String country, double weight) {
        this.mailId = mailId;
        this.country = country;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Mail [mailId=" + mailId + ", country=" + country + ", weight="
                + weight + "]";
    }
}
