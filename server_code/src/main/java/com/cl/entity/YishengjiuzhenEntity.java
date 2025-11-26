package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 医生就诊
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@TableName("yishengjiuzhen")
public class YishengjiuzhenEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 预约编号
     */

    private String yuyuebianhao;
    /**
     * 医生账号
     */

    private String yishengzhanghao;
    /**
     * 医生姓名
     */

    private String yishengxingming;
    /**
     * 患者账号
     */

    private String huanzhezhanghao;
    /**
     * 患者姓名
     */

    private String huanzhexingming;
    /**
     * 既往病史
     */

    private String jiwangbingshi;
    /**
     * 诊断问题
     */

    private String zhenduanwenti;
    /**
     * 治疗方案
     */

    private String zhiliaofangan;
    /**
     * 就诊详情
     */

    private String jiuzhenxiangqing;
    /**
     * 药品信息
     */

    private String yaopinxinxi;
    /**
     * 医嘱
     */

    private String yizhu;
    /**
     * 费用明细
     */

    private Double feiyongmingxi;
    /**
     * 就诊时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date jiuzhenshijian;
    /**
     * 是否支付
     */

    private String ispay;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public YishengjiuzhenEntity() {

    }


    public YishengjiuzhenEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：预约编号
     */
    public String getYuyuebianhao() {
        return yuyuebianhao;
    }

    /**
     * 设置：预约编号
     */
    public void setYuyuebianhao(String yuyuebianhao) {
        this.yuyuebianhao = yuyuebianhao;
    }

    /**
     * 获取：医生账号
     */
    public String getYishengzhanghao() {
        return yishengzhanghao;
    }

    /**
     * 设置：医生账号
     */
    public void setYishengzhanghao(String yishengzhanghao) {
        this.yishengzhanghao = yishengzhanghao;
    }

    /**
     * 获取：医生姓名
     */
    public String getYishengxingming() {
        return yishengxingming;
    }

    /**
     * 设置：医生姓名
     */
    public void setYishengxingming(String yishengxingming) {
        this.yishengxingming = yishengxingming;
    }

    /**
     * 获取：患者账号
     */
    public String getHuanzhezhanghao() {
        return huanzhezhanghao;
    }

    /**
     * 设置：患者账号
     */
    public void setHuanzhezhanghao(String huanzhezhanghao) {
        this.huanzhezhanghao = huanzhezhanghao;
    }

    /**
     * 获取：患者姓名
     */
    public String getHuanzhexingming() {
        return huanzhexingming;
    }

    /**
     * 设置：患者姓名
     */
    public void setHuanzhexingming(String huanzhexingming) {
        this.huanzhexingming = huanzhexingming;
    }

    /**
     * 获取：既往病史
     */
    public String getJiwangbingshi() {
        return jiwangbingshi;
    }

    /**
     * 设置：既往病史
     */
    public void setJiwangbingshi(String jiwangbingshi) {
        this.jiwangbingshi = jiwangbingshi;
    }

    /**
     * 获取：诊断问题
     */
    public String getZhenduanwenti() {
        return zhenduanwenti;
    }

    /**
     * 设置：诊断问题
     */
    public void setZhenduanwenti(String zhenduanwenti) {
        this.zhenduanwenti = zhenduanwenti;
    }

    /**
     * 获取：治疗方案
     */
    public String getZhiliaofangan() {
        return zhiliaofangan;
    }

    /**
     * 设置：治疗方案
     */
    public void setZhiliaofangan(String zhiliaofangan) {
        this.zhiliaofangan = zhiliaofangan;
    }

    /**
     * 获取：就诊详情
     */
    public String getJiuzhenxiangqing() {
        return jiuzhenxiangqing;
    }

    /**
     * 设置：就诊详情
     */
    public void setJiuzhenxiangqing(String jiuzhenxiangqing) {
        this.jiuzhenxiangqing = jiuzhenxiangqing;
    }

    /**
     * 获取：药品信息
     */
    public String getYaopinxinxi() {
        return yaopinxinxi;
    }

    /**
     * 设置：药品信息
     */
    public void setYaopinxinxi(String yaopinxinxi) {
        this.yaopinxinxi = yaopinxinxi;
    }

    /**
     * 获取：医嘱
     */
    public String getYizhu() {
        return yizhu;
    }

    /**
     * 设置：医嘱
     */
    public void setYizhu(String yizhu) {
        this.yizhu = yizhu;
    }

    /**
     * 获取：费用明细
     */
    public Double getFeiyongmingxi() {
        return feiyongmingxi;
    }

    /**
     * 设置：费用明细
     */
    public void setFeiyongmingxi(Double feiyongmingxi) {
        this.feiyongmingxi = feiyongmingxi;
    }

    /**
     * 获取：就诊时间
     */
    public Date getJiuzhenshijian() {
        return jiuzhenshijian;
    }

    /**
     * 设置：就诊时间
     */
    public void setJiuzhenshijian(Date jiuzhenshijian) {
        this.jiuzhenshijian = jiuzhenshijian;
    }

    /**
     * 获取：是否支付
     */
    public String getIspay() {
        return ispay;
    }

    /**
     * 设置：是否支付
     */
    public void setIspay(String ispay) {
        this.ispay = ispay;
    }

}
