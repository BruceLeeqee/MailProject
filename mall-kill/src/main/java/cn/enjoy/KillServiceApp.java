package cn.enjoy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Classname KillServiceApp
 * @Description 专门做秒杀业务的服务，跟其他服务做隔离
 * @Author Jack
 * Date 2020/8/12 13:58
 * Version 1.0
 */
@SpringBootApplication
@MapperScan("cn.enjoy.kill.dao")
@EnableScheduling
@EnableEurekaClient
@EnableCircuitBreaker
public class KillServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(KillServiceApp.class, args);
    }
}
