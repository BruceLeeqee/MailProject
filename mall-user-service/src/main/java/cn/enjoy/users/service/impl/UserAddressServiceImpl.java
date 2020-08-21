package cn.enjoy.users.service.impl;

import cn.enjoy.mall.model.UserAddress;
import cn.enjoy.mall.service.IUserAddressService;
import cn.enjoy.users.dao.UserAddressMapper;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class UserAddressServiceImpl implements IUserAddressService {
    @Resource
    private UserAddressMapper userAddressMapper;

    @Autowired
    private DefaultUidGenerator defaultUidGenerator;

    @Autowired
    private CachedUidGenerator cachedUidGenerator;

    @Override
    public void save(@RequestBody UserAddress userAddress) {
        if(userAddress.getAddressId()==null || userAddress.getAddressId() == 0){
            userAddress.setAddressId(defaultUidGenerator.getUID());
            userAddressMapper.insert(userAddress);
        }else{
            userAddressMapper.updateByPrimaryKey(userAddress);
        }
    }

    @Override
    public void remove(Integer addressId) {
        userAddressMapper.deleteByPrimaryKey(addressId);
    }

    @Override
    public List<UserAddress> selectById(Map map) {
        return userAddressMapper.selectById(map);
    }
}
