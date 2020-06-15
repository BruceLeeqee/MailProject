package cn.enjoy.mall.service.impl.manage;

import cn.enjoy.mall.dao.BrandMapper;
import cn.enjoy.mall.model.Brand;
import cn.enjoy.mall.service.manage.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> getAll() {
        return brandMapper.selectAll();
    }
}
