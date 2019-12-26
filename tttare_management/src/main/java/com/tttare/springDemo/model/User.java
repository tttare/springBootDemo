package com.tttare.springDemo.model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {

    private String userId;
    private String userName; //登录用户名
    private String nickName;//名称（昵称或者真实姓名，根据实际情况定义）
    private String password;
    private String salt;//加密密码的盐
    private String state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.
    private List<SysRole> roleList;// 一个用户具有多个角色
    private Timestamp createDate;//创建时间
    private Timestamp expiredDate;//过期日期
    private Timestamp lastLoginDate;//上次登录时间
    private String email;//邮箱
    /**
     * 密码盐. 重新对盐重新进行了定义，用户名+salt，这样就不容易被破解，可以采用多种方式定义加盐
     * @return
     */
    public String getCredentialsSalt(){
        return this.userName+this.salt;
    }
}
