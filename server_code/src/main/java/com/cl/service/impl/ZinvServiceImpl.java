package com.cl.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;

import com.cl.dao.ZinvDao;
import com.cl.dao.HuanzheDao;
import com.cl.entity.ZinvEntity;
import com.cl.entity.HuanzheEntity;
import com.cl.service.ZinvService;
import com.cl.entity.view.ZinvView;


@Service("zinvService")
public class ZinvServiceImpl extends ServiceImpl<ZinvDao, ZinvEntity> implements ZinvService {

    @Autowired
    private HuanzheDao huanzheDao;

    /**
     * 设置关联患者
     * @param zinvId 子女用户ID
     * @param huanzheId 患者ID
     * @return 是否成功
     */
    public boolean setRelatedPatient(Long zinvId, Long huanzheId) {
        // 验证患者是否存在
        HuanzheEntity huanzhe = huanzheDao.selectById(huanzheId);
        if (huanzhe == null) {
            return false;
        }
        
        // 更新子女用户的关联患者ID
        ZinvEntity zinv = this.selectById(zinvId);
        if (zinv != null) {
            zinv.setGuanlianHuanzheId(huanzheId);
            return this.updateById(zinv);
        }
        return false;
    }

    /**
     * 获取关联患者信息
     * @param zinvId 子女用户ID
     * @return 关联的患者ID
     */
    public Long getRelatedPatient(Long zinvId) {
        ZinvEntity zinv = this.selectById(zinvId);
        return zinv != null ? zinv.getGuanlianHuanzheId() : null;
    }

    /**
     * 移除关联患者
     * @param zinvId 子女用户ID
     * @return 是否成功
     */
    public boolean removeRelatedPatient(Long zinvId) {
        ZinvEntity zinv = this.selectById(zinvId);
        if (zinv != null) {
            zinv.setGuanlianHuanzheId(null);
            return this.updateById(zinv);
        }
        return false;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ZinvEntity> page = this.selectPage(
                new Query<ZinvEntity>(params).getPage(),
                new EntityWrapper<ZinvEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<ZinvEntity> wrapper) {
		  Page<ZinvView> page =new Query<ZinvView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<ZinvView> selectListView(Wrapper<ZinvEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ZinvView selectView(Wrapper<ZinvEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}