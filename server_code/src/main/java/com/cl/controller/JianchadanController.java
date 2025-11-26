package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.JianchadanEntity;
import com.cl.entity.view.JianchadanView;
import com.cl.service.JianchadanService;
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
 * 检查单
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@RestController
@RequestMapping("/jianchadan")
public class JianchadanController {
    @Autowired
    private JianchadanService jianchadanService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, JianchadanEntity jianchadan,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            jianchadan.setYishengzhanghao((String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            jianchadan.setHuanzhezhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<JianchadanEntity> ew = new EntityWrapper<JianchadanEntity>();


        PageUtils page = jianchadanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jianchadan), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, JianchadanEntity jianchadan,
                  HttpServletRequest request) {
        EntityWrapper<JianchadanEntity> ew = new EntityWrapper<JianchadanEntity>();

        PageUtils page = jianchadanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jianchadan), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(JianchadanEntity jianchadan) {
        EntityWrapper<JianchadanEntity> ew = new EntityWrapper<JianchadanEntity>();
        ew.allEq(MPUtil.allEQMapPre(jianchadan, "jianchadan"));
        return R.ok().put("data", jianchadanService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JianchadanEntity jianchadan) {
        EntityWrapper<JianchadanEntity> ew = new EntityWrapper<JianchadanEntity>();
        ew.allEq(MPUtil.allEQMapPre(jianchadan, "jianchadan"));
        JianchadanView jianchadanView = jianchadanService.selectView(ew);
        return R.ok("查询检查单成功").put("data", jianchadanView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        JianchadanEntity jianchadan = jianchadanService.selectById(id);
        jianchadan = jianchadanService.selectView(new EntityWrapper<JianchadanEntity>().eq("id", id));
        return R.ok().put("data", jianchadan);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        JianchadanEntity jianchadan = jianchadanService.selectById(id);
        jianchadan = jianchadanService.selectView(new EntityWrapper<JianchadanEntity>().eq("id", id));
        return R.ok().put("data", jianchadan);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody JianchadanEntity jianchadan, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(jianchadan);
        jianchadanService.insert(jianchadan);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody JianchadanEntity jianchadan, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(jianchadan);
        jianchadanService.insert(jianchadan);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody JianchadanEntity jianchadan, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(jianchadan);
        jianchadanService.updateById(jianchadan);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        jianchadanService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


}
