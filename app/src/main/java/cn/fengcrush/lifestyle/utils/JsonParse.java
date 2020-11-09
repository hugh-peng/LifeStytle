package cn.fengcrush.lifestyle.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.fengcrush.lifestyle.entity.CompanyName;

public class JsonParse {
    private static JsonParse instance;
    private JsonParse() {}
    public static JsonParse getInstance() {
        if(instance == null) {
            instance = new JsonParse();
        }
        return instance;
    }
    /**
     * 将获取到的数据流转化为Json
     */
    private String read(InputStream in){
        BufferedReader reader = null;
        StringBuilder sb = null;
        String line = null;
        try {
            //StringBuilder字符拼接效率高
            sb = new StringBuilder();
            //用InputStreamReader把in字节流转化为字符流BufferReader
            reader = new BufferedReader(new InputStreamReader(in));
            //拼接字符内容
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if(in != null) in.close();
                if(reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //解析Json文件并返回
    public List<CompanyName> getInfoFromJson(Context context) {
        List<CompanyName> nameList = new ArrayList<>();
        InputStream is = null;
        try {
            //获取本地的Json文件
            is = context.getResources().getAssets().open("company.json");
            String json = read(is);
            //新建gson解析json
            Gson gson = new Gson();
            //创建TypeToken的匿名子类，并条用getType()方法
            Type listType = new TypeToken<List<CompanyName>>() {
            }.getType();
            List<CompanyName> infoList = gson.fromJson(json,listType);
            return infoList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameList;
    }
}
