package com.nd.share.demo.util;

import com.nd.gaea.rest.security.authens.UserInfo;
import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Messages;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160602.
 */
public class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(Compressor.class);

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        // UUID生成
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid);
    }

    /**
     * 获取时间(yyyy-MM-dd HH:mm:ss)
     *
     * @param date 时间
     * @return
     */
    public static String getDateStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取时间(yyyy-MM-dd)
     *
     * @param date 时间
     * @return
     */
    public static String getDateStr1(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取时间
     *
     * @param dateStr 时间(yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static Date getDate(String dateStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateStr);
    }

    /**
     * 增加或减少天数
     *
     * @param date 日期
     * @param num  天数
     * @return
     */
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * 两个时间之间相差距离多少天
     *
     * @param currentDate 系统时间
     * @param lastUpdDate 最后更新时间
     * @return 相差天数
     */
    public static long getDistanceDays(Date currentDate, Date lastUpdDate) {
        long time1 = lastUpdDate.getTime();
        long time2 = currentDate.getTime();
        long diff;
        long days;
        diff = time2 - time1;
        days = diff / (1000 * 60 * 60 * 24);

        return days;
    }

    /**
     * 获取下载json文件的字符串内容
     *
     * @param fileUrl 下载地址
     * @return
     */
    public static String getContent(String fileUrl) {
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = null;
        InputStream inStream = null;
        try {
            // 防止url中文乱码
            String uri = encodeUri(fileUrl);
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();

//            reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("GB2312")));
            reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                stringBuffer.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    inStream.close();
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return stringBuffer.toString();
    }

    /**
     * 文件下载中文名编码
     *
     * @param downloadUri 下载地址
     * @return
     */
    public static String encodeUri(String downloadUri) {
        String uri = "";
        String fileName = downloadUri.substring(downloadUri.lastIndexOf("/") + 1);
        try {
            // 解决文件名中文乱码问题
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (Exception e) {
            logger.error("StringUtil.encodeUri:Encode fileName {} error.", fileName);
        }
        uri = downloadUri.substring(0, downloadUri.lastIndexOf("/") + 1) + fileName;

        return uri;
    }

    /**
     * 获取用户Id
     *
     * @param userId 用户Id
     * @return
     */
    public static String getUserId(String userId){
        if(userId == null || userId.equals("")){
            try{
                long userIdLong = 0L;
                UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                userIdLong = userInfo == null ? -1L : Long.parseLong(userInfo
                        .getUserId());
                userId = String.valueOf(userIdLong);
            }catch (Exception e){
                throw new RestfulException(ErrorCode.UNAUTHORIZED, Messages.UNAUTHORIZED, String.format("Illegal access msg:%s.", Messages.UNAUTHORIZED));
            }
        }
        return userId;
    }

    /**
     * 获取文件内容并转为字符串
     *
     * @param filePath
     * @return
     */
    public static String getFileContent(String filePath){
        StringBuffer stringBuffer = new StringBuffer();
        String content = null;
        try {
            content = FileUtils.readFileToString(new File((filePath)),Charset.forName("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }

        return content;
    }

}
