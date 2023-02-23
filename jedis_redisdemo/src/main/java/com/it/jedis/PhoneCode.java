package com.it.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * 连接jedis 超时原因
 * 1. redis数据库 需要 更改 两个设置 并 重启
 * 2. linux防火墙 的端口6379 为开放 可关闭防火墙  systemctl stop firewalld       firewall-cmd --reload
 * 或者 开启端口  firewall-cmd --zone=public --add-port=22/tcp --permanent （--permanent永久生效，没有此参数重启后失效）
 */
public class PhoneCode {
    public static void main(String[] args) {
        //模拟验证码发送
        sendCode("132222222222");
        //模拟验证
      //  getRedisCode("132222222222","900276");
    }

    //3验证码 校验
    public static void getRedisCode(String phone, String code) {
        Jedis jedis = new Jedis("192.168.95.134", 6379);
        //从 redis中 获取
        String codeKey = "VerifyCode" + phone + "code";
        String redisCode = jedis.get(codeKey);
        //判断
        if (redisCode.equals(code)) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
        jedis.close();
    }

    //2.让每个手机每天只能发 3 次 验证码放到redis 中 设置 过期 时间120s
    public static void sendCode(String phone) {
        Jedis jedis = new Jedis("192.168.95.134", 6379);
        //拼接 key
        //手机发送次数的key
        String countKey = "VerifyCode" + phone + "count";
        //验证码的key
        String codeKey = "VerifyCode" + phone + "code";

        //每个手机每天只能发送三次
        String count = jedis.get(countKey);
        if (count == null) {
            //没有发送次数 第一次发送 设置发送次数 1
            jedis.setex(countKey, 24 * 60 * 60, "1");
        } else if (Integer.parseInt(count) <= 2) {
            //发送次数+1
            jedis.incr(countKey);
        } else if (Integer.parseInt(count) > 2) {
            //发送3次 不能在发送
            System.out.println("今天发送次数 已经超过 3次");
            jedis.close();
            return;
        }
        //发送验证码 放到 redis 中
        String vCode = getCode();
        jedis.setex(codeKey, 120, vCode);
        jedis.close();
    }

    //1.生成 6位数字验证码
    public static String getCode() {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }
}
