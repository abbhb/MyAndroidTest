package com.example.utils;
import android.content.Context;
import android.content.res.AssetManager;

import com.example.myapplicationtest.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonUtil {


    private static Gson gson = null;

    static {
        gson = new Gson();
    }

    private GsonUtil() {}

    /**
     * 将object对象转成json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        String gsonString = null;
        if (gson != null && object != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }


    /**
     * 将gsonString转成泛型bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T parseJson(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }


    /**
     * 转成list
     * 解决泛型在编译期类型被擦除导致报错
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }


    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <K, V> Map<K, List<V>> GsonToMapLists(String gsonString, Class<K> kClazz, Class<V> vClazz) {
        Map<K, List<V>> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, TypeToken.getParameterized(Map.class, kClazz, TypeToken.getParameterized(List.class, vClazz).getType()).getType());
        }
        return map;
    }
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public static Map<String,Integer> GsonToMapForYuanShenNotPublicShare(String jsonString){
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonString);   @SuppressWarnings("unchecked")
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Integer> valueMap = new HashMap<String, Integer>();
            while (keyIter.hasNext())
            {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                if (value.equals("风")){
                    valueMap.put(key, R.color.feng);
                }
                if (value.equals("冰")){
                    valueMap.put(key, R.color.bing);
                }
                if (value.equals("水")){
                    valueMap.put(key, R.color.shui);
                }
                if (value.equals("火")){
                    valueMap.put(key, R.color.huo);
                }
                if (value.equals("草")){
                    valueMap.put(key, R.color.cao);
                }
                if (value.equals("雷")){
                    valueMap.put(key, R.color.lei);
                }
                if (value.equals("岩")){
                    valueMap.put(key, R.color.yan);
                }
            }
            return valueMap;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}