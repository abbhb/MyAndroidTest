package com.example.myapplicationtest;

import static com.example.utils.GsonUtil.getJson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.utils.GsonUtil;
import com.example.utils.Log;
import com.example.utils.StringUtil;
import com.example.utils.SystemUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CommUtil {

    private final String CACHE_NAME = "qumohe";

    private static CommUtil instance;

    public static CommUtil getInstance() {
        if (instance == null) {
            instance = new CommUtil();
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
    }

    public List<List<String>> splitList(List<String> list, int size) {
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i+=size) {
            List<String> subList = list.subList(i, Math.min(i + size, list.size()));
            if (!subList.isEmpty()) {
                result.add(subList);
            }
        }
        return result;
    }

    public void sendMessage(int what, String data, Handler handler) {
        Message msg = new Message();
        msg.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public Map<String, String> parseUrl(String url) {
        Map<String, String> result = new HashMap<>();
        url = url.substring(url.indexOf("?") + 1);
        for (String s : url.split("&")) {
            if (s.contains("=")) {
                result.put(s.split("=")[0], s.split("=")[1]);
            }
        }
        return result;
    }

    public String toUrl(Map<String, String> resultMap) {
        StringBuilder url = new StringBuilder();
        for (String key : resultMap.keySet()) {
            if ("game_biz".equals(key)) {
                resultMap.put(key, "hk4e_cn");
            }
            url.append(key).append("=").append(resultMap.get(key)).append("&");
        }
        return url.substring(0, url.length() - 2);

    }

    public void writeCacheFile(Context context, String content, String fileName) {
        String path = context.getResources().getString(R.string.cache_path);
        try {
            path = getFileRoot(context) + path;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path + fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
            fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            Log.e(e);
        }
    }

    public List<String> getAccounts(Context context) {
        SharedPreferences cache = CommUtil.getInstance().getSharedPreferences(context);
        List<String> uids = GsonUtil.jsonToList(cache.getString("uid", "[]"), String.class);
        String path = context.getResources().getString(R.string.cache_path);
        path = getFileRoot(context) + path;
        File dir = new File(path);
        if (!dir.exists() || dir.listFiles() == null) {
            return uids;
        }
        List<String> list = Arrays.stream(dir.listFiles())
                .map(File::getName)
                .map(name -> name.substring(0, name.indexOf("-")))
                .filter(name -> name.matches("\\d{9}"))
                .distinct()
                .collect(Collectors.toList());
        list.removeAll(uids);
        uids.addAll(list);
        return uids;
    }

    public String readCacheFile(Context context, String fileName, String defaultValue) {
        String path = context.getResources().getString(R.string.cache_path);
//        String path = context.getAssets()
//        String json = getJson(fileName, context);
        StringBuilder content = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            path = getFileRoot(context) + path;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path + fileName)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            Log.e(e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String s = content.toString();
//        String s = json;
        return "".equals(s) ? defaultValue : s;
    }

    private static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    public int getCharacterColor(Map<String, String> data, String name) {
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("风", R.color.feng);
        colorMap.put("火", R.color.huo);
        colorMap.put("冰", R.color.bing);
        colorMap.put("水", R.color.shui);
        colorMap.put("雷", R.color.lei);
        colorMap.put("岩", R.color.yan);
        return 0;
    }

    public TextView generateTextView(Context context, Integer color, String text) {
        LinearLayout.LayoutParams etParam = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        etParam.setMargins(0, SystemUtil.Dp2Px(context, 10), SystemUtil.Dp2Px(context, 6), 0);
        // 单个角色
        TextView textView = generateNormalTextView(context, color, text);
        textView.setLayoutParams(etParam);
        return textView;
    }

    public TextView generateNormalTextView(Context context, Integer color, String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        // 设置属性
        Resources resources = context.getResources();
        if (color != null) {
            textView.setTextColor(resources.getColor(color));
        }
        return textView;
    }

    public String getVersionName(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(e);
        }
        return null;
    }




    public void showDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("提示").setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", (dialog, which) -> {})
                .create().show();
    }



    public String getString(String str, String defaultValue) {
        if (StringUtil.isNotBlank(str)) {
            return str;
        }
        return defaultValue;
    }

}
