package com.zbh.advertising_service.utils;

import com.alibaba.fastjson.JSONObject;
import com.zbh.advertising_service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 获取微信accesstoken定时器
 */

@Component
public class GetWxAccessTokenTask implements ServletContextListener {
    private Timer timer = null;
    @Autowired
    private UserMapper userMapper;


    class GetAccessTokenTask extends TimerTask {//继承TimerTask类
        @Override
        public void run() {
            System.out.println("2小时一次获取微信accessToken");
            JSONObject json = JSONObject.parseObject(getWxAccessToken());
            if(json.containsKey("access_token")) {
                Constant.ACCESS_TOKEN = json.getString("access_token");
            }
        }
    }


    public void contextInitialized ( ServletContextEvent servletContextEvent){
        timer = new Timer();
        servletContextEvent.getServletContext().log("定时器已启动");
        JSONObject json = JSONObject.parseObject(getWxAccessToken());
        if(json.containsKey("access_token")) {
            Constant.ACCESS_TOKEN = json.getString("access_token");
            int expires = json.getInteger("expires_in");
            timer.schedule(new GetAccessTokenTask(),expires*1000,expires*1000);//延迟60秒，定时2小时
        } else {
            try {
                Thread.sleep(3000);
                json = JSONObject.parseObject(getWxAccessToken());
                if(json.containsKey("access_token")) {
                    Constant.ACCESS_TOKEN = json.getString("access_token");
                    int expires = json.getInteger("expires_in");
                    timer.schedule(new GetAccessTokenTask(),expires*1000,expires*1000);//延迟60秒，定时2小时
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取微信accessToken
     * @return
     */
    public static String getWxAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + Constant.WX_APP_ID+"&secret="+Constant.WX_APP_SECRET;
        String result = HttpUrlConnectionUtil.sendGet(url);
        return result;
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (timer != null) {
            timer.cancel();
            servletContextEvent.getServletContext().log("定时器销毁");
        }
    }
}
