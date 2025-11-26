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
 * 坐诊医生
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@TableName("zuozhenyisheng")
public class ZuozhenyishengEntity<T> implements Serializable {
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
     * 科室
     */

    private String keshi;
    /**
     * 头像
     */

    private String touxiang;
    /**
     * 就诊时间
     */

    private String jiuzhenshijian;
    /**
     * 挂号费
     */

    private Double guahaofei;
    /**
     * 可挂号人数
     */

    private Integer renshu;
    /**
     * 简介
     */

    private String jianjie;
    /**
     * 联系电话
     */

    private String lianxidianhua;
    /**
     * 收藏数
     */

    private Integer storeupNumber;
    /**
     * 评论数
     */

    private Integer discussNumber;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public ZuozhenyishengEntity() {

    }


    public ZuozhenyishengEntity(T t) {
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
     * 获取：头像
     */
    public String getTouxiang() {
        return touxiang;
    }

    /**
     * 设置：头像
     */
    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    /**
     * 获取：就诊时间
     */
    public String getJiuzhenshijian() {
        return jiuzhenshijian;
    }

    /**
     * 设置：就诊时间
     */
    public void setJiuzhenshijian(String jiuzhenshijian) {
        this.jiuzhenshijian = jiuzhenshijian;
    }

    /**
     * 获取：挂号费
     */
    public Double getGuahaofei() {
        return guahaofei;
    }

    /**
     * 设置：挂号费
     */
    public void setGuahaofei(Double guahaofei) {
        this.guahaofei = guahaofei;
    }

    /**
     * 获取：可挂号人数
     */
    public Integer getRenshu() {
        return renshu;
    }

    /**
     * 设置：可挂号人数
     */
    public void setRenshu(Integer renshu) {
        this.renshu = renshu;
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
     * 获取：联系电话
     */
    public String getLianxidianhua() {
        return lianxidianhua;
    }

    /**
     * 设置：联系电话
     */
    public void setLianxidianhua(String lianxidianhua) {
        this.lianxidianhua = lianxidianhua;
    }

    /**
     * 获取：收藏数
     */
    public Integer getStoreupNumber() {
        return storeupNumber;
    }

    /**
     * 设置：收藏数
     */
    public void setStoreupNumber(Integer storeupNumber) {
        this.storeupNumber = storeupNumber;
    }

    /**
     * 获取：评论数
     */
    public Integer getDiscussNumber() {
        return discussNumber;
    }

    /**
     * 设置：评论数
     */
    public void setDiscussNumber(Integer discussNumber) {
        this.discussNumber = discussNumber;
    }

}
