package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.BinglixinxiEntity;
import com.cl.entity.view.BinglixinxiView;
import com.cl.service.BinglixinxiService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * 病历信息
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@RestController
@RequestMapping("/binglixinxi")
public class BinglixinxiController {
    @Autowired
    private BinglixinxiService binglixinxiService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, BinglixinxiEntity binglixinxi,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("huanzhe")) {
            binglixinxi.setHuanzhezhanghao((String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("yisheng")) {
            binglixinxi.setYishengzhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<BinglixinxiEntity> ew = new EntityWrapper<BinglixinxiEntity>();


        PageUtils page = binglixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, binglixinxi), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, BinglixinxiEntity binglixinxi,
                  HttpServletRequest request) {
        EntityWrapper<BinglixinxiEntity> ew = new EntityWrapper<BinglixinxiEntity>();

        PageUtils page = binglixinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, binglixinxi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(BinglixinxiEntity binglixinxi) {
        EntityWrapper<BinglixinxiEntity> ew = new EntityWrapper<BinglixinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(binglixinxi, "binglixinxi"));
        return R.ok().put("data", binglixinxiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(BinglixinxiEntity binglixinxi) {
        EntityWrapper<BinglixinxiEntity> ew = new EntityWrapper<BinglixinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(binglixinxi, "binglixinxi"));
        BinglixinxiView binglixinxiView = binglixinxiService.selectView(ew);
        return R.ok("查询病历信息成功").put("data", binglixinxiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        BinglixinxiEntity binglixinxi = binglixinxiService.selectById(id);
        binglixinxi = binglixinxiService.selectView(new EntityWrapper<BinglixinxiEntity>().eq("id", id));
        return R.ok().put("data", binglixinxi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        BinglixinxiEntity binglixinxi = binglixinxiService.selectById(id);
        binglixinxi = binglixinxiService.selectView(new EntityWrapper<BinglixinxiEntity>().eq("id", id));
        return R.ok().put("data", binglixinxi);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BinglixinxiEntity binglixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(binglixinxi);
        binglixinxiService.insert(binglixinxi);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody BinglixinxiEntity binglixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(binglixinxi);
        binglixinxiService.insert(binglixinxi);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody BinglixinxiEntity binglixinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(binglixinxi);
        binglixinxiService.updateById(binglixinxi);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        binglixinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
