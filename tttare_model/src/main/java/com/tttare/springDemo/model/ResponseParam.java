package com.tttare.springDemo.model;

import lombok.Getter;

/**
 * ClassName: ResponseParam <br/>
 * Description: 接口响应实体类<br/>
 * date: 2019/9/6 14:15<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
public class ResponseParam {

    // 响应码
    private String code;
    // 数据
    private Object data;
    // 接口响应描述
    private String desc;

    public ResponseParam(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public ResponseParam(String code,Object data,String desc){
        this.code=code;
        this.desc=desc;
        this.data=data;
    }
}
