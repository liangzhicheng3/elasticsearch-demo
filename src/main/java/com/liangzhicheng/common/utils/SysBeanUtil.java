package com.liangzhicheng.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.ArrayList;
import java.util.List;

public class SysBeanUtil {

    /**
     * @description 拷贝单个entity
     * @param entity
     * @param cls
     * @param <T>
     * @return
     */
    public static<T> T copyEntity(Object entity, Class<T> cls){
        T target = null;
        if(entity != null){
            target = ReflectUtil.newInstance(cls);
            BeanUtil.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * @description 拷贝整个list，由于hutool只有单个bean的拷贝，没有整个List的拷贝，需要封装一个list的拷贝
     * @param source
     * @param cls
     * @param <T>
     * @return
     */
    public static<T> List<T> copyList(List<?> source, Class<T> cls) {
        if(source == null && source.size() < 1){
            return new ArrayList<>(1);
        }
        List<T> targetList = new ArrayList<>(source.size());
        T target = null;
        for (Object obj : source) {
            if(obj != null){
                target = ReflectUtil.newInstance(cls);
                BeanUtil.copyProperties(obj, target);
            }
            targetList.add(target);
        }
        return targetList;
    }

}
