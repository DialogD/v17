package com.hgz.v17msg.utils;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: DJA
 * @Date: 2019/11/27
 */
public class ChannelUtils {

    //map处于共享状态--不能使用线程不安全的对象HashMap
    private static final Map<String, Channel> map = new ConcurrentHashMap<>();

    public static void add(String userId,Channel channel){
        map.put(userId,channel);
    }

    public static void remove(String userId){
        map.remove(userId);
    }

    public static void remove(Channel channel){
        for (Map.Entry<String,Channel> entry : map.entrySet()){
            if (entry.getValue()==channel){
                map.remove(entry.getKey());
                return;
            }
        }
    }

    public static Channel getChannel(String userId){
        return map.get(userId);
    }
}
