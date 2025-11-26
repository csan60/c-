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
 * 科室
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@TableName("keshi")
public class KeshiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 科室
     */

    private String keshi;
    /**
     * 简介
     */

    private String jianjie;
    /**
     * 人员
     */

    private String renyuan;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public KeshiEntity() {

    }


    public KeshiEntity(T t) {
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
     * 获取：科室
     */
    public String getKeshi() {
        return keshi;
    }

    /**
     * 设置：科室
     */
    public void setKeshi(String keshi) {
        this.keshi = keshi;
    }

    /**
     * 获取：简介
     */
    public String getJianjie() {
        return jianjie;
    }

    /**
     * 设置：简介
     */
    public void setJianjie(String jianjie) {
        this.jianjie = jianjie;
    }

    /**
     * 获取：人员
     */
    public String getRenyuan() {
        return renyuan;
    }

    /**
     * 设置：人员
     */
    public void setRenyuan(String renyuan) {
        this.renyuan = renyuan;
    }

}
