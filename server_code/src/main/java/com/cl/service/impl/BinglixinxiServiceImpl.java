package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.BinglixinxiDao;
import com.cl.entity.BinglixinxiEntity;
import com.cl.entity.view.BinglixinxiView;
import com.cl.service.BinglixinxiService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("binglixinxiService")
public class BinglixinxiServiceImpl extends ServiceImpl<BinglixinxiDao, BinglixinxiEntity> implements BinglixinxiService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<BinglixinxiEntity> page = this.selectPage(
                new Query<BinglixinxiEntity>(params).getPage(),
                new EntityWrapper<BinglixinxiEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<BinglixinxiEntity> wrapper) {
        Page<BinglixinxiView> page = new Query<BinglixinxiView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<BinglixinxiView> selectListView(Wrapper<BinglixinxiEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public BinglixinxiView selectView(Wrapper<BinglixinxiEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
