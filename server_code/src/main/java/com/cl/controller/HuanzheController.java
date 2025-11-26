package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.HuanzheEntity;

import com.cl.entity.view.HuanzheView;
import com.cl.service.HuanzheService;
import com.cl.service.TokenService;
import com.cl.service.ZinvService;
import com.cl.entity.ZinvEntity;
import com.cl.utils.CommonUtil;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 患者
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@RestController
@RequestMapping("/huanzhe")
public class HuanzheController {
    @Autowired
    private HuanzheService huanzheService;
  
    @Autowired
    private ZinvService zinvService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        HuanzheEntity u = huanzheService.selectOne(new EntityWrapper<HuanzheEntity>().eq("huanzhezhanghao", username));
        if (u == null || !u.getMima().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(u.getId(), username, "huanzhe", "患者");
        // 在后端控制台输出生成的token
        System.out.println("=== 患者登录成功 ===");
        System.out.println("患者账号: " + username);
        System.out.println("生成的Token: " + token);
        System.out.println("用户ID: " + u.getId());
        System.out.println("角色: 患者");
        System.out.println("=================");
        return R.ok().put("token", token);


    }


    /**
     * 注册
     */
    @IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody HuanzheEntity huanzhe) {
        //ValidatorUtils.validateEntity(huanzhe);
        HuanzheEntity u = huanzheService.selectOne(new EntityWrapper<HuanzheEntity>().eq("huanzhezhanghao", huanzhe.getHuanzhezhanghao()));
        if (u != null) {
            return R.error("注册用户已存在");
        }
        // 检查手机号是否已被注册
        u = huanzheService.selectOne(new EntityWrapper<HuanzheEntity>().eq("mobile", huanzhe.getMobile()));
        if (u != null) {
            return R.error("手机已被注册");
        }
        Long uId = new Date().getTime();
        huanzhe.setId(uId);
        huanzheService.insert(huanzhe);
        return R.ok();
    }



    /**
     * 退出
     */
    @RequestMapping("/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }

    /**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("userId");
        return R.ok().put("data", huanzheService.selectView(new EntityWrapper<HuanzheEntity>().eq("id", id)));
    }

    /**
     * 密码重置
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        HuanzheEntity u = huanzheService.selectOne(new EntityWrapper<HuanzheEntity>().eq("huanzhezhanghao", username));
        if (u == null) {
            return R.error("账号不存在");
        }
        u.setMima("123456");
        huanzheService.updateById(u);
        return R.ok("密码已重置为：123456");
    }


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HuanzheEntity huanzhe,
                  HttpServletRequest request) {
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();


        PageUtils page = huanzheService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huanzhe), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HuanzheEntity huanzhe,
                  HttpServletRequest request) {
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();

        PageUtils page = huanzheService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huanzhe), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(HuanzheEntity huanzhe) {
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();
        ew.allEq(MPUtil.allEQMapPre(huanzhe, "huanzhe"));
        return R.ok().put("data", huanzheService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(HuanzheEntity huanzhe) {
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();
        ew.allEq(MPUtil.allEQMapPre(huanzhe, "huanzhe"));
        HuanzheView huanzheView = huanzheService.selectView(ew);
        return R.ok("查询患者成功").put("data", huanzheView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        HuanzheEntity huanzhe = huanzheService.selectById(id);
        huanzhe = huanzheService.selectView(new EntityWrapper<HuanzheEntity>().eq("id", id));
        return R.ok().put("data", huanzhe);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        HuanzheEntity huanzhe = huanzheService.selectById(id);
        huanzhe = huanzheService.selectView(new EntityWrapper<HuanzheEntity>().eq("id", id));
        return R.ok().put("data", huanzhe);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody HuanzheEntity huanzhe, HttpServletRequest request) {
        if (huanzheService.selectCount(new EntityWrapper<HuanzheEntity>().eq("huanzhezhanghao", huanzhe.getHuanzhezhanghao())) > 0) {
            return R.error("患者账号已存在");
        }
        if (huanzheService.selectCount(new EntityWrapper<HuanzheEntity>().eq("mobile", huanzhe.getMobile())) > 0) {
            return R.error("手机号已存在");
        }
        huanzhe.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(huanzhe);
        HuanzheEntity u = huanzheService.selectOne(new EntityWrapper<HuanzheEntity>().eq("huanzhezhanghao", huanzhe.getHuanzhezhanghao()));
        if (u != null) {
            return R.error("用户已存在");
        }
        huanzhe.setId(new Date().getTime());
        huanzheService.insert(huanzhe);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody HuanzheEntity huanzhe, HttpServletRequest request) {
        if (huanzheService.selectCount(new EntityWrapper<HuanzheEntity>().eq("huanzhezhanghao", huanzhe.getHuanzhezhanghao())) > 0) {
            return R.error("患者账号已存在");
        }
        if (huanzheService.selectCount(new EntityWrapper<HuanzheEntity>().eq("mobile", huanzhe.getMobile())) > 0) {
            return R.error("手机号已存在");
        }
        huanzhe.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(huanzhe);
        HuanzheEntity u = huanzheService.selectOne(new EntityWrapper<HuanzheEntity>().eq("huanzhezhanghao", huanzhe.getHuanzhezhanghao()));
        if (u != null) {
            return R.error("用户已存在");
        }
        huanzhe.setId(new Date().getTime());
        huanzheService.insert(huanzhe);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody HuanzheEntity huanzhe, HttpServletRequest request) {
        // 添加调试日志
        System.out.println("=== 患者信息更新 ===");
        System.out.println("患者ID: " + huanzhe.getId());
        System.out.println("患者姓名: " + huanzhe.getHuanzhexingming());
        System.out.println("性别: " + huanzhe.getXingbie());
        System.out.println("手机号码: " + huanzhe.getShoujihaoma());
        System.out.println("年龄: " + huanzhe.getNianling());
        System.out.println("手机号: " + huanzhe.getMobile());
        System.out.println("==================");
        
        //ValidatorUtils.validateEntity(huanzhe);
        boolean result = huanzheService.updateById(huanzhe);//全部更新
        
        System.out.println("更新结果: " + (result ? "成功" : "失败"));
        
        if (result) {
            return R.ok("更新成功");
        } else {
            return R.error("更新失败");
        }
    }

    /**
     * 获取关联的子女列表
     */
    @RequestMapping("/getRelatedChildren")
    public R getRelatedChildren(HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("userId");
        if(userId == null) {
            return R.error("用户未登录");
        }
        
        // 查询关联当前患者的所有子女
        EntityWrapper<ZinvEntity> ew = new EntityWrapper<ZinvEntity>();
        ew.eq("guanlian_huanzhe_id", userId);
        List<ZinvEntity> children = zinvService.selectList(ew);
        
        if(children == null || children.isEmpty()) {
            return R.error("未找到关联的子女");
        }
        
        return R.ok().put("data", children);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        huanzheService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(huanzheService.selectValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> m : result) {
            for (String k : m.keySet()) {
                if (m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date) m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName, @RequestParam String yColumnNameMul, HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String, Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();
        for (int i = 0; i < yColumnNames.length; i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(huanzheService.selectValue(params, ew));
            for (Map<String, Object> m : result) {
                for (String k : m.keySet()) {
                    if (m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date) m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", MPUtil.camelToSnake(xColumnName));
        params.put("yColumn", MPUtil.camelToSnake(yColumnName));
        params.put("timeStatType", timeStatType);
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(huanzheService.selectTimeStatValue(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> m : result) {
            for (String k : m.keySet()) {
                if (m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date) m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, @RequestParam String yColumnNameMul, HttpServletRequest request) {
        String[] yColumnNames = MPUtil.camelToSnake(yColumnNameMul).split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String, Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();
        for (int i = 0; i < yColumnNames.length; i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(huanzheService.selectTimeStatValue(params, ew));
            for (Map<String, Object> m : result) {
                for (String k : m.keySet()) {
                    if (m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date) m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", MPUtil.camelToSnake(columnName));
        EntityWrapper<HuanzheEntity> ew = new EntityWrapper<HuanzheEntity>();
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(huanzheService.selectGroup(params, ew));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> m : result) {
            for (String k : m.keySet()) {
                if (m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date) m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }


}
