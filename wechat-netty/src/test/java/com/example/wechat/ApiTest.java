package com.example.wechat;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;


public class ApiTest {

    public static void main(String[] args) {
        System.out.println("hi 微信公众号：bugstack虫洞栈");
        int max = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));

        System.out.println(max);
    }

}
