package cn.enjoy.feign;

import cn.enjoy.mall.service.manage.IOrderManageService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-ORDER-SERVICE")
public interface IOrderManageServiceClient extends IOrderManageService {
}
