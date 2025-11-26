package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 患者
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@TableName("huanzhe")
public class HuanzheEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 患者账号
     */

    private String huanzhezhanghao;
    /**
     * 密码
     */

    private String mima;
    /**
     * 患者姓名
     */

    private String huanzhexingming;
    /**
     * 头像
     */

    private String touxiang;
    /**
     * 性别
     */

    private String xingbie;
    /**
     * 手机号码
     */

    private String shoujihaoma;
    /**
     * 年龄
     */

    private String nianling;
    /**
     * 手机号
     */

    private String mobile;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public HuanzheEntity() {

    }


    public HuanzheEntity(T t) {
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
     * 获取：密码
     */
    public String getMima() {
        return mima;
    }

    /**
     * 设置：密码
     */
    public void setMima(String mima) {
        this.mima = mima;
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
     * 获取：性别
     */
    public String getXingbie() {
        return xingbie;
    }

    /**
     * 设置：性别
     */
    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
    }

    /**
     * 获取：手机号码
     */
    public String getShoujihaoma() {
        return shoujihaoma;
    }

    /**
     * 设置：手机号码
     */
    public void setShoujihaoma(String shoujihaoma) {
        this.shoujihaoma = shoujihaoma;
    }

    /**
     * 获取：年龄
     */
    public String getNianling() {
        return nianling;
    }

    /**
     * 设置：年龄
     */
    public void setNianling(String nianling) {
        this.nianling = nianling;
    }

    /**
     * 获取：手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置：手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
