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
 * 检查单
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@TableName("jianchadan")
public class JianchadanEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 项目名称
     */

    private String xiangmumingcheng;
    /**
     * 检查类型
     */

    private String jianchaleixing;
    /**
     * 检查价格
     */

    private Double jianchajiage;
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
     * 是否支付
     */

    private String ispay;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public JianchadanEntity() {

    }


    public JianchadanEntity(T t) {
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
     * 获取：项目名称
     */
    public String getXiangmumingcheng() {
        return xiangmumingcheng;
    }

    /**
     * 设置：项目名称
     */
    public void setXiangmumingcheng(String xiangmumingcheng) {
        this.xiangmumingcheng = xiangmumingcheng;
    }

    /**
     * 获取：检查类型
     */
    public String getJianchaleixing() {
        return jianchaleixing;
    }

    /**
     * 设置：检查类型
     */
    public void setJianchaleixing(String jianchaleixing) {
        this.jianchaleixing = jianchaleixing;
    }

    /**
     * 获取：检查价格
     */
    public Double getJianchajiage() {
        return jianchajiage;
    }

    /**
     * 设置：检查价格
     */
    public void setJianchajiage(Double jianchajiage) {
        this.jianchajiage = jianchajiage;
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
