package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.YuyueguahaoEntity;
import com.cl.entity.view.YuyueguahaoView;
import com.cl.service.YuyueguahaoService;
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
 * 预约挂号
 * 后端接口
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@RestController
@RequestMapping("/yuyueguahao")
public class YuyueguahaoController {
    @Autowired
    private YuyueguahaoService yuyueguahaoService;


    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, YuyueguahaoEntity yuyueguahao,
                  @RequestParam(required = false) Double renshustart,
                  @RequestParam(required = false) Double renshuend,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            yuyueguahao.setYishengzhanghao((String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            yuyueguahao.setHuanzhezhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        if (renshustart != null) ew.ge("renshu", renshustart);
        if (renshuend != null) ew.le("renshu", renshuend);


        PageUtils page = yuyueguahaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuyueguahao), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, YuyueguahaoEntity yuyueguahao,
                  @RequestParam(required = false) Double renshustart,
                  @RequestParam(required = false) Double renshuend,
                  HttpServletRequest request) {
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        if (renshustart != null) ew.ge("renshu", renshustart);
        if (renshuend != null) ew.le("renshu", renshuend);

        PageUtils page = yuyueguahaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuyueguahao), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(YuyueguahaoEntity yuyueguahao) {
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        ew.allEq(MPUtil.allEQMapPre(yuyueguahao, "yuyueguahao"));
        return R.ok().put("data", yuyueguahaoService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(YuyueguahaoEntity yuyueguahao) {
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        ew.allEq(MPUtil.allEQMapPre(yuyueguahao, "yuyueguahao"));
        YuyueguahaoView yuyueguahaoView = yuyueguahaoService.selectView(ew);
        return R.ok("查询预约挂号成功").put("data", yuyueguahaoView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        YuyueguahaoEntity yuyueguahao = yuyueguahaoService.selectById(id);
        yuyueguahao = yuyueguahaoService.selectView(new EntityWrapper<YuyueguahaoEntity>().eq("id", id));
        return R.ok().put("data", yuyueguahao);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        YuyueguahaoEntity yuyueguahao = yuyueguahaoService.selectById(id);
        yuyueguahao = yuyueguahaoService.selectView(new EntityWrapper<YuyueguahaoEntity>().eq("id", id));
        return R.ok().put("data", yuyueguahao);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody YuyueguahaoEntity yuyueguahao, HttpServletRequest request) {
        // 后端兜底：完善必要字段，避免前端遗漏导致唯一索引/空值异常
        fillDefaultFields(yuyueguahao, request);
        // 唯一预约编号兜底生成（前端未传或重复时）
        ensureUniqueYuyuebianhao(yuyueguahao);
        yuyueguahaoService.insert(yuyueguahao);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody YuyueguahaoEntity yuyueguahao, HttpServletRequest request) {
        // 后端兜底：完善必要字段，避免前端遗漏导致唯一索引/空值异常
        fillDefaultFields(yuyueguahao, request);
        // 唯一预约编号兜底生成（前端未传或重复时）
        ensureUniqueYuyuebianhao(yuyueguahao);
        yuyueguahaoService.insert(yuyueguahao);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody YuyueguahaoEntity yuyueguahao, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(yuyueguahao);
        yuyueguahaoService.updateById(yuyueguahao);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        yuyueguahaoService.deleteBatchIds(Arrays.asList(ids));
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
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            ew.eq("huanzhezhanghao", (String) request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(yuyueguahaoService.selectValue(params, ew));
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
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            ew.eq("huanzhezhanghao", (String) request.getSession().getAttribute("username"));
        }
        for (int i = 0; i < yColumnNames.length; i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(yuyueguahaoService.selectValue(params, ew));
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
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            ew.eq("huanzhezhanghao", (String) request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(yuyueguahaoService.selectTimeStatValue(params, ew));
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
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            ew.eq("huanzhezhanghao", (String) request.getSession().getAttribute("username"));
        }
        for (int i = 0; i < yColumnNames.length; i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = MPUtil.snakeListToCamel(yuyueguahaoService.selectTimeStatValue(params, ew));
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
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            ew.eq("yishengzhanghao", (String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            ew.eq("huanzhezhanghao", (String) request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = MPUtil.snakeListToCamel(yuyueguahaoService.selectGroup(params, ew));
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
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params, YuyueguahaoEntity yuyueguahao, HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yisheng")) {
            yuyueguahao.setYishengzhanghao((String) request.getSession().getAttribute("username"));
        }
        if (tableName.equals("huanzhe")) {
            yuyueguahao.setHuanzhezhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<YuyueguahaoEntity> ew = new EntityWrapper<YuyueguahaoEntity>();
        int count = yuyueguahaoService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yuyueguahao), params), params));
        return R.ok().put("data", count);
    }

    // 私有方法：为预约记录补齐默认字段与唯一编号
    private void fillDefaultFields(YuyueguahaoEntity y, HttpServletRequest request){
        if(y.getAddtime()==null){
            y.setAddtime(new Date());
        }
        if(y.getIspay()==null || y.getIspay().trim().isEmpty()){
            y.setIspay("未支付");
        }
        if(y.getJiuzhenzhuangtai()==null || y.getJiuzhenzhuangtai().trim().isEmpty()){
            y.setJiuzhenzhuangtai("待就诊");
        }
        // 根据登录身份补齐账号字段
        try {
            Object tn = request.getSession().getAttribute("tableName");
            Object un = request.getSession().getAttribute("username");
            if(tn!=null && un!=null){
                String tableName = tn.toString();
                String username = un.toString();
                if("huanzhe".equals(tableName) && (y.getHuanzhezhanghao()==null || y.getHuanzhezhanghao().trim().isEmpty())){
                    y.setHuanzhezhanghao(username);
                }
                if("yisheng".equals(tableName) && (y.getYishengzhanghao()==null || y.getYishengzhanghao().trim().isEmpty())){
                    y.setYishengzhanghao(username);
                }
            }
        } catch (Exception ignore){ }
    }

    private void ensureUniqueYuyuebianhao(YuyueguahaoEntity y){
        if(y.getYuyuebianhao()==null || y.getYuyuebianhao().trim().isEmpty() || yuyuebianhaoExists(y.getYuyuebianhao())){
            // 生成不重复的预约编号：YY + yyyyMMddHHmmss + 4位随机数
            int maxTry = 5;
            for(int i=0;i<maxTry;i++){
                String code = genYuyueCode();
                if(!yuyuebianhaoExists(code)){
                    y.setYuyuebianhao(code);
                    return;
                }
            }
            // 退化：极端情况下追加随机串
            String code = genYuyueCode() + new Random().nextInt(9000);
            y.setYuyuebianhao(code);
        }
    }

    private boolean yuyuebianhaoExists(String code){
        if(code==null){
            return false;
        }
        com.baomidou.mybatisplus.mapper.EntityWrapper<YuyueguahaoEntity> ew = new com.baomidou.mybatisplus.mapper.EntityWrapper<>();
        ew.eq("yuyuebianhao", code);
        Integer count = yuyueguahaoService.selectCount(ew);
        return count!=null && count>0;
    }

    private String genYuyueCode(){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        String ts = sdf.format(new Date());
        int rnd = 1000 + new java.util.Random().nextInt(9000);
        return "YY" + ts + rnd;
    }

}
