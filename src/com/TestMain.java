package com;

/**
 * author: dongxl
 * created on: 2019-08-26 16:32
 * description:
 */
public class TestMain {
    public static void main(String[] strs) {
        String fileName = "sss2.sss1";
//        String suffix = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
        String suffix =  fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
        System.out.println(suffix);
    }

}
