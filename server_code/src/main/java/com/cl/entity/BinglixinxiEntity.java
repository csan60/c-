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
 * 病历信息
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@TableName("binglixinxi")
public class BinglixinxiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 病历编号
     */

    private String binglibianhao;
    /**
     * 患者账号
     */

    private String huanzhezhanghao;
    /**
     * 患者姓名
     */

    private String huanzhexingming;
    /**
     * 病情详情
     */

    private String bingqingxiangqing;
    /**
     * 就诊时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date jiuzhenshijian;
    /**
     * 诊断结果
     */

    private String zhenduanjieguo;
    /**
     * 医生账号
     */

    private String yishengzhanghao;
    /**
     * 医生姓名
     */

    private String yishengxingming;
    /**
     * 过敏史
     */

    private String guominshi;
    /**
     * 病史
     */

    private String bingshi;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public BinglixinxiEntity() {

    }


    public BinglixinxiEntity(T t) {
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
     * 获取：病历编号
     */
    public String getBinglibianhao() {
        return binglibianhao;
    }

    /**
     * 设置：病历编号
     */
    public void setBinglibianhao(String binglibianhao) {
        this.binglibianhao = binglibianhao;
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
     * 获取：病情详情
     */
    public String getBingqingxiangqing() {
        return bingqingxiangqing;
    }

    /**
     * 设置：病情详情
     */
    public void setBingqingxiangqing(String bingqingxiangqing) {
        this.bingqingxiangqing = bingqingxiangqing;
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
     * 获取：诊断结果
     */
    public String getZhenduanjieguo() {
        return zhenduanjieguo;
    }

    /**
     * 设置：诊断结果
     */
    public void setZhenduanjieguo(String zhenduanjieguo) {
        this.zhenduanjieguo = zhenduanjieguo;
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
     * 获取：过敏史
     */
    public String getGuominshi() {
        return guominshi;
    }

    /**
     * 设置：过敏史
     */
    public void setGuominshi(String guominshi) {
        this.guominshi = guominshi;
    }

    /**
     * 获取：病史
     */
    public String getBingshi() {
        return bingshi;
    }

    /**
     * 设置：病史
     */
    public void setBingshi(String bingshi) {
        this.bingshi = bingshi;
    }

}
