/**
 * Created on 2015年9月2日 by Caiming
 */
package cn.enjoy.users.service.impl;


import cn.enjoy.core.exception.BusinessException;
import cn.enjoy.core.utils.GridModel;
import cn.enjoy.core.utils.UUIDGenerator;
import cn.enjoy.sys.model.SelectModel;
import cn.enjoy.sys.model.SysDictData;
import cn.enjoy.sys.model.SysDictType;
import cn.enjoy.sys.service.IDictDataService;
import cn.enjoy.users.dao.SysDictDataMapper;
import cn.enjoy.users.dao.SysDictTypeMapper;
import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company:享学信息科技有限公司 Co., Ltd.</p>
 * @author Caiming
 * @version 1.0
 * 修改记录：
 * 修改序号，修改日期，修改人，修改内容
 */
@RestController
//@Path("/sys/dictData")
public class DictDataServiceImpl implements IDictDataService {
	
	@Autowired
	private SysDictDataMapper dictDataMapper;
	
	@Autowired
	private SysDictTypeMapper dictTypeMapper;

	@Override
//	@Path("findDictByType")
//	@GET
//	@Produces(value = {MediaType.APPLICATION_JSON})
	public List<SysDictData> findDictByType(/*@QueryParam("type") */String type) {
		return dictDataMapper.queryDictDataByType(type);
	}

	@Override
	public List<SysDictData> queryDictDataByCode(String code) {
		return dictDataMapper.queryDictDataByCode(code);
	}

	@Override
	public List<SysDictData> findAllDict() {
		return dictDataMapper.queryAllDictData();
	}
	
	@Override
	public List<SysDictType> findAllDictType(){
		return dictTypeMapper.queryAllDictType();
	}
	
	@Override
	public List<SelectModel> findSelectModelsByType(String type){
		return dictDataMapper.querySelectModelByType(type);
	}

	@Override
	public List<SysDictData> queryDictDataByTypeAndORDER(Map param) {
		return dictDataMapper.queryDictDataByTypeAndORDER(param);
	}


	@Override
//	@Path("queryAllDictDataPage")
	public GridModel<SysDictData> queryAllDictDataPage(String param, String type, Integer pageNo, Integer pageSize, String sidx, String sord) {
		if(null==type) {
			throw new BusinessException("操作失败，type为空");
		}
		String orderString = "";
		if (!StringUtils.isEmpty(sidx)) {
			orderString = sidx + "." + sord;
		}
		PageBounds pageBounds = new PageBounds(pageNo, pageSize, Order.formString(orderString));
		PageList<SysDictData> pageList =(PageList<SysDictData>)dictDataMapper.queryAllDictDataPage(param,type,pageBounds);
		return new GridModel<SysDictData>(pageList);
	}

	@Override
	public void deleteByIds(List<String> ids) {
		dictDataMapper.deleteByIds(ids);
	}

	@Override
	@Transactional
	public void addDictData(SysDictData sysDictData){
		sysDictData.setId(UUIDGenerator.getUUID());
		dictDataMapper.insert(sysDictData);
	}

	@Override
	public SysDictData selectByPrimaryKey(String id){
		return dictDataMapper.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void deleteDictData(String id) {
		dictDataMapper.deleteByPrimaryKey(id);

	}

	@Override
	@Transactional
	public void freezeDictData(String id,String status) {
		SysDictData da = dictDataMapper.selectByPrimaryKey(id);
		da.setValid(Integer.parseInt(status));
		dictDataMapper.updateByPrimaryKey(da);

	}

	@Override
	@Transactional
	public void updateDictData(SysDictData sysDictData) {
		dictDataMapper.updateByPrimaryKey(sysDictData);

	}



}

