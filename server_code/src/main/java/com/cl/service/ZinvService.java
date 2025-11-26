package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.ZinvEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.ZinvView;


/**
 * 子女用户
 *
 * @author 
 * @email 
 * @date 2024-01-01 10:00:00
 */
public interface ZinvService extends IService<ZinvEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ZinvView> selectListView(Wrapper<ZinvEntity> wrapper);
   	
   	ZinvView selectView(@Param("ew") Wrapper<ZinvEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<ZinvEntity> wrapper);
   	
    /**
     * 设置关联患者
     * @param zinvId 子女用户ID
     * @param huanzheId 患者ID
     * @return 是否成功
     */
    boolean setRelatedPatient(Long zinvId, Long huanzheId);

    /**
     * 获取关联患者信息
     * @param zinvId 子女用户ID
     * @return 关联的患者ID
     */
    Long getRelatedPatient(Long zinvId);

    /**
     * 移除关联患者
     * @param zinvId 子女用户ID
     * @return 是否成功
     */
    boolean removeRelatedPatient(Long zinvId);

}