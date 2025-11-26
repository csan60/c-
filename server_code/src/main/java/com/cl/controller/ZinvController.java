package com.cl.controller;

import com.cl.annotation.IgnoreAuth;
import com.cl.entity.ZinvEntity;
import com.cl.entity.HuanzheEntity;
import com.cl.service.ZinvService;
import com.cl.service.HuanzheService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.CommonUtil;
import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 子女用户
 * 后端接口
 * @author 
 * @email 
 * @date 2024-01-01 10:00:00
 */
@RestController
@RequestMapping("/zinv")
public class ZinvController {
    @Autowired
    private ZinvService zinvService;

    @Autowired
    private HuanzheService huanzheService;

    @Autowired
    private TokenService tokenService;



    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        // 调试日志：确认请求到达
        System.out.println("=== 子女用户登录接口被调用 ===");
        System.out.println("接收到的用户名: " + username);
        System.out.println("接收到的密码: " + (password != null ? "***" : "null"));
        System.out.println("================================");
        ZinvEntity u = zinvService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>().eq("zhanghao", username));
        if(u==null || !u.getMima().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(u.getId(),username, "zinv", "子女用户");
        // 在后端控制台输出生成的token
        System.out.println("=== 子女用户登录成功 ===");
        System.out.println("子女账号: " + username);
        System.out.println("生成的Token: " + token);
        System.out.println("用户ID: " + u.getId());
        System.out.println("角色: 子女用户");
        System.out.println("=================");
        R r = R.ok();
        r.put("token", token);
        r.put("role", "子女用户");
        r.put("tableName", "zinv");
        return r;
    }

    /**
     * 注册
     */
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody ZinvEntity zinv, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zinv);
        
        // 检查账号是否已存在
        if(zinvService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>().eq("zhanghao", zinv.getZhanghao())) !=null) {
            return R.error("账号已存在");
        }
        
        // 检查手机号是否已被注册
        if(zinv.getShouji() != null) {
            // 标准化个人手机号
            String sj = zinv.getShouji().replaceAll("[\\s-]", "");
            zinv.setShouji(sj);
        }
        if(zinvService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>().eq("shouji", zinv.getShouji())) !=null) {
            return R.error("手机号已被注册");
        }
        // 关联手机号为必填（与数据库约束一致）
        if (zinv.getGuanlianShouji() == null) {
            return R.error("关联手机号不能为空");
        }
        

        // 通过子女填写的关联手机号自动匹配老人（优先 huanzhe.mobile，兼容 huanzhe.shoujihaoma）并设置关联ID（强约束：必须匹配到）
        if (zinv.getGuanlianShouji() != null) {
            // 标准化并校验手机号
            String phone = String.valueOf(zinv.getGuanlianShouji());
            phone = phone.replaceAll("[\\s-]", "");
            if (org.apache.commons.lang3.StringUtils.isBlank(phone)) {
                return R.error("关联手机号不能为空");
            }
            if (!phone.matches("^1\\d{10}$")) {
                return R.error("关联手机号格式不正确，请输入11位手机号");
            }
            zinv.setGuanlianShouji(phone);
            HuanzheEntity hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("mobile", phone));
            if (hzByMobile == null) {
                hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("shoujihaoma", phone));
            }
            if (hzByMobile == null) {
                return R.error("关联手机号未找到对应老人，请先在老人端注册该手机号");
            }
            zinv.setGuanlianHuanzheId(hzByMobile.getId());
        }

        zinv.setId(new Date().getTime());
        zinvService.insert(zinv);
        return R.ok();
    }

    /**
     * 退出
     */
    @PostMapping(value = "/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }

    /**
     * 密码重置
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
        ZinvEntity u = zinvService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>().eq("zhanghao", username));
        if(u==null) {
            return R.error("账号不存在");
        }
        u.setMima("123456");
        zinvService.update(u,null);
        return R.ok("密码已重置为：123456");
    }

    /**
     * 列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ZinvEntity zinv,
                  HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("zinv")) {
            zinv.setId((Long)request.getSession().getAttribute("userId"));
        }
        com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity> ew = new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>();

        PageUtils page = zinvService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zinv), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ZinvEntity zinv,
                  HttpServletRequest request){
        com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity> ew = new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>();

        PageUtils page = zinvService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zinv), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        ZinvEntity zinv = zinvService.selectById(id);
        zinv.setMima(null);
        return R.ok().put("data", zinv);
    }

    /**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrZinv(HttpServletRequest request){
        Long id = (Long)request.getSession().getAttribute("userId");
        ZinvEntity u = zinvService.selectById(id);
        u.setMima(null);
        return R.ok().put("data", u);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody ZinvEntity zinv, HttpServletRequest request){
        zinv.setId(new Date().getTime());
        // 基本校验
        if (StringUtils.isBlank(zinv.getZhanghao())) {
            return R.error("账号不能为空");
        }
        if (StringUtils.isBlank(zinv.getMima())) {
            return R.error("密码不能为空");
        }
        if (StringUtils.isBlank(zinv.getXingming())) {
            return R.error("姓名不能为空");
        }
        if (zinv.getGuanlianShouji() == null) {
            return R.error("关联手机号不能为空");
        }
        // 唯一性校验
        if(zinvService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>().eq("zhanghao", zinv.getZhanghao())) != null) {
            return R.error("账号已存在");
        }
        // 通过子女填写的关联手机号自动匹配老人（优先 huanzhe.mobile，兼容 huanzhe.shoujihaoma）并设置关联ID（强约束：必须匹配到）
        if (zinv.getGuanlianShouji() != null) {
            // 标准化并校验手机号
            String phone = zinv.getGuanlianShouji();
            phone = phone.replaceAll("[\\s-]", "");
            if (org.apache.commons.lang3.StringUtils.isBlank(phone)) {
                return R.error("关联手机号不能为空");
            }
            if (!phone.matches("^1\\d{10}$")) {
                return R.error("关联手机号格式不正确，请输入11位手机号");
            }
            zinv.setGuanlianShouji(phone);
            HuanzheEntity hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("mobile", phone));
            if (hzByMobile == null) {
                hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("shoujihaoma", phone));
            }
            if (hzByMobile == null) {
                return R.error("关联手机号未找到对应老人，请先在老人端注册该手机号");
            }
            zinv.setGuanlianHuanzheId(hzByMobile.getId());
        }
        zinvService.insert(zinv);
        return R.ok();
    }

    /**
     * 新增（与前端 $api.add('zinv') 对齐）
     */
    @PostMapping("/add")
    public R add(@RequestBody ZinvEntity zinv, HttpServletRequest request){
        zinv.setId(new Date().getTime());
        // 基本校验
        if (StringUtils.isBlank(zinv.getZhanghao())) {
            return R.error("账号不能为空");
        }
        if (StringUtils.isBlank(zinv.getMima())) {
            return R.error("密码不能为空");
        }
        if (StringUtils.isBlank(zinv.getXingming())) {
            return R.error("姓名不能为空");
        }
        if (zinv.getGuanlianShouji() == null) {
            return R.error("关联手机号不能为空");
        }
        // 唯一性校验
        if(zinvService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>().eq("zhanghao", zinv.getZhanghao())) != null) {
            return R.error("账号已存在");
        }
        // 通过子女填写的关联手机号自动匹配老人（优先 huanzhe.mobile，兼容 huanzhe.shoujihaoma）并设置关联ID（强约束：必须匹配到）
        if (zinv.getGuanlianShouji() != null) {
            // 标准化并校验手机号
            String phone = zinv.getGuanlianShouji();
            phone = phone.replaceAll("[\\s-]", "");
            if (org.apache.commons.lang3.StringUtils.isBlank(phone)) {
                return R.error("关联手机号不能为空");
            }
            if (!phone.matches("^1\\d{10}$")) {
                return R.error("关联手机号格式不正确，请输入11位手机号");
            }
            zinv.setGuanlianShouji(phone);
            HuanzheEntity hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("mobile", phone));
            if (hzByMobile == null) {
                hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("shoujihaoma", phone));
            }
            if (hzByMobile == null) {
                return R.error("关联手机号未找到对应老人，请先在老人端注册该手机号");
            }
            zinv.setGuanlianHuanzheId(hzByMobile.getId());
        }
        zinvService.insert(zinv);
        return R.ok("新增成功");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ZinvEntity zinv, HttpServletRequest request){
        if (zinv.getId() == null) {
            return R.error("缺少ID");
        }
        ZinvEntity exist = zinvService.selectById(zinv.getId());
        if (exist == null) {
            return R.error("记录不存在");
        }
        // 若传入账号且准备修改，需要校验唯一性
        if (StringUtils.isNotBlank(zinv.getZhanghao())) {
            ZinvEntity same = zinvService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<ZinvEntity>()
                    .eq("zhanghao", zinv.getZhanghao()));
            if (same != null && !same.getId().equals(exist.getId())) {
                return R.error("账号已存在");
            }
            exist.setZhanghao(zinv.getZhanghao());
        }
        if (zinv.getMima() != null && StringUtils.isNotBlank(zinv.getMima())) {
            exist.setMima(zinv.getMima());
        }
        if (zinv.getXingming() != null) {
            exist.setXingming(zinv.getXingming());
        }
        if (zinv.getXingbie() != null) {
            exist.setXingbie(zinv.getXingbie());
        }
        if (zinv.getShouji() != null) {
            exist.setShouji(zinv.getShouji());
        }
        if (zinv.getTouxiang() != null) {
            exist.setTouxiang(zinv.getTouxiang());
        }
        if (zinv.getGuanlianHuanzheId() != null) {
            // 可选：校验关联患者是否存在
            HuanzheEntity hz = huanzheService.selectById(zinv.getGuanlianHuanzheId());
            if (hz == null) {
                return R.error("关联的患者不存在");
            }
            exist.setGuanlianHuanzheId(zinv.getGuanlianHuanzheId());
        }
        if (zinv.getGuanlianShouji() != null) {
            // 标准化并校验手机号，避免写入异常格式
            String phone = zinv.getGuanlianShouji();
            phone = phone == null ? null : phone.replaceAll("[\\s-]", "");
            if (org.apache.commons.lang3.StringUtils.isBlank(phone)) {
                return R.error("关联手机号不能为空");
            }
            // 这里只校验中国大陆 11 位手机号格式，如需支持更多地区可调整规则
            if (!phone.matches("^1\\d{10}$")) {
                return R.error("关联手机号格式不正确，请输入11位手机号");
            }

            exist.setGuanlianShouji(phone);
            // 通过子女填写的关联手机号自动匹配老人（优先 huanzhe.mobile，兼容 huanzhe.shoujihaoma）并设置关联ID（强约束：必须匹配到）
            HuanzheEntity hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("mobile", phone));
            if (hzByMobile == null) {
                hzByMobile = huanzheService.selectOne(new com.baomidou.mybatisplus.mapper.EntityWrapper<HuanzheEntity>().eq("shoujihaoma", phone));
            }
            if (hzByMobile == null) {
                return R.error("关联手机号未找到对应老人，修改失败");
            }
            exist.setGuanlianHuanzheId(hzByMobile.getId());
        }
        if (zinv.getAge() != null) {
            exist.setAge(zinv.getAge());
        }
        zinvService.updateById(exist);
        return R.ok("修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        zinvService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 修改密码
     */
    @RequestMapping("/updatePassword")
    public R updatePassword(String oldPassword, String newPassword, HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("userId");
        ZinvEntity u = zinvService.selectById(userId);
        if(!oldPassword.equals(u.getMima())) {
            return R.error("原密码不正确");
        }
        if(newPassword == null){
            return R.error("新密码不能为空") ;
        }
        u.setMima(newPassword);
        zinvService.updateById(u);
        return R.ok();
    }

    /**
     * 设置关联患者
     */
    @RequestMapping("/setRelatedPatient")
    public R setRelatedPatient(@RequestParam Long huanzheId, HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("userId");
        ZinvEntity zinv = zinvService.selectById(userId);
        if(zinv == null) {
            return R.error("用户不存在");
        }
        zinv.setGuanlianHuanzheId(huanzheId);
        zinvService.updateById(zinv);
        return R.ok("关联设置成功");
    }

    /**
     * 获取关联的患者信息
     */
    @RequestMapping("/getRelatedPatient")
    public R getRelatedPatient(HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("userId");
        ZinvEntity zinv = zinvService.selectById(userId);
        if(zinv == null || zinv.getGuanlianHuanzheId() == null) {
            return R.error("未关联患者");
        }
        
        // 获取完整的患者信息
        HuanzheEntity huanzhe = huanzheService.selectById(zinv.getGuanlianHuanzheId());
        if(huanzhe == null) {
            return R.error("关联的患者不存在");
        }
        
        return R.ok().put("data", huanzhe);
    }

    /**
     * 取消关联患者
     */
    @RequestMapping("/removeRelatedPatient")
    public R removeRelatedPatient(HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute("userId");
        ZinvEntity zinv = zinvService.selectById(userId);
        if(zinv == null) {
            return R.error("用户不存在");
        }
        zinv.setGuanlianHuanzheId(null);
        zinvService.updateById(zinv);
        return R.ok("取消关联成功");
    }

}