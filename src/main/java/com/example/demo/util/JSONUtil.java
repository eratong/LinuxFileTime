package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * json操作工具类，基于fastjson封装
 */
public final class JSONUtil {
    
    /**
     * 默认json格式化方式
     */
    private static final SerializerFeature[] DEFAULT_FORMAT = {SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.QuoteFieldNames, SerializerFeature.SkipTransientField,
            SerializerFeature.SortField};
    
    public static final SerializerFeature[] WRITE_NULL_VAL_FORMAT = {SerializerFeature.WriteDateUseDateFormat,
            SerializerFeature.WriteEnumUsingToString,
            SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.QuoteFieldNames, SerializerFeature.SkipTransientField,
            SerializerFeature.SortField,SerializerFeature.WriteMapNullValue};
    
    private JSONUtil() {
    }
    
    /**
     * 从json获取指定key的字符串
     *
     * @param json json字符串
     * @param key  字符串的key
     * @return 指定key的值
     */
    public static Object getStringFromJSONObject(final String json,
                                                 final String key) {
        BeanUtil.requireNonNull(json, "json is null");
        return JSON.parseObject(json).getString(key);
    }
    
    /**
     * 将字符串转换成JSON字符串
     *
     * @param jsonString json字符串
     * @return 转换成的json对象
     */
    private static JSONObject getJSONFromString(final String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return new JSONObject();
        }
        return JSON.parseObject(jsonString);
    }

    public static JSONObject getJSONObject(final String jsonString){
        if (StringUtils.isEmpty(jsonString)) {
            return new JSONObject();
        }
        return JSON.parseObject(jsonString);
    }
    /**
     * 将json字符串，转换成指定java bean
     *
     * @param jsonStr   json串对象
     * @param beanClass 指定的bean
     * @param <T>       任意bean的类型
     * @return 转换后的java bean对象
     */
    public static <T> T toBean(String jsonStr,
                               Class<T> beanClass) {
        BeanUtil.requireNonNull(jsonStr, "jsonStr is null");
        JSONObject jo = JSON.parseObject(jsonStr);
        jo.put(JSON.DEFAULT_TYPE_KEY, beanClass.getName());
        return JSON.parseObject(jo.toJSONString(), beanClass);
    }
    
    /**
     * @param obj 需要转换的java bean
     * @param <T> 入参对象类型泛型
     * @return 对应的json字符串
     */
    public static <T> String toJson(T obj) {
        BeanUtil.requireNonNull(obj, "obj is null");
        return JSON.toJSONString(obj, DEFAULT_FORMAT);
    }
    
    /**
     * 通过Map生成一个json字符串
     *
     * @param obj 需要转换的java bean
     * @param <T> 入参对象类型泛型
     * @return 对应的json字符串
     */
    public static <T> String toJson(T obj, SimplePropertyPreFilter simplePropertyPreFilter) {
        BeanUtil.requireNonNull(obj, "obj is null");
        return JSON.toJSONString(obj,simplePropertyPreFilter, DEFAULT_FORMAT);
    }
    /**
     * 通过Map生成一个json字符串
     *
     * @param obj 需要转换的java bean
     * @param <T> 入参对象类型泛型
     * @return 对应的json字符串
     */
    public static <T> String toJson(T obj, SimplePropertyPreFilter simplePropertyPreFilter, SerializerFeature[] format) {
        BeanUtil.requireNonNull(obj, "obj is null");
        return JSON.toJSONString(obj,simplePropertyPreFilter, format);
    }
    /**
     * 通过Map生成一个json字符串
     *
     * @param map 需要转换的map
     * @return json串
     */
    public static String toJson(Map<String, Object> map) {
        BeanUtil.requireNonNull(map, "map is null");
        return JSON.toJSONString(map, DEFAULT_FORMAT);
    }
    
    /**
     * 美化传入的json,使得该json字符串容易查看
     *
     * @param jsonString 需要处理的json串
     * @return 美化后的json串
     */
    public static String prettyFormatJson(String jsonString) {
        BeanUtil.requireNonNull(jsonString, "jsonString is null");
        return JSON.toJSONString(getJSONFromString(jsonString), true);
    }
    
    /**
     * 将传入的json字符串转换成Map
     *
     * @param jsonString 需要处理的json串
     * @return 对应的map
     */
    public static Map<String, Object> toMap(String jsonString) {
        BeanUtil.requireNonNull(jsonString, "jsonString is null");
        return getJSONFromString(jsonString);
    }

    public static JSONArray ArraytoJSONArray(String jsonString){
        BeanUtil.requireNonNull(jsonString, "jsonString is null");
        return JSON.parseArray(jsonString);
    }

    /**
     * 转换对应的泛型
     *
     * @param jsonString 需要处理的json串
     * @return
     */
    public static <T> T toBean(String jsonString,
                               TypeReference<T> type) {
        BeanUtil.requireNonNull(jsonString, "jsonString is null");
        return JSONObject.parseObject(jsonString, type);
        
    }
    
}
