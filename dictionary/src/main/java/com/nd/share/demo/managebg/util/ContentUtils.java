package com.nd.share.demo.managebg.util;

import com.alibaba.fastjson.JSONObject;
import com.nd.sdp.cs.common.CsConfig;
import com.nd.sdp.cs.sdk.Dentry;
import com.nd.sdp.cs.sdk.ExtendUploadData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160801.
 */
@Component
public class ContentUtils {

    @Value(value = "${cs.host}")
    private String csHost;
    @Value(value = "${cs.path}")
    private String csPath;

    private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/', };

    private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
            60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
            -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
            38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
            -1, -1 };

    /**
     * 文件上传CS
     *
     * @param fileName  文件名
     * @param distPath  CS路径
     * @param jsonUri   上传文件全路径
     * @param sessionId
     * @return
     * @throws Exception
     */
    public String reUpload(String fileName, String distPath, String jsonUri, String sessionId) throws Exception {
        ExtendUploadData requestData = new ExtendUploadData();
        requestData.setFilePath(distPath);
        CsConfig.setHost(csHost);

        Dentry request = new Dentry();
        request.setName(fileName);
        request.setScope(1);   //0-私密，1-公开，默认：0，可选,暂时指定为公开
        System.out.println("jsonUri=======" + jsonUri);
        Dentry returnDentry = request.upload(csPath, jsonUri, requestData, sessionId, null);
        return returnDentry.getPath();
    }

    /**
     * 密码是否是正序或反序连续4位及以上
     * @param pwd
     * @return true为正确，false为错误。
     */
    public static boolean isPasswordContinuous(String pwd) {
        int count = 0;//正序次数
        int reverseCount = 0;//反序次数
        String[] strArr = pwd.split("");
        for(int i = 1 ; i < strArr.length-1 ; i ++) {//从1开始是因为划分数组时，第一个为空
            if(isPositiveContinuous(strArr[i],strArr[i+1])) {
                count ++;
            }
            else {
                count = 0;
            }
            if(isReverseContinuous(strArr[i],strArr[i+1])) {
                reverseCount ++;
            }
            else {
                reverseCount = 0;
            }
            if(count > 2 || reverseCount > 2) break;
        }
        if(count > 2 || reverseCount > 2) return false;
        return true;
    }

    /**
     * 是否是正序连续
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isPositiveContinuous(String str1 , String str2) {
        if(str2.hashCode() - str1.hashCode() == 1) return true;
        return false;
    }
    /**
     * 是否是反序连续
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isReverseContinuous(String str1 , String str2) {
        if(str2.hashCode() - str1.hashCode() == -1) return true;
        return false;
    }
    /**
     * - 字母区分大小写，可输入符号<br/>
     - 密码必须同时包含字母和数字<br/>
     - 密码长度8-20位<br/>
     - 密码中不能存在连续4个及以上的数字或字母（如：1234、7654、abcd、defgh等）<br/>
     * @param password 密码
     * @return true为正确，false为错误
     */
    public static JSONObject isPasswordAvailable(String password) {
        JSONObject jsonObject = new JSONObject();
        String str = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";//必须同时包含字母数字并且是8-20位
        if(password.matches(str)) {
            boolean flag = isPasswordContinuous(password);
            if(flag){
                jsonObject.put("code",0);
                jsonObject.put("msg","");
            }else{
                jsonObject.put("code",1);
                jsonObject.put("msg","密码中不能存在连续4个及以上的数字或字母（如：1234、7654、abcd、defgh等）");
            }
        }else{
            jsonObject.put("code",1);
            jsonObject.put("msg","密码必须同时包含字母数字并且是8-20位");
        }
        return jsonObject;
    }

    /**
     * 解密
     * @param str
     * @return
     */
    public static byte[] decode(String str) {
        byte[] data = str.getBytes();
        int len = data.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
        int i = 0;
        int b1, b2, b3, b4;

        while (i < len) {
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }

            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

            do {
                b3 = data[i++];
                if (b3 == 61) {
                    return buf.toByteArray();
                }
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

            do {
                b4 = data[i++];
                if (b4 == 61) {
                    return buf.toByteArray();
                }
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write((int) (((b3 & 0x03) << 6) | b4));
        }
        return buf.toByteArray();
    }
    /**
     * 全角半角映射
     * @return
     */
    public static Map<String,String> getMapping(){
        Map<String,String> mapping=new HashMap<String, String>();
        char start2='a';
        char start1='ａ';
        for (int i = 0; i < 26; i++) {
            mapping.put(Character.toString((char)(start1+i)),Character.toString((char)(start2+i)));
        }

        return mapping;
    }

    /**
     * 全角转半角
     * @param content 内容
     * @param mapping 全角半角映射
     * @return 转半角之后的内容
     */
    public static String replace(String content,Map<String,String> mapping){
        for (Map.Entry<String, String> stringStringEntry : mapping.entrySet()) {
            content=content.replaceAll(stringStringEntry.getKey(),stringStringEntry.getValue());
        }
        return content;
    }
}
