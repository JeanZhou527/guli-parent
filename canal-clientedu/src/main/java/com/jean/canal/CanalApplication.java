package com.jean.canal;

import com.jean.canal.client.CanalClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class CanalApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class, args);
    }

    @Resource
    private CanalClient canalClient;
    @Override
    public void run(String... args) throws Exception {
        // 项目启动，就执行canal客户端进行监听远程数据库的变化
        canalClient.run();
    }
}
