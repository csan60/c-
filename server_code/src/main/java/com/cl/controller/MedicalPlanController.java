package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.MedicalPlan;
import com.cl.entity.HuanzheEntity;
import com.cl.service.MedicalPlanService;
import com.cl.service.HuanzheService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 医嘱计划
 *
 * @author
 * @email
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/medicalplan")
public class MedicalPlanController {
    @Autowired
    private MedicalPlanService medicalPlanService;
    
    @Autowired
    private HuanzheService huanzheService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, MedicalPlan medicalPlan,
                  HttpServletRequest request) {
        EntityWrapper<MedicalPlan> ew = new EntityWrapper<MedicalPlan>();
        PageUtils page = medicalPlanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, medicalPlan), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, MedicalPlan medicalPlan,
                  HttpServletRequest request) {
        EntityWrapper<MedicalPlan> ew = new EntityWrapper<MedicalPlan>();
        PageUtils page = medicalPlanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, medicalPlan), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(MedicalPlan medicalPlan) {
        EntityWrapper<MedicalPlan> ew = new EntityWrapper<MedicalPlan>();
        ew.allEq(MPUtil.allEQMapPre(medicalPlan, "medicalplan"));
        return R.ok().put("data", medicalPlanService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(MedicalPlan medicalPlan) {
        EntityWrapper<MedicalPlan> ew = new EntityWrapper<MedicalPlan>();
        ew.allEq(MPUtil.allEQMapPre(medicalPlan, "medicalplan"));
        MedicalPlan medicalPlanView = medicalPlanService.selectView(ew);
        return R.ok("查询医嘱计划成功").put("data", medicalPlanView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MedicalPlan medicalPlan = medicalPlanService.selectById(id);
        return R.ok().put("data", medicalPlan);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        MedicalPlan medicalPlan = medicalPlanService.selectById(id);
        return R.ok().put("data", medicalPlan);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MedicalPlan medicalPlan, HttpServletRequest request) {
        medicalPlan.setCreatedAt(LocalDateTime.now());
        medicalPlan.setUpdatedAt(LocalDateTime.now());
        medicalPlanService.insert(medicalPlan);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody MedicalPlan medicalPlan, HttpServletRequest request) {
          if (medicalPlan.getOlderUserId() == null) {
             return R.error("患者ID不能为空");
         }
    
    // 验证患者是否存在
        HuanzheEntity huanzhe = huanzheService.selectById(medicalPlan.getOlderUserId());
        if (huanzhe == null) {
            return R.error("患者不存在");
        }
    
            medicalPlan.setCreatedAt(LocalDateTime.now());
            medicalPlan.setUpdatedAt(LocalDateTime.now());
            medicalPlanService.insert(medicalPlan);
            return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody MedicalPlan medicalPlan, HttpServletRequest request) {
        medicalPlan.setUpdatedAt(LocalDateTime.now());
        medicalPlanService.updateById(medicalPlan);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        medicalPlanService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 根据OCR识别结果创建医嘱计划
     */
    @RequestMapping("/createFromOcr")
    public R createFromOcr(@RequestParam Long olderUserId, @RequestParam String ocrResult, HttpServletRequest request) {
        try {
            // 验证用户是否存在
            HuanzheEntity huanzhe = huanzheService.selectById(olderUserId);
            if (huanzhe == null) {
                return R.error("用户不存在");
            }
            
            List<MedicalPlan> plans = medicalPlanService.createMedicalPlansFromOcr(olderUserId, ocrResult);
            return R.ok("医嘱计划创建成功").put("data", plans).put("count", plans.size());
        } catch (Exception e) {
            return R.error("创建医嘱计划失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户的有效医嘱计划
     */
    @RequestMapping("/getActivePlans/{olderUserId}")
    public R getActivePlans(@PathVariable("olderUserId") Long olderUserId) {
        List<MedicalPlan> plans = medicalPlanService.getActivePlans(olderUserId);
        return R.ok().put("data", plans);
    }

    /**
     * 解析OCR文本（测试接口）
     */
    @RequestMapping("/parseOcr")
    public R parseOcr(@RequestParam String ocrText) {
        List<Map<String, String>> result = medicalPlanService.parseOcrText(ocrText);
        return R.ok().put("data", result);
    }
}