package com.gx.shiro;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName EncryptPractise
 * @Description 加解密练习
 * @Authtor guoxiang
 * @Date 2020/5/11 17:19
 **/
public class EncryptPractise {

    /**
     * Base64加解密操作
     */
    @Test
    public void base64Encode(){
        String str = "hello";
        String base64Encoded = Base64.encodeToString(str.getBytes());
        System.out.println("base64加密后："+ base64Encoded);
        String str2 = Base64.decodeToString(base64Encoded);
        System.out.println("base64解密后："+ str2);
        Assert.assertEquals(str, str2);
    }

    /**
     * 散列算法一般用于生成数据的摘要信息，是一种不可逆的算法，一般适合存储密码之类的数据，常见的散列算法如 MD5、SHA 等。
     * MD5散列算法加密操作
     */
    @Test
    public void md5(){
        String str = "zhangyuehang";
        String md5Str = new Md5Hash(str).toString();
        System.out.println("MD5加密后："+ md5Str);
        // 一般进行散列时最好提供一个 salt（盐），比如加密密码 “admin”，产生的散列值是 “21232f297a57a5a743894a0e4a801fc3”，可以到一些 md5 解密网站很容易的通过散列值得到密码 “admin”
        String salt = "!25&GRE%e$*";
        String md5SaltStr = new Md5Hash(str, salt).toString();
        System.out.println("MD5加盐加密后："+ md5SaltStr);
    }
}
