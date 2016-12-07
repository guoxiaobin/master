package com.nd.dictionary.internal.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Zip打包器
 * Created by yanguanyu on 2016/9/7.
 */
public class ZipPackager {

    private static final Logger logger = LoggerFactory.getLogger(ZipPackager.class);

    private String zipFullFilePath;
    private String encryptPassword;

    private ZipFile zipFile;


    public ZipPackager(String zipFullFilePath, String password) throws ZipException {
        if (StringUtils.isEmpty(zipFullFilePath)) {
            throw new IllegalArgumentException("zipFullFilePath must not be null");
        }
        zipFile = new ZipFile(zipFullFilePath);
        zipFile.setFileNameCharset("utf8");// 解决文件名中文乱码
        encryptPassword = password;
    }
    
    private ZipParameters defaultZipParameters(String fileNameInZip) {
        ZipParameters zipParameters = new ZipParameters();
        // 压缩方式
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        // 压缩级别(压缩完后至少能减少一半大小)
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        // 用流方式生成压缩文件
        zipParameters.setSourceExternalStream(true);
        // 开启加密
        zipParameters.setEncryptFiles(true);
        // 加密方式
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
        // AES密钥强度
        zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        // 添加压缩文件（此处可加在压缩文件下的存放目录）
        zipParameters.setFileNameInZip(fileNameInZip);
        // 设置密码
        zipParameters.setPassword(encryptPassword);
        return zipParameters;
    }

    /**
     * 添加单个文件流到zip中
     * @param fileNameInZip file name in zip
     * @param inputStream InputSteam
     * @throws ZipException
     */
    public void addStreamToZip(String fileNameInZip, InputStream inputStream) throws ZipException {
        try {
            zipFile.addStream(inputStream, defaultZipParameters(fileNameInZip));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("addStreamToZip inputStream close failed", e);
                }
            }
        }
    }

//    /**
//     * 添加多个文件流到zip中
//     * @param streamMap Map key->value : fileNameInZip->Stream
//     * @throws ZipException
//     */
//    public void addStreamToZip(Map<String, InputStream> streamMap) throws ZipException {
//        for (Map.Entry<String, InputStream> entry : streamMap.entrySet()) {
//            addStreamToZip(entry.getKey(), entry.getValue());
//        }
//    }

}
