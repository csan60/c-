package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.DiscusszuozhenyishengDao;
import com.cl.entity.DiscusszuozhenyishengEntity;
import com.cl.entity.view.DiscusszuozhenyishengView;
import com.cl.service.DiscusszuozhenyishengService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("discusszuozhenyishengService")
public class DiscusszuozhenyishengServiceImpl extends ServiceImpl<DiscusszuozhenyishengDao, DiscusszuozhenyishengEntity> implements DiscusszuozhenyishengService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscusszuozhenyishengEntity> page = this.selectPage(
                new Query<DiscusszuozhenyishengEntity>(params).getPage(),
                new EntityWrapper<DiscusszuozhenyishengEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscusszuozhenyishengEntity> wrapper) {
        Page<DiscusszuozhenyishengView> page = new Query<DiscusszuozhenyishengView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<DiscusszuozhenyishengView> selectListView(Wrapper<DiscusszuozhenyishengEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public DiscusszuozhenyishengView selectView(Wrapper<DiscusszuozhenyishengEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
