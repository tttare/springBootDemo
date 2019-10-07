package com.tttare.springDemo.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * ClassName: CommonUtil <br/>
 * Description: <br/>
 * date: 2019/9/3 11:08<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class CommonUtil {

    //生产UUID
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    //复杂java对象的json转化:适用于含有date信息等包装类型的复杂对象
    public static <T> T parseToJava(String jsonStr,Class<T> clazz){
        return JSON.parseObject(jsonStr,new TypeReference<T>(){});
    }

    // date -> 时间字符串
    public static String dateFormat(Date date,String format){
        if(date==null){
            throw new NullPointerException("date is null");
        }
        if(format==null){
            format ="yyyy -MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    // 为当前date 加或减  时H 天 D  月M  年Y
    public Date addOrSubDate(Date currentDate,char target,int num){
        return null;
    }

    public  static void main(String[] args){
        System.out.print(getUUID());
    }
}
