package com.nd.share.demo.util;

import com.nd.share.demo.domain.PkgLogger;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * zip压缩包作成
 *
 * @author 郭晓斌(121017)
 * @version created on 20160531.
 */
public final class Compressor {

    private static final Logger logger = LoggerFactory.getLogger(Compressor.class);

    @Resource
    private static MongoTemplate mongoTemplate;

    /**
     * 压缩包生成（针对文件路劲不包含中文）
     *
     * @param dicType      辞典类型
     * @param fileFullName 文件路劲名（如：word/u123456.json）
     * @param content      文件内容
     * @param type         文件类型（0：字符创，1：文件）
     * @throws IOException
     */
    public static void createZipPkg(String dicType, String fileFullName, String content, String type, ZipOutputStream zos) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[1024];
        int length = 0;

        try {
            // 获取文件流
            is = getStream(dicType, content, type);

            BufferedInputStream bis = new BufferedInputStream(is);
            zos.putNextEntry(new ZipEntry(fileFullName));
            // json内容写入
            while ((length = bis.read(buf)) > 0) {
                zos.write(buf, 0, length);
            }
        } catch (IOException e) {
            logger.error("Compressor.createZipPkg:create file {} error.", fileFullName);
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建压缩包
     *
     * @param dicType      辞典类型
     * @param fileFullName 文件存放路劲（压缩包内）
     * @param content      文件内容（字符串或下载地址）
     * @param type         文件类型（0:文本文件 1:图片/音频）
     * @param zipFile      zip对象
     * @param password     压缩密码
     */
    public static void addStreamToZip(String dicType, String fileFullName, String content, String type, ZipFile zipFile, String password) {
        InputStream is = null;

        try {
            // 解决文件名乱码（特别是中文还有拼音：wéi.mp3）
            zipFile.setFileNameCharset("utf8");
            ZipParameters parameters = new ZipParameters();
            // 压缩方式
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            // 压缩级别(压缩完后至少能减少一半大小)
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            // 用流方式生成压缩文件
            parameters.setSourceExternalStream(true);
            // 开启加密
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            // AES密钥强度
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            // 添加压缩文件（此处可加在压缩文件下的存放目录）
            parameters.setFileNameInZip(fileFullName);
            // 设置密码
            parameters.setPassword(password);
            // 获取文件流
            is = getStream(dicType, content, type);

            zipFile.addStream(is, parameters);
        } catch (Exception e) {
            // 打印日志
            writePkgLogger(dicType, content, e.getMessage());
            logger.error("Compressor.createZipPkg:create file {} error.", fileFullName);
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件流
     *
     * @param dicType 辞典类型
     * @param content 文件内容（字符串或下载地址）
     * @param type    文件类型（0:文本文件 1:图片/音频）
     * @return
     */
    public static InputStream getStream(String dicType, String content, String type) {
        InputStream is = null;

        try {
            // 词语详情（字符串）
            if ("0".equals(type)) {
                is = new ByteArrayInputStream(content.getBytes("UTF-8"));
            } else {
                String uri = StringUtil.encodeUri(content);
                URL url = new URL(uri);
                URLConnection conn = url.openConnection();
                is = conn.getInputStream();
            }
        } catch (Exception e) {
            // 打印日志
            writePkgLogger(dicType, content, e.getMessage());
            logger.error("Compressor.getStream:Error is happend when download file:{}.", content);
            e.printStackTrace();
        }

        return is;
    }

    /**
     * 解压文件
     *
     * @param zipPath  压缩包路劲
     * @param destPath 压缩后存放路劲（例：F:/zip）
     * @param password 密码
     */
    public static void extractAllFiles(String zipPath, String destPath, String password) {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            // 压缩文件是否存在
            if (zipFile.isValidZipFile()) {
                // 是否需要密码
                if (zipFile.isEncrypted()) {
                    zipFile.setPassword(password);
                }
                zipFile.extractAll(destPath);
            }

        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件
     *
     * @param oldFile
     * @param newPath
     */
    public static void copyFile(File oldFile, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;

            //文件存在时
            if (oldFile.exists()) {
                //读入原文件
                inStream = new FileInputStream(oldFile);
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    //字节数 文件大小
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }

            }
        } catch (Exception e) {
            logger.error("copy file err：", e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (fs != null) {
                    fs.close();
                }

            } catch (IOException e) {
                logger.error("close stream err：", e);
            }
        }
    }

    /**
     * 文件下载
     */
    public void downLoadFile(String downloadPath) {

        String downloadURL = "f:\\page.zip";
        int bytesum = 0;
        int byteread = 0;
        Date date = new Date();

        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy/MM/dd");
        String dateFloder = sf1.format(date);

        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            //URL url = new URL(downloadURL + "/" + dateFloder + "/" + "page.zip");
            URL url = new URL("http://localhost:8080/testzip/1.rar");
            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(downloadURL);
            byte[] buffer = new byte[4028];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            logger.info("文件下载成功.....");
        } catch (Exception e) {
            logger.info("下载异常" + e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                inStream = null;
            }
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                fs = null;
            }
        }
    }

    /**
     * 离线包日志
     *
     * @param dicType      辞典类型
     * @param content      内容
     * @param srcExcetpion 异常信息
     */
    private static void writePkgLogger(String dicType, String content, String srcExcetpion) {
        PkgLogger pkgLogger = new PkgLogger();
        pkgLogger.setDicType(dicType);
        pkgLogger.setContent(content);
        pkgLogger.setSrcExcption(srcExcetpion);
        pkgLogger.setCreateDate(new Date());
        mongoTemplate.save(pkgLogger);
    }

}
