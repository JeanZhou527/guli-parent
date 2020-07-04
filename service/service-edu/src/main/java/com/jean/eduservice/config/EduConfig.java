package com.jean.eduservice.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jean.eduservice.mapper")  //一定要记得写，扫描mapper接口的位置
public class EduConfig {

    //加入mybatis-plus逻辑删除插件
    @Bean
    public ISqlInjector sqlInjector()
    {
        return  new LogicSqlInjector();
    }

    //加入mybatis-plus的分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor()
    {
        return  new PaginationInterceptor();
    }

}
