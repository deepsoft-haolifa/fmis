package com.ruoyi.fmis.util;


import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 实体工具类
 *
 * @author L.cm
 */
@UtilityClass
public class BeanUtil extends org.springframework.beans.BeanUtils {

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   泛型标记
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名
     * @param <T>      泛型标记
     * @return 对象
     */
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Bean的属性, 支持 propertyName 多级 ：test.user.name
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @return 属性值
     */
    public static Object getProperty(Object bean, String propertyName) {
        if (bean == null) {
            return null;
        }
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        return beanWrapper.getPropertyValue(propertyName);
    }

    /**
     * 设置Bean属性, 支持 propertyName 多级 ：test.user.name
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @param value        属性值
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        Objects.requireNonNull(bean, "bean Could not null");
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        beanWrapper.setPropertyValue(propertyName, value);
    }


    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param source      the source bean
     * @param targetClazz the target bean class
     * @param <T>         泛型标记
     * @return T
     * @throws BeansException if the copying failed
     */
    public static <T> T copyProperties(Object source, Class<T> targetClazz) throws BeansException {
        if (source == null) {
            return null;
        }
        T to = newInstance(targetClazz);
        BeanUtil.copyProperties(source, to);
        return to;
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param sourceList  the source list bean
     * @param targetClazz the target bean class
     * @param <T>         泛型标记
     * @return List
     * @throws BeansException if the copying failed
     */
    public static <T> List<T> copyProperties(Collection<?> sourceList, Class<T> targetClazz) throws BeansException {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            T bean = BeanUtil.copyProperties(source, targetClazz);
            outList.add(bean);
        }
        return outList;
    }


    /**
    * 将类中所有属性和值添加到Map中
    * @author      jony.zeng
    * @param obj
    * @return Map
    * @date        2019/5/29 19:53
    */
    public static Map<String, Object> convertToMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (getValueByFieldName(fieldName, obj) != null) {
                map.put(fieldName, getValueByFieldName(fieldName, obj));
            }
        }

        return map;

    }


    /**
     * 将类中所有属性和值添加到Map中,为null也转化
     * @author      jony.zeng
     * @param obj
     * @return Map
     * @date        2019/5/29 19:53
     */
    public static Map<String, Object> convertToMapNull(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj == null) {
            return null;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            map.put(fieldName, getValueByFieldName(fieldName, obj));
        }

        return map;

    }



    /**
    * 将Map中的值映射到Bean中
    * @author      jony.zeng
    * @date        2020/6/5 11:59
    */
    public static <T> T convertToBean(Map<String, Object> map, Class<T> targetClazz) {
        T to = newInstance(targetClazz);
        Field[] fs = targetClazz.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            try {
                f.set(to,map.get(f.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return to;
    }

    /**
     * 根据属性名获取该类此属性的值
     * @author      jony.zeng
     * @date        2019/5/29 19:53
     */
    private static Object getValueByFieldName(String fieldName, Object object) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        try {
            Method method = object.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(object, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }

    }

}
