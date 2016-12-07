/**
 * 
 */
package com.nd.share.demo.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author 黄梦飞(920225)
 * @version created on 2016年7月1日下午3:38:43.
 */
public class IndexJsonSortUtil {
    /**
     * 根据传入的原始json原始数据最终排序(主业务方法)
     * @param JsonArray 原始数据
     * @return 
     */
    public static JSONArray sortSpellIndexJson(JSONArray jaAll){
        //排列好的大写字母组合
        JSONArray ja1 = sortIndexSpellJsonArrayCommon(jaAll, "letter", false);
        for(int i = 0 ; i < ja1.size() ; i ++ ){
            JSONObject jo2 = ja1.getJSONObject(i);
            JSONArray ja2 = sortIndexSpellJsonArrayCommon(jo2.getJSONArray("spells"),"spell", false);
            for(int j = 0 ; j < ja2.size() ; j ++){
                JSONObject jo3 = ja2.getJSONObject(j);
                JSONArray ja3 = sortIndexSpellJsonArrayCommon(jo3.getJSONArray("words"), "spell", true);
                jo3.put("words", ja3);
            }
            jo2.put("spells", ja2);
        }
        return ja1;
    } 
    /**
     * 根据分类条件分类输出array(通用)
     * @param array 原array
     * @param sortKey 排序所根据的key
     * @param keyCharIndex 排序所根据的位数
     * @param isRhyme 是否为音调比较
     * @param int 阶段标记(1为最外层,2为中部,3为最里层音调)
     * @return
     */
    public static JSONArray sortIndexSpellJsonArrayCommon(JSONArray array,String sortKey,boolean isRhyme){
        JSONArray newarray = new JSONArray();
        for(int i = 0 ; i < array.size() ; i ++ ){
            JSONObject jo = array.getJSONObject(i);
            for(int j = newarray.size() ; j >= 0 ; j --){
                if(j == 0){
                    newarray.add(0, jo);
                    break;
                }
                JSONObject oldjo = newarray.getJSONObject(j-1);
                String spell = (String)jo.get(sortKey);
                String oldspell = (String)oldjo.get(sortKey);
                
                
                if(isRhyme){
                    int keyCharIndex = getRhymeCharIndex(spell);
                    //若为轻音则直接置于队列尾
                    if(keyCharIndex == -1){
                        newarray.add(j,jo);
                        break;
                    }
                    char keychar = spell.charAt(keyCharIndex);
                    char oldkeychar = oldspell.charAt(keyCharIndex);
                    //比较音调大小
                    if(compareRhymes(keychar, oldkeychar) < 0){
                        continue;
                    }else{
                        newarray.add(j, jo);
                        break;
                    }
                }else{
                    if(compareSpells(spell, oldspell) < 0){
                        continue;
                    }else{
                        newarray.add(j,jo);
                        break;
                    }
                }
            }
        }
        return newarray;
    }
    /**
     * 带音标音节排序
     * @param ch
     * @return
     */
    public static int getIndexOfSpRhyme(char ch){
        //拼音排序列表
        char[] chs = {'ā','á','ǎ','à','a','ē','é','ě','è','e','ī','í','ǐ','ì','i','ō','ó','ǒ','ò','o','ū','ú','ǔ','ù','u','ǖ','ǘ','ǚ','ǜ','ü'};
        for(int i = 0 ; i < chs.length ; i++){
           if(chs[i] == ch){
               return i;
           } else{
               continue;
           }
        }
        return 30+ch;
        
    }
    /**
     * 比较两个带音调韵母的大小
     * @param a
     * @param b
     * @return a在b后面,返回1;a在b前面,返回-1;相等返回0
     */
    public static int compareRhymes(char a,char b){
        int indexa = getIndexOfSpRhyme(a);
        int indexb = getIndexOfSpRhyme(b);
        int comp = indexa - indexb;
        if(comp > 0){
            return 1;
        }else if(comp < 0 ){
            return -1;
        }else{
            return 0;
        }
    }
    
    /**
     * 比较两个音节(无音调)的大小
     * @param a
     * @param b
     * @param keycharindex(比较开始的位数)
     * @return
     */
    public static int compareSpells(String a,String b){
        int res = a.compareTo(b);
        if(res < 0 ){
            return -1;
        }else if(res > 0){
            return 1;
        }else{
            return 0;
        }
    }
    /**
     * 获取音调韵母所在的位置,轻音(K-ON)返回-1
     * @param spell
     * @return 自然数为序列号,-1表示该音节为轻声
     */
    public static int getRhymeCharIndex(String spell){
        for(int i = 0 ; i < spell.length() ; i ++ ){
            char ch = spell.charAt(i);
            if(ch > 122){
                return i;
            }else{
                continue;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        /*char[] chs = {'ā','á','ǎ','à','a','ē','é','ě','è','e','ī','í','ǐ','ì','i','ō','ó','ǒ','ò','o','ū','ú','ǔ','ù','u','ǖ','ǘ','ǚ','ǜ','ü'};
        int index = compareRhymes('ǐ', 'ǐ');
        System.out.println('z'+0);
        System.out.println('a'+0);
        System.out.println('Z'+0);
        System.out.println(index);
        System.out.println(getRhymeCharIndex("àng"));
        System.out.println("sp"+"a".compareTo("wa"));
        int resq = 'Ｑ'+0;
        System.out.println("Ｑ:"+resq);
        for(int i = 0 ; i < chs.length ; i ++ ){
            if(i%5 == 4){
                continue;
            }
            
            char a = chs[i] ;
            int b = a + 0;
//            if(b<122){
                System.out.println(""+a+":"+b); 
//            }
            
        }*/

//      reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("GB2312")));
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = null;
        File f = new File("E:/tmpfile/soyin.json");
        InputStream inStream = null;
        try {
            inStream = new FileInputStream(f);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
        	try {
        		if(inStream != null)
				inStream.close();				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));
        String tempString = null;
        // 一次读入一行，直到读入null为文件结束
        try {
            while ((tempString = reader.readLine()) != null) {
                stringBuffer.append(tempString);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String s = stringBuffer.toString();
        JSONArray ja = (JSONArray)JSONArray.parse(s);
        JSONArray jaout = sortSpellIndexJson(ja);
        String outstr = jaout.toString();
        OutputStream os = null;
        try {
            os = new FileOutputStream("E:/99Ufile/蓝翔_267515/suoyinout.json");
            os.write(outstr.getBytes());
            os.close();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	try {
        		if(os != null)
        		os.close();				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
    }

}
