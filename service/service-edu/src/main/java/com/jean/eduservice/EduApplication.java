package com.jean.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient //开启服务注册
@EnableFeignClients //开启服务调用  代表它（消费端）要调用其他的微服务
@SpringBootApplication
//这样就会扫描该项目依赖的其他模块下的com.jean包（也就是说会去扫描service-base模块下的com.jean包下的所有类
@ComponentScan(basePackages = {"com.jean"})
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class,args);
    }
}
