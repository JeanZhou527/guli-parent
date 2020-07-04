package com.jean.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data //生成get、set方法和无参构造方法  lombok里的注解
@AllArgsConstructor  //生成有参数构造方法  lombok里的注解
@NoArgsConstructor //生成无参数构造方法   lombok里的注解
@EqualsAndHashCode(callSuper=true)//生成equals和hashCode方法时，true：会去调用下父类的equals和hashCode方法，false：不会去调用父类的equals和hashCode方法
public class GuliException extends RuntimeException{
    private Integer code; //状态码
    private String msg;  //异常信息
}
