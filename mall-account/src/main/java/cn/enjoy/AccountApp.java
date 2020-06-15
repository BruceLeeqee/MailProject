package cn.enjoy;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @author Ray
 * @date 2018/2/1.
 */
@SpringBootApplication
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"cn.enjoy.sys.feign"})
public class AccountApp {

    public static void main(String[] args) {
         SpringApplication.run(AccountApp.class, args);
    }
}
