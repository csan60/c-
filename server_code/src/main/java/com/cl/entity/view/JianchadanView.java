package com.cl.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.cl.entity.JianchadanEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 检查单
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
@TableName("jianchadan")
public class JianchadanView extends JianchadanEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public JianchadanView() {
    }

    public JianchadanView(JianchadanEntity jianchadanEntity) {
        try {
            BeanUtils.copyProperties(this, jianchadanEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
