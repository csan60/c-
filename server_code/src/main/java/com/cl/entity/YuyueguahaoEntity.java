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
 * 预约挂号
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@TableName("yuyueguahao")
public class YuyueguahaoEntity<T> implements Serializable {
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
     * 挂号费
     */

    private Double guahaofei;
    /**
     * 可挂号人数
     */

    private Integer renshu;
    /**
     * 预约时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date yuyueshijian;
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
    /**
     * 既往病史
     */

    private String jiwangbingshi;
    /**
     * 就诊状态
     */

    private String jiuzhenzhuangtai;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public YuyueguahaoEntity() {

    }


    public YuyueguahaoEntity(T t) {
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
     * 获取：预约时间
     */
    public Date getYuyueshijian() {
        return yuyueshijian;
    }

    /**
     * 设置：预约时间
     */
    public void setYuyueshijian(Date yuyueshijian) {
        this.yuyueshijian = yuyueshijian;
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
     * 获取：就诊状态
     */
    public String getJiuzhenzhuangtai() {
        return jiuzhenzhuangtai;
    }

    /**
     * 设置：就诊状态
     */
    public void setJiuzhenzhuangtai(String jiuzhenzhuangtai) {
        this.jiuzhenzhuangtai = jiuzhenzhuangtai;
    }

}
