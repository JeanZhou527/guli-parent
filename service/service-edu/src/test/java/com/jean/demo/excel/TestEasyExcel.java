package com.jean.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args)
    {
        write();
        read();
    }

    //实现excel读操作
    public static void  read(){

        //1、设置读入文件夹地址和excel文件名称
        String fileName = "D:\\write.xlsx";

        //2、调用easyexcel里面的读方法实现读操作
        //read方法两个参数，第一个参数：文件路径名称，第二个参数：实现类class,第三个参数：监听器类
        EasyExcel.read(fileName, DemoData.class,new ExcelListener()).sheet().doRead();
    }


    //实现excel写的操作
    public static void  write(){

        //1、设置写入文件夹地址和excel文件名称
        String fileName = "D:\\write.xlsx";

        //2、调用easyexcel里面的方法实现写操作
        //write方法两个参数，第一个参数：文件路径名称，第二个参数：实现类class
        EasyExcel.write(fileName, DemoData.class).sheet("学生列表").doWrite(getData());
    }

    //创建一个方法返回list集合
    private static List<DemoData> getData()
    {
       List<DemoData> list = new ArrayList<>();
            for(int i=0; i<10; i++)
            {
                DemoData data=new DemoData();
                data.setSno(i);
                data.setSname("lucy"+i);

                list.add(data);
            }
            return list;
    }
}
