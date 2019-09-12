package com.tttare.springDemo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class FileObject {

    private String id;
    private String name;
    // true 文件 false 文件夹
    private boolean isFile;
    //文件大小 单位 Gb Mb Kb
    private String size;
    //文件最后修改时间
    private String date;
    private int depth;
    private String parent_id;
    private List<FileObject> children = new ArrayList<FileObject>();
    private String path;
    public void setIsFile (boolean b){
        this.isFile=b;
    }
    public boolean getIsFile(){
        return this.isFile;
    }
}
