package cn.enjoy.users.service.impl;

import cn.enjoy.sys.model.SysUserRole;
import cn.enjoy.sys.service.IUserRoleService;
import cn.enjoy.users.dao.SysUserRoleMapper;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserRoleServiceImpl implements IUserRoleService {

	@Resource
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Override
	public List<SysUserRole> queryUserRoleByUserId(String userId) {
		
		return sysUserRoleMapper.queryUserRoleByUserId(userId);
	}



}
