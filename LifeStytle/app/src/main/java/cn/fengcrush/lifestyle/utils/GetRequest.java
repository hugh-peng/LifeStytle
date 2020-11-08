package cn.fengcrush.lifestyle.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class GetRequest {
    private static final String host = "http://api09.aliyun.venuscn.com";      //请求地址
    private static final String path = "/express/trace/query";                //请求目录
    private static final String method = "GET";                              //请求方式

    public static String getRequest(Context context,String cno, String queryMsg) {
        String no ="";        //可以接收选择的公司编号，也可以是空值查询；有编号查询结果更加准确
        no = cno;
        String res = "";
        try{
            URL url = new URL(host+path+"?comid="+no+"&number="+queryMsg);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);    //请求方法
            //请求头
            connection.setRequestProperty("Authorization","APPCODE 0f367bbd709a43dda29e4bc63d3eab23");
            connection.setUseCaches(false);         //不进行缓存
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            //请求成功
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(connection.getInputStream(),"utf-8"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line=in.readLine()) != null) {
                    buffer.append(line);
                }
                res = buffer.toString();
                return res;
            }else {
                //避免在子线程中弹出Toast报错
                Looper.prepare();
                Toast.makeText(context,"请求失败,请检查运单号或公司编号！",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }


}
