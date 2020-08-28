package cn.enjoy;

import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @author Ray
 * @date 2018/2/1.
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("cn.enjoy.**.dao")
@EnableEurekaClient
public class ProductApp {

    public static void main(String[] args) {
         SpringApplication.run(ProductApp.class, args);
    }

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }
}
