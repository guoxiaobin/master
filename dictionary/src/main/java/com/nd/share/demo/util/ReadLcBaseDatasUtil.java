package com.nd.share.demo.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class ReadLcBaseDatasUtil {

    /**
     * 读取数据，并返回key-value集合
     *
     * @param fileNameMap 文件名集合
     * @return
     * @throws IOException
     */
    public static Map<String, String> getMapFromFile(Map<String,String> fileNameMap){
        Map<String, String> map = new HashMap<>();
        for(Map.Entry e : fileNameMap.entrySet()){
            String path = ReadLcBaseDatasUtil.class.getResource("/").getPath() + "/" + e.getValue();
            try {
                List<String> lines = IOUtils.readLines(new FileInputStream(new File(path)), "utf-8");
                for (String line : lines) {
                    if (line.equals("")) {
                        continue;
                    }
                    String[] kv = line.split(":");
                    if (kv[0].equals("")) {
                        continue;
                    }
                    map.put(kv[0], kv[1]);
                }
            }catch (Exception ex){
                return null;
            }
        }
        return map;
    }
}
