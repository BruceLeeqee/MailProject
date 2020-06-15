package cn.enjoy.mall.service.impl.manage;

import cn.enjoy.core.utils.GridModel;
import cn.enjoy.mall.dao.GoodsTypeMapper;
import cn.enjoy.mall.model.GoodsType;
import cn.enjoy.mall.service.manage.IGoodsTypeManageService;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品模型
 * @author Ray
 * @date 2018/3/12.
 */
@RestController
public class GoodsTypeManageServiceImpl implements IGoodsTypeManageService {

    @Resource
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public GridModel<GoodsType> queryByPage(int page, int pageSize, String parentId, String name) {
        PageBounds pageBounds = new PageBounds(page, pageSize);

        PageList<GoodsType> goodsTypes = goodsTypeMapper.queryByPage(name, pageBounds);

        if(goodsTypes.getPaginator() == null) {
            Integer totalCount = goodsTypeMapper.queryByPageTotalCount(name);
            Paginator paginator = new Paginator(page,pageSize,totalCount);
            PageList<GoodsType> goodsTypes2 = new PageList<>(goodsTypes, paginator);
            GridModel<GoodsType> goodsTypesModel = new GridModel<>(goodsTypes2);
            return goodsTypesModel;
        }

        return new GridModel<>(goodsTypes);
    }

    @Override
    public List<GoodsType> queryAll() {
        return goodsTypeMapper.queryAll();
    }

    @Override
    public void save(GoodsType goodsType) {
        if(goodsType.getId() == null){
            goodsTypeMapper.insert(goodsType);
        } else {
            goodsTypeMapper.updateByPrimaryKeySelective(goodsType);
        }
    }

    @Override
    public void delete(short id) {
        goodsTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByIds(String[] ids) {
        for(String id : ids){
            goodsTypeMapper.deleteByPrimaryKey(Short.parseShort(id));
        }
    }
}
