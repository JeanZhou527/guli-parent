package com.jean.servicebase.exceptionhandler;


import com.jean.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


//指定出现什么类型异常执行这个方法
    //全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据
    public R error (Exception e)
    {
        e.printStackTrace();
        return R.error().message("执行全局异常处理。。。");
    }

    //特定异常   实际开发中，也不一定能确定是哪个异常，所以开发中，用得少点
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody //为了返回数据
    public R error (ArithmeticException e)
    {
        e.printStackTrace();
        return R.error().message("执行ArithmeticException异常处理。。。");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody //为了返回数据
    public R error (GuliException e)
    {
        log.error(e.getMessage());
        e.printStackTrace();
//        return R.error().message("执行GuliException自定义 异常处理。。。");
         return R.error().code(e.getCode()).message(e.getMsg());

    }


}
