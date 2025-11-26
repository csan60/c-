package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.DiscusszuozhenyishengEntity;
import com.cl.entity.view.DiscusszuozhenyishengView;
import com.cl.service.DiscusszuozhenyishengService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 坐诊医生评论表
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@RestController
@RequestMapping("/discusszuozhenyisheng")
public class DiscusszuozhenyishengController {
    @Autowired
    private DiscusszuozhenyishengService discusszuozhenyishengService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, DiscusszuozhenyishengEntity discusszuozhenyisheng,
                  HttpServletRequest request) {
        EntityWrapper<DiscusszuozhenyishengEntity> ew = new EntityWrapper<DiscusszuozhenyishengEntity>();


        PageUtils page = discusszuozhenyishengService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusszuozhenyisheng), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, DiscusszuozhenyishengEntity discusszuozhenyisheng,
                  HttpServletRequest request) {
        EntityWrapper<DiscusszuozhenyishengEntity> ew = new EntityWrapper<DiscusszuozhenyishengEntity>();

        PageUtils page = discusszuozhenyishengService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusszuozhenyisheng), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(DiscusszuozhenyishengEntity discusszuozhenyisheng) {
        EntityWrapper<DiscusszuozhenyishengEntity> ew = new EntityWrapper<DiscusszuozhenyishengEntity>();
        ew.allEq(MPUtil.allEQMapPre(discusszuozhenyisheng, "discusszuozhenyisheng"));
        return R.ok().put("data", discusszuozhenyishengService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DiscusszuozhenyishengEntity discusszuozhenyisheng) {
        EntityWrapper<DiscusszuozhenyishengEntity> ew = new EntityWrapper<DiscusszuozhenyishengEntity>();
        ew.allEq(MPUtil.allEQMapPre(discusszuozhenyisheng, "discusszuozhenyisheng"));
        DiscusszuozhenyishengView discusszuozhenyishengView = discusszuozhenyishengService.selectView(ew);
        return R.ok("查询坐诊医生评论表成功").put("data", discusszuozhenyishengView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        DiscusszuozhenyishengEntity discusszuozhenyisheng = discusszuozhenyishengService.selectById(id);
        discusszuozhenyisheng = discusszuozhenyishengService.selectView(new EntityWrapper<DiscusszuozhenyishengEntity>().eq("id", id));
        return R.ok().put("data", discusszuozhenyisheng);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        DiscusszuozhenyishengEntity discusszuozhenyisheng = discusszuozhenyishengService.selectById(id);
        discusszuozhenyisheng = discusszuozhenyishengService.selectView(new EntityWrapper<DiscusszuozhenyishengEntity>().eq("id", id));
        return R.ok().put("data", discusszuozhenyisheng);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DiscusszuozhenyishengEntity discusszuozhenyisheng, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(discusszuozhenyisheng);
        discusszuozhenyishengService.insert(discusszuozhenyisheng);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DiscusszuozhenyishengEntity discusszuozhenyisheng, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(discusszuozhenyisheng);
        discusszuozhenyishengService.insert(discusszuozhenyisheng);
        return R.ok();
    }

    /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username) {
        DiscusszuozhenyishengEntity discusszuozhenyisheng = discusszuozhenyishengService.selectOne(new EntityWrapper<DiscusszuozhenyishengEntity>().eq("", username));
        return R.ok().put("data", discusszuozhenyisheng);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody DiscusszuozhenyishengEntity discusszuozhenyisheng, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(discusszuozhenyisheng);
        discusszuozhenyishengService.updateById(discusszuozhenyisheng);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        discusszuozhenyishengService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 前端智能排序
     */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, DiscusszuozhenyishengEntity discusszuozhenyisheng, HttpServletRequest request, String pre) {
        EntityWrapper<DiscusszuozhenyishengEntity> ew = new EntityWrapper<DiscusszuozhenyishengEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = entry.getKey();
            if (pre.endsWith(".")) {
                newMap.put(pre + newKey, entry.getValue());
            } else if (StringUtils.isEmpty(pre)) {
                newMap.put(newKey, entry.getValue());
            } else {
                newMap.put(pre + "." + newKey, entry.getValue());
            }
        }
        params.put("sort", "clicktime");
        params.put("order", "desc");
        PageUtils page = discusszuozhenyishengService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusszuozhenyisheng), params), params));
        return R.ok().put("data", page);
    }


}
