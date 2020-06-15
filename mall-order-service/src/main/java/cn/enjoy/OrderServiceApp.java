package cn.enjoy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Ray
 * @date 2018/2/1.
 */
@SpringBootApplication
@MapperScan("cn.enjoy.mall.dao")
@EnableScheduling
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"cn.enjoy.mall.feign"})
public class OrderServiceApp {

    public static void main(String[] args) {

         SpringApplication.run(OrderServiceApp.class, args);
    }
}
