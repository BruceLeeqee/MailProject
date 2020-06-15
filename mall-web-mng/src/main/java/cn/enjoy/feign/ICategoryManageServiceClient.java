package cn.enjoy.feign;

import cn.enjoy.mall.service.manage.ICategoryManageService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-PRODUCT-SERVICE")
public interface ICategoryManageServiceClient extends ICategoryManageService {
}
