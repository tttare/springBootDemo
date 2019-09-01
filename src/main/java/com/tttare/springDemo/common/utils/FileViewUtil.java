package com.tttare.springDemo.common.utils;

import com.tttare.springDemo.model.FileObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

//  文件夹文件展示 工具类
public class FileViewUtil {

    //递归查找文件及文件夹
    public static FileObject getSubFile(File file, FileObject fo,int depth) {
        String size = "";
        long fileS = file.length();
        // DecimalFormat df = new DecimalFormat("#.00");
        if (fileS < 1024) {
            size = fileS + "BT";
            // size = df.format((double) fileS) + "BT";
        } else if (fileS < 1048576) {
            size = fileS / 1024 + "KB";
            // size = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            size = fileS / 1048576 + "MB";
            // size = df.format((double) fileS / 1048576) + "MB";
        } else {
            size = fileS / 1073741824 + "GB";
            // size = df.format((double) fileS / 1073741824) +"GB";
        }
        fo.setSize(size);
        fo.setName(file.getName());
        fo.setPath(file.getAbsolutePath());
        Date date = new Date(file.lastModified());
        fo.setDepth(depth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        fo.setDate(sdf.format(date));
        if (file.isDirectory()) {
            fo.setIsFile(false);
            File[] files = file.listFiles();
            Arrays.sort(files, new CompratorByLastModified());
            if (files.length >= 0) {
                for (int i = 0; i < files.length; i++) {
                    FileObject subfo = new FileObject();
                    subfo.setParent_id(fo.getId());
                    subfo.setId(fo.getId() + String.valueOf(i + 1));
                    depth++;
                    fo.getChildren().add(getSubFile(files[i], subfo,depth));
                }
            }
        }else{
            fo.setIsFile(true);
        }
        return fo;
    }


    //根据最后一次修改时间排序
    static class CompratorByLastModified implements Comparator<File> {
        public int compare(File f1, File f2) {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0)
                return -1;
            else if (diff == 0)
                return 0;
            else
                return 1;
        }

        public boolean equals(Object obj) {
            return true;
        }
    }

}
