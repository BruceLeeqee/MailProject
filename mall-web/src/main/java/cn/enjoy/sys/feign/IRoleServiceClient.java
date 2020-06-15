package cn.enjoy.sys.feign;

import cn.enjoy.sys.service.IRoleService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-USER-SERVICE")
public interface IRoleServiceClient extends IRoleService {
}
