package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("emergency_contacts")
public class EmergencyContact implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id; // 主键

    private Long userId; // 所属老人用户ID

    private String name; // 联系人姓名

    private String relationship; // 与老人关系（如 儿子、女儿、邻居）

    private String phone; // 联系人手机号

    private String address; // 居住地址（可选）
}
