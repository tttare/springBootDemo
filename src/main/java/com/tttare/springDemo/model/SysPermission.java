package com.tttare.springDemo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ClassName: SysPermission <br/>
 * Description: <br/>
 * date: 2019/9/3 12:04<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
@Setter
public class SysPermission {

    private String permissionId;//主键.
    private String permissionName;//名称.
    private String resourceType;//资源类型，[menu|button]
    private String url;//资源路径.
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private Boolean available = Boolean.TRUE;
    //角色 -- 权限关系：多对多关系;
    private List<SysRole> roles;
}
