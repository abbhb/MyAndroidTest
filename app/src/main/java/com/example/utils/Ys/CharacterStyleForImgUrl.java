package com.example.utils.Ys;

import android.content.Context;

import com.example.myapplicationtest.CommUtil;
import com.example.myapplicationtest.R;
import com.example.utils.GsonUtil;

import java.util.HashMap;
import java.util.Map;

public class CharacterStyleForImgUrl {
    private static final Map<String, String> map = new HashMap<>();

    static {
        map.put("纳西妲","https://upload-bbs.mihoyo.com/game_record/genshin/character_icon/UI_AvatarIcon_Nahida.png");
    }

    public static void pullConfig(Context context) {
        String content = CommUtil.getInstance().readJsonFile(context, "role_icon.json", "{}");
        map.putAll(convertMap(GsonUtil.parseJson(content, Map.class)));
        content = CommUtil.getInstance().readJsonFile(context, "weapon_icon.json", "{}");
        map.putAll(convertMap(GsonUtil.parseJson(content, Map.class)));

    }

    /**
     *
     * @param name 角色名
     * @return ImgUrl
     */
    public static String get(String name) {
        return map.get(name);
    }



    private static Map<String, String> convertMap(Map map) {
        Map<String, Object> request = new HashMap<>(map);
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : request.entrySet()) {
            Object value = entry.getValue();
            result.put(entry.getKey(), (String) value);
        }
        return result;
    }
}
