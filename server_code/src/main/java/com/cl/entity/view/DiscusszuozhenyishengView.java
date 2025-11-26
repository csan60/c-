package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.DiscusszuozhenyishengEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 坐诊医生评论表
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
@TableName("discusszuozhenyisheng")
public class DiscusszuozhenyishengView extends DiscusszuozhenyishengEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public DiscusszuozhenyishengView() {
    }

    public DiscusszuozhenyishengView(DiscusszuozhenyishengEntity discusszuozhenyishengEntity) {
        try {
            BeanUtils.copyProperties(this, discusszuozhenyishengEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
