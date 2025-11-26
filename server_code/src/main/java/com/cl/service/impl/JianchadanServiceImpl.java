package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.JianchadanDao;
import com.cl.entity.JianchadanEntity;
import com.cl.entity.view.JianchadanView;
import com.cl.service.JianchadanService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("jianchadanService")
public class JianchadanServiceImpl extends ServiceImpl<JianchadanDao, JianchadanEntity> implements JianchadanService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JianchadanEntity> page = this.selectPage(
                new Query<JianchadanEntity>(params).getPage(),
                new EntityWrapper<JianchadanEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<JianchadanEntity> wrapper) {
        Page<JianchadanView> page = new Query<JianchadanView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<JianchadanView> selectListView(Wrapper<JianchadanEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public JianchadanView selectView(Wrapper<JianchadanEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
