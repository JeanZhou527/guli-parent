package com.jean.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data  //此注解是为了生成getset方法
public class DemoData {

    //设置excel的表头名称
    @ExcelProperty("学生编号")
    private  Integer sno;

    @ExcelProperty("学生姓名")
    private  String sname;
}
