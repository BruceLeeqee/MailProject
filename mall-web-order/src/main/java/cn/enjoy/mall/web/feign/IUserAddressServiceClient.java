package cn.enjoy.mall.web.feign;

import cn.enjoy.mall.service.IUserAddressService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IUserAddressServiceClient extends IUserAddressService {
}
