package cn.enjoy.mall.service.impl.manage;

import cn.enjoy.core.utils.GridModel;
import cn.enjoy.mall.constant.KillConstants;
import cn.enjoy.mall.dao.KillGoodsPriceMapper;
import cn.enjoy.mall.model.KillGoodsPrice;
import cn.enjoy.mall.service.manage.IKillSpecManageService;
import cn.enjoy.mall.vo.KillGoodsSpecPriceDetailVo;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@RestController
public class KillGoodsManageServiceImpl implements IKillSpecManageService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private KillGoodsPriceMapper killGoodsPriceMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int delete(Integer id) {
        //清缓存
        stringRedisTemplate.delete(KillConstants.KILLGOODS_LIST);
        stringRedisTemplate.delete(KillConstants.KILL_GOOD_COUNT+id);
        return killGoodsPriceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int save(KillGoodsPrice record) {
        int ret = killGoodsPriceMapper.insert(record);
        if (ret > 0){//当前秒杀配置成功，配置秒杀虚拟库存
            final String killGoodCount = KillConstants.KILL_GOOD_COUNT+record.getId();

            //清缓存
            stringRedisTemplate.delete(KillConstants.KILLGOODS_LIST);
            //失效时间
            long expireTime = record.getEndTime().getTime() - System.currentTimeMillis();
            if (expireTime > 0){
                stringRedisTemplate.opsForValue().set(killGoodCount,record.getKillCount().toString(),expireTime, TimeUnit.MILLISECONDS);
            } else {
                stringRedisTemplate.delete(killGoodCount);
            }
        }
        return ret;
    }

    @Override
    public int selectCountBySpecGoodId(Integer specGoodsId) {
        return killGoodsPriceMapper.selectCountBySpecGoodId(specGoodsId);
    }

    @Override
    public KillGoodsPrice selectByPrimaryKey(Integer id) {
        return killGoodsPriceMapper.selectByPrimaryKey(id);
    }

    @Override
    public KillGoodsSpecPriceDetailVo detailBySpecGoodId(Integer specGoodsId) {
        return killGoodsPriceMapper.detailBySpecGoodId(specGoodsId);
    }

    public KillGoodsSpecPriceDetailVo detailById(Integer id) {
        return killGoodsPriceMapper.detail(id);
    }

    @Override
    public int update(KillGoodsPrice record) {
        int ret =  killGoodsPriceMapper.updateByPrimaryKey(record);
        if (ret > 0){//当前秒杀配置成功，配置秒杀虚拟库存
            flushCache(record);
        }
        return ret;
    }

    @Override
    public int updateSecKill(KillGoodsPrice record) {
        int i = killGoodsPriceMapper.updateSecKill(record);
        return i;
    }

    @Override
    public int updateBySpecGoodsId(KillGoodsPrice record) {
        String sql = "update tp_kill_goods_price set kill_count = kill_count-? where spec_goods_id = ? and kill_count > 0";
        int i = jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, record.getKillCount());
                ps.setInt(2, record.getSpecGoodsId());
            }
        });
//        int i = killGoodsPriceMapper.updateSecKill(record);
        return i;
    }

    public void flushCache(KillGoodsPrice record) {
        final String killGoodCount = KillConstants.KILL_GOOD_COUNT + record.getId();

        //清缓存
        stringRedisTemplate.delete(KillConstants.KILLGOODS_LIST);
        //失效时间
        long expireTime = record.getEndTime().getTime() - System.currentTimeMillis();
        if (expireTime > 0) {
            stringRedisTemplate.opsForValue().set(killGoodCount, record.getKillCount().toString(), expireTime, TimeUnit.MILLISECONDS);
        } else {
            stringRedisTemplate.delete(killGoodCount);
        }
    }

    @Override
    public GridModel<KillGoodsSpecPriceDetailVo> queryByPage(String name, int page, int pageSize) {
        PageBounds pageBounds = new PageBounds(page, pageSize);

        PageList<KillGoodsSpecPriceDetailVo> list = killGoodsPriceMapper.select(name, pageBounds);

        if(list.getPaginator() == null) {
            Integer totalCount = killGoodsPriceMapper.selectTotalCount(name);
            Paginator paginator = new Paginator(page,pageSize,totalCount);
            list = new PageList<>(list, paginator);
            GridModel<KillGoodsSpecPriceDetailVo> list2Model = new GridModel<>(list);
            return list2Model;
        }

        return new GridModel<>(list);
    }

    public GridModel<KillGoodsSpecPriceDetailVo> queryView(int page, int pageSize) {
        PageBounds pageBounds = new PageBounds(page, pageSize);

        PageList<KillGoodsSpecPriceDetailVo> list = killGoodsPriceMapper.selectView(pageBounds);

        if(list.getPaginator() == null) {
            Integer totalCount = killGoodsPriceMapper.selectViewTotalCount();
            Paginator paginator = new Paginator(page,pageSize,totalCount);
            list = new PageList<>(list, paginator);
            GridModel<KillGoodsSpecPriceDetailVo> list2Model = new GridModel<>(list);
            return list2Model;
        }

        return new GridModel<>(list);
    }
}
