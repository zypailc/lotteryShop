package com.didu.lotteryshop.common.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

//import org.apache.tomcat.util.codec.binary.Base64;


/**
 * Aes数据对称加密工具类
 * @author CHJ
 * @date 2019-09-27 9:42
 */
public class AesEncryptUtil {

    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    //可配置到Constant中，并读取配置文件注入,16位,自己定义
    private static final String KEY = "xxxxxxxxxxxxxxxx";

    /**
     * 加密
     * @param content 加密的字符串
     * @param encryptKey key值
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        byte[] b = cipher.doFinal(content.getBytes("utf-8"));
        // 采用base64算法进行转码,避免出现中文乱码
        //return Base64.encodeBase64String(b);
        return Base64.encode(b);

    }

    /**
     * 解密
     * @param encryptStr 解密的字符串
     * @param decryptKey 解密的key值
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        // 采用base64算法进行转码,避免出现中文乱码
    //    byte[] encryptBytes = Base64.decodeBase64(encryptStr);
        byte[] encryptBytes = Base64.decode(encryptStr);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    public static String encrypt(String content) throws Exception {
        return encrypt(content, KEY);
    }
    public static String decrypt(String encryptStr) throws Exception {
        return decrypt(encryptStr, KEY);
    }

    /**
     * 密码加密 不可逆
     * @param password 密码
     * @param secretKey 秘钥
     * @return
     */
    public static String encrypt_code(String password,String secretKey){
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            String s = password+secretKey;
            byte[] inputByteArray = s.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素

            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

    public static void main(String[] args) throws Exception {
  /*      Map map=new HashMap<String,String>();
        map.put("key","value");
        map.put("中文","汉字");
        System.out.println("六位数字："+(int)((Math.random()*9+1)*1000000000));
        String content = Convert.toStr(map);
        System.out.println("加密前：" + content);
        String s1 = "42de934ec1b94019bf337a1a47563ba9";//CodeUtil.getUuid();
        String s2 = "b01416492f834dddad713a0d1e15346f";//CodeUtil.getUuid();
        System.out.println("s1:"+s1);//42de934ec1b94019bf337a1a47563ba9
        System.out.println("s2:"+s2);//b01416492f834dddad713a0d1e15346f
        System.out.println("s1.2:"+AesEncryptUtil.encrypt_code(s1,s2));//83CDD78BBB0249E0C4A694FA9991437A
        String encrypt = encrypt(content, KEY);
        System.out.println("加密后：" + encrypt);

        String decrypt = decrypt(encrypt, KEY);
        System.out.println("解密后：" + decrypt);

        //0E14309A5592C544A683E5C623295027
        //0E14309A5592C544A683E5C623295027
        System.out.println("mima:"+AesEncryptUtil.encrypt_code("1","b13828b542d244bf9b08e5c2caf95df8"));
        System.out.println("mima1:"+AesEncryptUtil.encrypt_code("972661448111982254","b13828b542d244bf9b08e5c2caf95df8"));
        //生成4个uuid
        System.out.println("1:"+CodeUtil.getUuid());
        System.out.println("2:"+CodeUtil.getUuid());
        System.out.println("3:"+CodeUtil.getUuid());
        System.out.println("4:"+CodeUtil.getUuid());

        String str = "\"Gt6LgNNQOJrd2AmmeUdpi9GZ7ec3c1Wvlze93Z+QYwCurzlITbiWuZioa0tmGsMjvh52AALCCPEjiyfUXjd1/lyFmDmonkqBamL7WKJWXlTm8iajz5HVryZ/1YhkHqG9EU7z9576QUuse3LkL+Lnpg==\"";
        System.out.println("indexof:"+str.replaceAll("\"",""));
        System.out.println("indexof_str:"+str);
        str = str.replaceAll("\"","");
        String ss = AesEncryptUtil.decrypt(str, Constants.AES_ETHWALLET_KEY);
        System.out.println("indexof1:"+ss);

        String wallet = "net.sf.ezmorph.bean.MorphDynaBean@4e30280b["+
                "{fileName=UTC--2019-11-12T06-17-58.702000000Z--327f742b07dc456f54b2f2815bd964bc17328ae0.json, address=0x327f742b07dc456f54b2f2815bd964bc17328ae0, userId=2}"+
                //         UTC--2019-11-12T06-17-58.702000000Z--327f742b07dc456f54b2f2815bd964bc17328ae0.json
                "]";
        System.out.println(AesEncryptUtil.encrypt("UTC--2019-11-12T06-17-5fadsfasdfadsfadsfdsf8.702000000Z--327f742b07dc456f54b2f2815bd964bc17328ae0.json",Constants.KEY_THREE));
        //SLeGFAaTwsTnsqLCNQvXzBY0EcQ65uV1v1jIbQdUFh5Q9fx4XEKPpYYUvWiuUtla5XZPy6cQyvFliLJuNI3/rr/AdtecYaCZIFj0kD8GyOK2Q5B4QUfJrw4IeQBRJ9Ii
        System.out.println("SLeGFAaTwsTnsqLCNQvXzBY0EcQ65uV1v1jIbQdUFh5Q9fx4XEKPpYYUvWiuUtla5XZPy6cQyvFliLJuNI3/rr/AdtecYaCZIFj0kD8GyOK2Q5B4QUfJrw4IeQBRJ9Ii".length());
        //SLeGFAaTwsTnsqLCNQvXzGr6fC5d+KYQJZJZhWhkr9NFtq3t0ykqAzR1r70FtaT6ev2nV4jHPNW3J+7gQElTcu5jvh+6s8M9yYFbXnwXIGuC8VDASazbqYEPKv87d42FsokVJjojUxNRB6k4Of46oQ==
        System.out.println(AesEncryptUtil.decrypt("SLeGFAaTwsTnsqLCNQvXzBY0EcQ65uV1v1jIbQdUFh5Q9fx4XEKPpYYUvWiuUtla5XZPy6cQyvFliLJuNI3/rr/AdtecYaCZIFj0kD8GyOK2Q5B4QUfJrw4IeQBRJ9Ii",Constants.KEY_THREE));*/
        /*System.out.println("mima:"+AesEncryptUtil.encrypt_code("1",Constants.KEY_TOW));

        System.out.println("mima:"+AesEncryptUtil.decrypt("icx44PKACC5WOJke8+U53Q==",Constants.KEY_TOW));*/


        Date nowDate = new Date();
        //彩票结束时间
        Date endTime = new Date(1577507100);//"2019-12-28 12:25:00"

        //彩票开奖间隔时间(单位分钟)
        BigDecimal intervalDate = new BigDecimal("10");

        //彩票结束时间加开奖间隔时间
        //Date endAndIntervalDate = DateUtils.addMinutes(endTime,intervalDate.intValue());
        //延迟10分钟后开奖
        Date delayEndTime = DateUtils.addMinutes(endTime,10);
        endTime = DateUtils.addMinutes(new Date(),-11);
        Date endAndIntervalDate = DateUtils.addMinutes(endTime,intervalDate.intValue());
        System.out.println(nowDate.after(delayEndTime));
        System.out.println(nowDate.before(endAndIntervalDate));



    }

}
