package cn.enjoy.users;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 用户以及授权启动类
 */
@SpringBootApplication
@MapperScan("cn.enjoy.**.dao")
@EnableEurekaClient
public class UserServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApp.class);
    }
}
