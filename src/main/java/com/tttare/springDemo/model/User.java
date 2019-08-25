package com.tttare.springDemo.model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {

    private String userId;
    private String userName;
    private String passWord;
    private Date createDate;
    private Date updateDate;
    /***用户类型:
            0 一般用户：无管理后台的权限，无上传下载权限；
            1 vip用户：无管理后台权限，有上传下载权限；
            2 管理员：有管理后台权限及vip用户权限，有封禁权限，无资源的删除权限；
            3 超级管理员，拥有项目已知的所有权限；*/
    private Integer type;
    //是否被封禁： 0或null为否，1位已经封禁
    private Integer isBan;


}
