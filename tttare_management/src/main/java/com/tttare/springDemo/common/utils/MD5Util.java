package com.tttare.springDemo.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;
// MD5工具类
public class MD5Util {

    /**利用MD5进行加密*/
    public static String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        return base64en.encode(md5.digest(str.getBytes("utf-8")));
    }

    /**判断用户密码是否正确
     *newpasswd 用户输入的密码
     *oldpasswd 正确密码*/
    public static boolean checkpassword(String newpasswd,String oldpasswd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return EncoderByMd5(newpasswd).equals(oldpasswd);

    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String str="passwordTedsadsadsadsadsadsdsasdsadasdsadsadasdsawwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwt";
        System.out.println(EncoderByMd5(str));
        System.out.println(EncoderByMd5(str).length());
    }
}
