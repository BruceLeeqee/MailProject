package cn.enjoy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Ray
 * @date 2018/2/1.
 */
@SpringBootApplication
@MapperScan("cn.enjoy.**.dao")
@EnableEurekaClient
public class ProductApp {

    public static void main(String[] args) {
         SpringApplication.run(ProductApp.class, args);
    }
}
