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
 * 排班信息
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@TableName("paibanxinxi")
public class PaibanxinxiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 医生账号
     */

    private String yishengzhanghao;
    /**
     * 医生姓名
     */

    private String yishengxingming;
    /**
     * 日期
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat
    private Date riqi;
    /**
     * 班次
     */

    private String banci;
    /**
     * 上班时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date shangbanshijian;
    /**
     * 下班时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date xiabanshijian;
    /**
     * 备注
     */

    private String beizhu;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public PaibanxinxiEntity() {

    }


    public PaibanxinxiEntity(T t) {
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
     * 获取：日期
     */
    public Date getRiqi() {
        return riqi;
    }

    /**
     * 设置：日期
     */
    public void setRiqi(Date riqi) {
        this.riqi = riqi;
    }

    /**
     * 获取：班次
     */
    public String getBanci() {
        return banci;
    }

    /**
     * 设置：班次
     */
    public void setBanci(String banci) {
        this.banci = banci;
    }

    /**
     * 获取：上班时间
     */
    public Date getShangbanshijian() {
        return shangbanshijian;
    }

    /**
     * 设置：上班时间
     */
    public void setShangbanshijian(Date shangbanshijian) {
        this.shangbanshijian = shangbanshijian;
    }

    /**
     * 获取：下班时间
     */
    public Date getXiabanshijian() {
        return xiabanshijian;
    }

    /**
     * 设置：下班时间
     */
    public void setXiabanshijian(Date xiabanshijian) {
        this.xiabanshijian = xiabanshijian;
    }

    /**
     * 获取：备注
     */
    public String getBeizhu() {
        return beizhu;
    }

    /**
     * 设置：备注
     */
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

}
