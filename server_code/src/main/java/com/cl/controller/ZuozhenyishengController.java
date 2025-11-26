package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.ZuozhenyishengEntity;
import com.cl.entity.view.ZuozhenyishengView;
import com.cl.service.StoreupService;
import com.cl.service.ZuozhenyishengService;
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
 * 坐诊医生
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@RestController
@RequestMapping("/zuozhenyisheng")
public class ZuozhenyishengController {
    @Autowired
    private ZuozhenyishengService zuozhenyishengService;

    @Autowired
    private StoreupService storeupService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ZuozhenyishengEntity zuozhenyisheng,
                  @RequestParam(required = false) Double renshustart,
                  @RequestParam(required = false) Double renshuend,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            zuozhenyisheng.setYishengzhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<ZuozhenyishengEntity> ew = new EntityWrapper<ZuozhenyishengEntity>();
        if (renshustart != null) ew.ge("renshu", renshustart);
        if (renshuend != null) ew.le("renshu", renshuend);


        PageUtils page = zuozhenyishengService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zuozhenyisheng), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ZuozhenyishengEntity zuozhenyisheng,
                  @RequestParam(required = false) Double renshustart,
                  @RequestParam(required = false) Double renshuend,
                  HttpServletRequest request) {
        EntityWrapper<ZuozhenyishengEntity> ew = new EntityWrapper<ZuozhenyishengEntity>();
        if (renshustart != null) ew.ge("renshu", renshustart);
        if (renshuend != null) ew.le("renshu", renshuend);

        PageUtils page = zuozhenyishengService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zuozhenyisheng), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(ZuozhenyishengEntity zuozhenyisheng) {
        EntityWrapper<ZuozhenyishengEntity> ew = new EntityWrapper<ZuozhenyishengEntity>();
        ew.allEq(MPUtil.allEQMapPre(zuozhenyisheng, "zuozhenyisheng"));
        return R.ok().put("data", zuozhenyishengService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ZuozhenyishengEntity zuozhenyisheng) {
        EntityWrapper<ZuozhenyishengEntity> ew = new EntityWrapper<ZuozhenyishengEntity>();
        ew.allEq(MPUtil.allEQMapPre(zuozhenyisheng, "zuozhenyisheng"));
        ZuozhenyishengView zuozhenyishengView = zuozhenyishengService.selectView(ew);
        return R.ok("查询坐诊医生成功").put("data", zuozhenyishengView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ZuozhenyishengEntity zuozhenyisheng = zuozhenyishengService.selectById(id);
        zuozhenyisheng = zuozhenyishengService.selectView(new EntityWrapper<ZuozhenyishengEntity>().eq("id", id));
        return R.ok().put("data", zuozhenyisheng);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ZuozhenyishengEntity zuozhenyisheng = zuozhenyishengService.selectById(id);
        zuozhenyisheng = zuozhenyishengService.selectView(new EntityWrapper<ZuozhenyishengEntity>().eq("id", id));
        return R.ok().put("data", zuozhenyisheng);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ZuozhenyishengEntity zuozhenyisheng, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(zuozhenyisheng);
        zuozhenyishengService.insert(zuozhenyisheng);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ZuozhenyishengEntity zuozhenyisheng, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(zuozhenyisheng);
        zuozhenyishengService.insert(zuozhenyisheng);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ZuozhenyishengEntity zuozhenyisheng, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(zuozhenyisheng);
        zuozhenyishengService.updateById(zuozhenyisheng);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        zuozhenyishengService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
