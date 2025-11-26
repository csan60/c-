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
 * 医院信息
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@TableName("yiyuanxinxi")
public class YiyuanxinxiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 医院名称
     */

    private String yiyuanmingcheng;
    /**
     * 医院图片
     */

    private String yiyuantupian;
    /**
     * 就医指引
     */

    private String jiuyizhiyin;
    /**
     * 急诊范围
     */

    private String jizhenfanwei;
    /**
     * 报销流程
     */

    private String baoxiaoliucheng;
    /**
     * 名医介绍
     */

    private String mingyijieshao;
    /**
     * 医院简介
     */

    private String yiyuanjianjie;
    /**
     * 医院地址
     */

    private String yiyuandizhi;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public YiyuanxinxiEntity() {

    }


    public YiyuanxinxiEntity(T t) {
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
     * 获取：医院名称
     */
    public String getYiyuanmingcheng() {
        return yiyuanmingcheng;
    }

    /**
     * 设置：医院名称
     */
    public void setYiyuanmingcheng(String yiyuanmingcheng) {
        this.yiyuanmingcheng = yiyuanmingcheng;
    }

    /**
     * 获取：医院图片
     */
    public String getYiyuantupian() {
        return yiyuantupian;
    }

    /**
     * 设置：医院图片
     */
    public void setYiyuantupian(String yiyuantupian) {
        this.yiyuantupian = yiyuantupian;
    }

    /**
     * 获取：就医指引
     */
    public String getJiuyizhiyin() {
        return jiuyizhiyin;
    }

    /**
     * 设置：就医指引
     */
    public void setJiuyizhiyin(String jiuyizhiyin) {
        this.jiuyizhiyin = jiuyizhiyin;
    }

    /**
     * 获取：急诊范围
     */
    public String getJizhenfanwei() {
        return jizhenfanwei;
    }

    /**
     * 设置：急诊范围
     */
    public void setJizhenfanwei(String jizhenfanwei) {
        this.jizhenfanwei = jizhenfanwei;
    }

    /**
     * 获取：报销流程
     */
    public String getBaoxiaoliucheng() {
        return baoxiaoliucheng;
    }

    /**
     * 设置：报销流程
     */
    public void setBaoxiaoliucheng(String baoxiaoliucheng) {
        this.baoxiaoliucheng = baoxiaoliucheng;
    }

    /**
     * 获取：名医介绍
     */
    public String getMingyijieshao() {
        return mingyijieshao;
    }

    /**
     * 设置：名医介绍
     */
    public void setMingyijieshao(String mingyijieshao) {
        this.mingyijieshao = mingyijieshao;
    }

    /**
     * 获取：医院简介
     */
    public String getYiyuanjianjie() {
        return yiyuanjianjie;
    }

    /**
     * 设置：医院简介
     */
    public void setYiyuanjianjie(String yiyuanjianjie) {
        this.yiyuanjianjie = yiyuanjianjie;
    }

    /**
     * 获取：医院地址
     */
    public String getYiyuandizhi() {
        return yiyuandizhi;
    }

    /**
     * 设置：医院地址
     */
    public void setYiyuandizhi(String yiyuandizhi) {
        this.yiyuandizhi = yiyuandizhi;
    }

}
