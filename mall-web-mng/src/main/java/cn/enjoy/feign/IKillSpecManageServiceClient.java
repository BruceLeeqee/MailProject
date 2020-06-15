package cn.enjoy.feign;

import cn.enjoy.mall.service.manage.IKillSpecManageService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "MALL-PRODUCT-SERVICE")
public interface IKillSpecManageServiceClient extends IKillSpecManageService {
}
