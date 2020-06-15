package cn.enjoy.sys.feign;

import cn.enjoy.sys.service.IDepartmentService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-USER-SERVICE")
public interface IDepartmentServiceClient extends IDepartmentService {
}
