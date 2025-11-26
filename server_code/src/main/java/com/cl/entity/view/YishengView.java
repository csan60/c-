package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.YishengEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 医生
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@TableName("yisheng")
public class YishengView extends YishengEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public YishengView() {
    }

    public YishengView(YishengEntity yishengEntity) {
        try {
            BeanUtils.copyProperties(this, yishengEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
