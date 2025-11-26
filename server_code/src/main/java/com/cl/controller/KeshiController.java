package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.KeshiEntity;
import com.cl.entity.view.KeshiView;
import com.cl.service.KeshiService;
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
 * 科室
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@RestController
@RequestMapping("/keshi")
public class KeshiController {
    @Autowired
    private KeshiService keshiService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, KeshiEntity keshi,
                  HttpServletRequest request) {
        EntityWrapper<KeshiEntity> ew = new EntityWrapper<KeshiEntity>();


        PageUtils page = keshiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, keshi), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, KeshiEntity keshi,
                  HttpServletRequest request) {
        EntityWrapper<KeshiEntity> ew = new EntityWrapper<KeshiEntity>();

        PageUtils page = keshiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, keshi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(KeshiEntity keshi) {
        EntityWrapper<KeshiEntity> ew = new EntityWrapper<KeshiEntity>();
        ew.allEq(MPUtil.allEQMapPre(keshi, "keshi"));
        return R.ok().put("data", keshiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(KeshiEntity keshi) {
        EntityWrapper<KeshiEntity> ew = new EntityWrapper<KeshiEntity>();
        ew.allEq(MPUtil.allEQMapPre(keshi, "keshi"));
        KeshiView keshiView = keshiService.selectView(ew);
        return R.ok("查询科室成功").put("data", keshiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        KeshiEntity keshi = keshiService.selectById(id);
        keshi = keshiService.selectView(new EntityWrapper<KeshiEntity>().eq("id", id));
        return R.ok().put("data", keshi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        KeshiEntity keshi = keshiService.selectById(id);
        keshi = keshiService.selectView(new EntityWrapper<KeshiEntity>().eq("id", id));
        return R.ok().put("data", keshi);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody KeshiEntity keshi, HttpServletRequest request) {
        if (keshiService.selectCount(new EntityWrapper<KeshiEntity>().eq("keshi", keshi.getKeshi())) > 0) {
            return R.error("科室已存在");
        }
        //ValidatorUtils.validateEntity(keshi);
        keshiService.insert(keshi);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody KeshiEntity keshi, HttpServletRequest request) {
        if (keshiService.selectCount(new EntityWrapper<KeshiEntity>().eq("keshi", keshi.getKeshi())) > 0) {
            return R.error("科室已存在");
        }
        //ValidatorUtils.validateEntity(keshi);
        keshiService.insert(keshi);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody KeshiEntity keshi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(keshi);
        keshiService.updateById(keshi);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        keshiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
