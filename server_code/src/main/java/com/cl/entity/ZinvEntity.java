package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * 子女用户
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2024-01-01 10:00:00
 */
@TableName("zinv")
public class ZinvEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public ZinvEntity() {
		
	}
	
	public ZinvEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 创建时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	/**
	 * 账号
	 */
	private String zhanghao;
		
	/**
	 * 密码
	 */
	private String mima;
		
	/**
	 * 姓名
	 */
	private String xingming;
		
	/**
	 * 性别
	 */
	private String xingbie;
		
	/**
	 * 手机
	 */
	private String shouji;
		

		
	/**
	 * 头像
	 */
	private String touxiang;
		
	/**
	 * 关联患者ID
	 */
    @JsonProperty("guanlian_huanzhe_id")
    private Long guanlianHuanzheId;
		
	/**
	 * 备注（表结构中未定义，避免持久化）
	 */
	@TableField(exist = false)
	private String beizhu;

	/**
	 * 关联手机号（对应表字段 guanlian_shouji，非空）
	 */
    @JsonProperty("guanlian_shouji")
    private String guanlianShouji;

	/**
	 * 年龄（对应表字段 age，可为空）
	 */
	private Integer age;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：创建时间
	 */
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getAddtime() {
		return addtime;
	}
	/**
	 * 设置：账号
	 */
	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	/**
	 * 获取：账号
	 */
	public String getZhanghao() {
		return zhanghao;
	}
	/**
	 * 设置：密码
	 */
	public void setMima(String mima) {
		this.mima = mima;
	}
	/**
	 * 获取：密码
	 */
	public String getMima() {
		return mima;
	}
	/**
	 * 设置：姓名
	 */
	public void setXingming(String xingming) {
		this.xingming = xingming;
	}
	/**
	 * 获取：姓名
	 */
	public String getXingming() {
		return xingming;
	}
	/**
	 * 设置：性别
	 */
	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}
	/**
	 * 获取：性别
	 */
	public String getXingbie() {
		return xingbie;
	}
	/**
	 * 设置：手机
	 */
	public void setShouji(String shouji) {
		this.shouji = shouji;
	}
	/**
	 * 获取：手机
	 */
	public String getShouji() {
		return shouji;
	}
	/**
	 * 设置：头像
	 */
	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}
	/**
	 * 获取：头像
	 */
	public String getTouxiang() {
		return touxiang;
	}
	/**
	 * 设置：关联患者ID
	 */
	public void setGuanlianHuanzheId(Long guanlianHuanzheId) {
		this.guanlianHuanzheId = guanlianHuanzheId;
	}
	/**
	 * 获取：关联患者ID
	 */
	public Long getGuanlianHuanzheId() {
		return guanlianHuanzheId;
	}
	/**
	 * 设置：备注
	 */
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
	/**
	 * 获取：备注
	 */
	public String getBeizhu() {
		return beizhu;
	}

	/**
	 * 设置：关联手机号
	 */
    public void setGuanlianShouji(String guanlianShouji) {
        this.guanlianShouji = guanlianShouji;
    }
	/**
	 * 获取：关联手机号
	 */
    public String getGuanlianShouji() {
        return guanlianShouji;
    }

	/**
	 * 设置：年龄
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * 获取：年龄
	 */
	public Integer getAge() {
		return age;
	}
}