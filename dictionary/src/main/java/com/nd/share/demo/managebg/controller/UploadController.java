package com.nd.share.demo.managebg.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.gaea.client.support.WafClientContextHolder;
import com.nd.share.demo.domain.AppUpdInfo;
import com.nd.share.demo.domain.LcBaseDatas;
import com.nd.share.demo.domain.Questions;
import com.nd.share.demo.domain.WordDetail;
import com.nd.share.demo.managebg.dao.CommonDao;
import com.nd.share.demo.managebg.dao.LcBaseDatasDao;
import com.nd.share.demo.managebg.service.CommonService;
import com.nd.share.demo.managebg.service.WordResourceService;
import com.nd.share.demo.managebg.util.ContentUtils;
import com.nd.share.demo.repository.dao.QuestionsDao;
import com.nd.share.demo.repository.dao.WordDetailDao;
import com.nd.share.demo.repository.dao.WordUpdListDao;
import com.nd.share.demo.service.AppService;
import com.nd.share.demo.service.Entity.ModalFeedback;
import com.nd.share.demo.service.Entity.SimpleChoice;
import com.nd.share.demo.service.PackOfflinePkgService;
import com.nd.share.demo.service.WordsInfoService;
import com.nd.share.demo.service.impl.IndexServiceImpl.IndexType;
import com.nd.share.demo.util.Compressor;
import com.nd.share.demo.util.StringUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @author 郭晓斌(121017)
 * @version created on 20160801.
 */
@RestController
public class UploadController {
    private final static Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Resource
    private WordResourceService wordResourceService;
    @Resource
    private PackOfflinePkgService packOfflinePkgService;
    @Resource
    private AppService appService;
    @Resource
    private WordsInfoService wordsInfoService;
    @Resource
    private CommonService commonService;

    @Resource
    private CommonDao commonDao;
    @Resource
    private WordUpdListDao wordUpdListDao;
    @Resource
    private LcBaseDatasDao lcBaseDatasDao;
    @Resource
    private WordDetailDao wordDetailDao;
    @Resource
    private QuestionsDao questionsDao;

    @Value("${lc.uri}")
    private String lcUrl;
    @Value("${lc.version}")
    private String lcVersion;
    @Value("${bsyskey.header}")
    private String basyskeyHeader;
    @Value(value = "${ref.path}")
    private String refPath;
    @Value(value = "${item.uri}")
    private String itemUri;
    @Value(value = "${env.dir}")
    private String envDir;

    HttpHeaders defaultHeaders;


    @RequestMapping(value = "/convert/specialchar", method = RequestMethod.GET)
    public JSONObject convertSpecialChar(@RequestParam(value = "type",required = true) int type,
                                         @RequestParam(value = "dicType",required = true) String dicType,
                                         @RequestParam(value = "special",required = true) String special){
        JSONObject jsonObject = new JSONObject();
        // \uE59A -> 空字符串
        this.convertDetails(dicType,type,special);
        jsonObject.put("msg", "\uE59A -> 空字符串完成");
        return  jsonObject;
    }

    @RequestMapping(value = "/others", method = RequestMethod.GET)
    public JSONObject others(@RequestParam(value = "type",required = true) int type,
                             @RequestParam(value = "dicType",required = true) String dicType){
        JSONObject jsonObject = new JSONObject();
        if(type == 0){
            jsonObject = packOfflinePkgService.packOfflinePkg();
        }else if(type == 3){
            // 全角->半角
            this.convertDetails(dicType,type,"");
            jsonObject.put("msg", "转换完成");
        }else if (type == 4){
            questionsDao.deleteQuestions(Integer.parseInt(dicType));
            this.saveQuestions("provider eq 人教社文言文辞书-释义","sy",dicType);
            this.saveQuestions("provider eq 人教社文言文辞书-通假字","tjz",dicType);
            jsonObject.put("msg","保存成功");
        }else if (type == 5){
            // 批量更新图片
            String msg1 = this.updateResource("img/20160831/PNG图片",dicType,"img");
            String msg2 = this.updateResource("img/20160831/特殊字",dicType,"img");
            String msg3 = this.updateResource("img/20160912/EPS",dicType,"img");
            String msg4 = this.updateResource("img/20160912/TIF",dicType,"img");
            String message = String.format("PNG图片: %s<br/> 特殊字: %s<br/>EPS图片: %s<br/>TIF图片: %s<br/>",
                    msg1, msg2, msg3, msg4);
            jsonObject.put("msg", message);
        }else if (type == 6){
            // 批量更新音频
            String msg1 = this.updateResource("mp3/20160902",dicType,"mp3");
            jsonObject.put("msg","音频文件 " + msg1);
        }else if (type == 7){
            // 更新凡例
            StringBuffer filePath = new StringBuffer();
            filePath.append(ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/"));
            filePath.append(File.separator);
            filePath.append("resource/");
            filePath.append(envDir + "/");
            filePath.append("example/20160908/content.txt");
            String content = StringUtil.getFileContent(filePath.toString());
            //因为git会根据系统自动换\r\n，导致content.txt有的是\r\n，有的是\n，这里统一替换
            content = content.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\r\n");
            // 获取凡例
            Map<String, String> indexMap = this.getIndexContent(IndexType.OTHER);
            JSONArray example = new JSONArray();
            for(Map.Entry<String,String> e : indexMap.entrySet()){
                JSONArray jsonArray = JSONObject.parseArray(e.getValue());
                for(int i = 0; i < jsonArray.size(); i++){
                    // 获取数据
                    String str = JSONObject.toJSONString(jsonArray.get(i),SerializerFeature.WriteMapNullValue);
                    JSONObject tempJson = JSONObject.parseObject(str);
                    if (tempJson.getIntValue("type") == 1) {
                        tempJson.put("description",content);
                    }
                    example.add(tempJson);
                }
                String str = JSONObject.toJSONString(example,SerializerFeature.WriteMapNullValue);
                String csPath = e.getKey();
                String fileName = csPath.substring(csPath.lastIndexOf("/") + 1);
                String fileFullName = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/") + "/resource/" + fileName;
                // 生成临时文件
                this.createTempleFile(fileFullName, Compressor.getStream(dicType,str,"0"));
                // 更新文件
                wordResourceService.reUploadFile(fileFullName, csPath);
                // 删除临时文件
                this.deleteTempleFile(fileFullName);
                break;
            }
            jsonObject.put("msg","凡例更新成功");
        }else{
            // 热修复更新
            AppUpdInfo appUpdInfo = appService.saveAppVersionInfo(1, type, dicType);
            jsonObject.put("pkgVersion", appUpdInfo.getPkgVersion());
            jsonObject.put("appVersion", appUpdInfo.getAppVersion());
            jsonObject.put("type", type == 1 ? "安卓" : "IOS");
        }

        return jsonObject;
    }

    /**
     * 转换全角->半角、\uE59A特殊字符
     *
     * @param dicType
     * @param type
     */
    private void convertDetails(String dicType,int type, String specialChar){
        // 词语详情
        List<WordDetail> wordDetailList = wordDetailDao.getAllWordDetails(Integer.parseInt(dicType));
        String content = null;
        JSONObject paramJson = new JSONObject();
        int num = 0;
        for(int i = 0; i < wordDetailList.size(); i++){
            String uuid = wordDetailList.get(i).getIdentifier();
            content = this.getDetailById(uuid, "0");
            if(type == 3){
                // 全角->半角转换
                content = ContentUtils.replace(content,ContentUtils.getMapping());
            } else if(type == 8){
                if(!content.contains(specialChar)){
                    continue;
                }
                // 替换特殊字符
                content = content.replace(specialChar,"");;
            }
            paramJson = JSONObject.parseObject(content,JSONObject.class);
            // LC词语详情更新
            this.updateDetailById(uuid, paramJson);
            num++;
        }
        logger.info("UploadController.convertDetails:Detail Convert Data's count is {}.",num);

        Map<String,String> indexMap = new HashMap<>();
        // 拼音索引
        indexMap = this.getIndexContent(IndexType.PINYIN);
        this.updateIndexFile(indexMap,dicType,type,specialChar);
        // 笔画索引
        indexMap = this.getIndexContent(IndexType.BIHUA);
        this.updateIndexFile(indexMap,dicType,type,specialChar);
        // 虚词索引
        indexMap = this.getIndexContent(IndexType.XUCI);
        this.updateIndexFile(indexMap,dicType,type,specialChar);
        // 附录
        indexMap = this.getIndexContent(IndexType.FULU);
        this.updateIndexFile(indexMap,dicType,type,specialChar);
        // 其他
        indexMap = this.getIndexContent(IndexType.OTHER);
        this.updateIndexFile(indexMap,dicType,type,specialChar);
    }

    /**
     * 音频/图片资源更新
     *
     * @param uploadFile 上传文件
     * @param fileName   文件名
     * @param csPath     文件cs路径
     * @param type       资源类型
     * @param dicType    辞典类型
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/uploadOne", method = RequestMethod.POST)
    public JSONObject uploadOne(@RequestParam(value = "uploadFile") MultipartFile uploadFile, HttpServletRequest request, String fileName, String csPath, String type, String id,
                                int dicType) throws IOException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        String realName = uploadFile.getOriginalFilename();
        String realFileType = realName.substring(realName.lastIndexOf(".") + 1).toLowerCase();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileType.equals(realFileType)) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "更新的文件只能为[*." + fileType + "]");
            return jsonObject;
        }
        if (!fileName.equals(realName)) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "请选择文件名为:" + fileName + "的文件");
            return jsonObject;
        }

        // 本地上传文件全路径
        String fileFullName = UploadController.class.getResource("/").getPath() + "pkg/" + fileName;

        // 生成临时文件
        this.createTempleFile(fileFullName, uploadFile.getInputStream());
        // 更新文件
        Map<String, Object> uriMap = wordResourceService.reUploadFile(fileFullName, csPath);
        logger.info("key:" + fileFullName);
        logger.info("value:" + csPath);
        logger.info("csUri:" + uriMap.get("csUri"));

        jsonObject.put("code", uriMap.get("code"));
        jsonObject.put("msg", uriMap.get("msg"));

        // 删除临时文件
        this.deleteTempleFile(fileFullName);

        // 更新文件名
        if("0".equals(request.getSession().getAttribute("role")) && ("img".equals(type) || "mp3".equals(type))){
            LcBaseDatas lcBaseDatas = lcBaseDatasDao.searchLcBaseDatas(dicType,id);
            lcBaseDatas.setKey(fileName);
            lcBaseDatas.setValue(csPath);

            // 更新数据
            lcBaseDatasDao.save(lcBaseDatas, dicType);
        }

        return jsonObject;
    }

    /**
     * 获取词语详情信息
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/get/detail", method = RequestMethod.POST)
    public JSONObject getDetail(@RequestBody Map<String,String> map){
        JSONObject jsonObject = new JSONObject();
        String jsonStr = this.getDetailById(map.get("uuid"), "1");
        jsonObject.put("detail",jsonStr);

        return jsonObject;
    }

    /**
     * 更新词语详情
     *
     * @param content 更新内容
     * @param uuid 词语uuid
     * @return
     */
    @RequestMapping(value = "/update/detail", method = RequestMethod.POST)
    public JSONObject updateDetail(String content,String uuid, String dtType, int dtDicType){
        JSONObject paramJson = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        // JSON格式校验
        try {
            // 格式化original
            JSONObject original = JSONObject.parseObject(content,JSONObject.class);
            String strOriginal = JSONObject.toJSONString(original);
            // 获取词语详情
            JSONObject jsonDetail = JSONObject.parseObject(getDetailById(uuid,"0"),JSONObject.class);
            String strCustom_properties = JSONObject.toJSONString(jsonDetail.get("custom_properties"));
            JSONObject custom_properties = JSONObject.parseObject(strCustom_properties,JSONObject.class);
            // 更新词语详情
            custom_properties.put("original",strOriginal);
            jsonDetail.put("custom_properties",custom_properties);
            paramJson = jsonDetail;
        }catch (Exception e){
            e.printStackTrace();

            jsonObject.put("code", 1);
            jsonObject.put("msg", "JSON数据格式不正确");
            return jsonObject;
        }

        // LC词语详情更新
        jsonObject = this.updateDetailById(uuid, paramJson);
        // 更新词条更新列表
        String dicType = commonDao.getDictionaryType(dtDicType);
        wordUpdListDao.saveWord(dicType, uuid);

        return jsonObject;
    }

    /**
     * 生成临时文件
     *
     * @param fileFullName
     * @param inputStream
     */
    private void createTempleFile(String fileFullName, InputStream inputStream) {
    	File file = null;
    	OutputStream os = null;
        try {
            file = new File(fileFullName);
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if(os!=null)
				os.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    /**
     * 删除临时文件
     *
     * @param fileFullName
     */
    private void deleteTempleFile(String fileFullName) {
        File file = new File(fileFullName);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取词语详情响应体
     *
     * @param uuid 词语uuid
     * @param type 处理类型
     * @return
     */
    private String getDetailById(String uuid,String type) {
        String jsonStr = "";
        Map map = new HashMap();
        Object obj = new Object();
        String url = lcUrl + "/" + lcVersion + "/coursewareobjects/{id}?include={include}&coverage={coverage}";
        Map<String, Object> param = new HashMap<>();
        param.put("id", uuid);
        param.put("include", "TI,CG");
        param.put("coverage", "Org/nd/");

        try {
            WafSecurityHttpClient httpClient = new WafSecurityHttpClient();
            if("0".equals(type)){
                obj = httpClient.getForObject(url, Object.class, param);
            }else{
                map = httpClient.getForObject(url, Map.class, param);
                map = (Map<String,String>)map.get("custom_properties");
                obj = JSONObject.parseObject(map.get("original").toString(), JSONObject.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return jsonStr;
        }
        // 格式化JSON
        jsonStr = JSONObject.toJSONString(obj,true);

        return jsonStr;
    }

    /**
     * 更新词语详情
     *
     * @param uuid 词语uuid
     * @param paramJson 更新内容
     * @return
     */
    private JSONObject updateDetailById(String uuid, JSONObject paramJson) {
        JSONObject json = new JSONObject();
        Object obj = new Object();
        String url = lcUrl + "/" + lcVersion + "/coursewareobjects/{id}";
        Map<String, Object> param = new HashMap<>();
        param.put("id", uuid);
        param.put("include", "TI,CG");
        param.put("coverage", "Org/nd/");

        init();
        try {
            HttpEntity<JSONObject> httpEntity = new HttpEntity<>(paramJson,defaultHeaders);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(url, HttpMethod.PUT, httpEntity, JSONObject.class, param);

            json.put("code", 0);
            json.put("msg", "更新成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            json.put("code", 1);
            json.put("msg", "更新失败<br>失败原因:" + e.getMessage());
        }

        return json;
    }

    /**
     * 头部初始化
     */
    private void init() {
        defaultHeaders = new HttpHeaders();
        defaultHeaders.put("Content-Type", Arrays.asList("application/json;charset=UTF-8"));
        String bearerToken = WafClientContextHolder.getToken().getBearerToken();
        defaultHeaders.put("Authorization", Arrays.asList("Bearer \"" + bearerToken + "\""));
    }

    /**
     * 返回索引内容
     *
     * @param type 索引类型
     * @return
     */
    private Map<String,String> getIndexContent(IndexType type){
        List<String> indexUrlList = wordsInfoService.getIndexSrc(type.getValue(), 0, 50);
        if(indexUrlList.size() == 0){
            return null;
        }

        Map<String,String> indexMap = new HashMap<>();
        String indexUrl = null;
        // 附录
        if("$RA0508".equals(type.getValue())){
            for(int i = 0; i < indexUrlList.size(); i++){
                indexUrl = indexUrlList.get(i);
                String src = indexUrl.replace("${ref_path}/",refPath);
                indexMap.put(indexUrl.replace("${ref_path}",""),StringUtil.getContent(src));
            }
        }else{
            indexUrl = indexUrlList.get(0);
            String src = indexUrl.replace("${ref_path}/",refPath);
            indexMap.put(indexUrl.replace("${ref_path}",""),StringUtil.getContent(src));
        }

        return indexMap;
    }

    /**
     * 更新索引文件
     *
     * @param indexMap 索引内容Map
     * @param dicType 辞典类型
     * @param type 操作类型
     */
    private void updateIndexFile(Map<String,String> indexMap, String dicType,int type, String specialChar){
        if(indexMap != null){
            String content = null;
            String fileFullName = null;
            for(Map.Entry<String, String> e : indexMap.entrySet()){
                String csPath = e.getKey();
                content = e.getValue();
                String fileName = csPath.substring(csPath.lastIndexOf("/") + 1);
                // 本地上传文件全路径
                fileFullName = UploadController.class.getResource("/").getPath() + "pkg/" + fileName;
                if(type == 3){
                    // 全角->半角转换
                    content = ContentUtils.replace(content,ContentUtils.getMapping());
                } else if(type == 8){
                    // 替换特殊字符
                    content = content.replace(specialChar,"");
                }
                // 生成临时文件
                this.createTempleFile(fileFullName, Compressor.getStream(dicType,content,"0"));
                // 更新文件
                wordResourceService.reUploadFile(fileFullName, csPath);
                // 删除临时文件
                this.deleteTempleFile(fileFullName);
            }
        }
    }

    /**
     * xml转字符串
     *
     * @param element 转换节点
     * @param startStr 其实字符串
     * @param endStr 结束字符串
     * @return
     */
    private String convertXmlString(Element element, String startStr, String endStr){
        String xmlStr = null;
        xmlStr = element.asXML();
        xmlStr = xmlStr.substring(xmlStr.indexOf(startStr),xmlStr.lastIndexOf(endStr));
        xmlStr = xmlStr.replaceAll(" ","");
        xmlStr = xmlStr.replaceAll("\uE004","");
        return xmlStr;
    }

    /**
     * 保存练习题
     *
     * @param prop LC题目类型
     * @param category DB题目类型
     * @param dicType 辞书类型
     */
    private void saveQuestions(String prop, String category, String dicType){
        // 获取练习题uri集合
        List<String> indexSrcList = commonService.getIndexSrc("$RE0200",0,500, prop);
        String uri = null;
        List<String> correctList = null;// 答案
        String prompt = null;// 答案
        int total = 0;
        int choiceNum = 0;
        int fillNum = 0;
        int otherNum = 0;

        for(int i = 0; i < indexSrcList.size(); i++){
            logger.info("UploadController.others:Question:{}.",indexSrcList.get(i));
            // 单选题对象
            Questions questions = new Questions();
            uri = indexSrcList.get(i).replace("${ref-path}/",itemUri);
            String content = StringUtil.getContent(uri);
            total++;
            try {
                Document document = DocumentHelper.parseText(content);
                Element root = document.getRootElement();
                String text = null;
                String questionType = null;
                List<SimpleChoice> simpleChoiceList = new ArrayList<>();
                Element choiceInteractionXml = root.element("itemBody").element("choiceInteraction");
                Element divXml = root.element("itemBody").element("div");
                // 单选题
                if(choiceInteractionXml != null && "choice".equals(choiceInteractionXml.attributeValue("questionType"))){
                    choiceNum++;
                    // 题型
                    questionType = "choice";
                    // 题干
                    prompt = this.convertXmlString(choiceInteractionXml.element("prompt"), "<div>", "</prompt>");
                    // 选项
                    List<Element> simpleChoiceXmlList = choiceInteractionXml.elements("simpleChoice");
                    for(int j = 0; j < simpleChoiceXmlList.size(); j++){
                        SimpleChoice simpleChoice = new SimpleChoice();
                        Element element = simpleChoiceXmlList.get(j);
                        simpleChoice.setIdentifier(element.attributeValue("identifier"));
                        simpleChoice.setFixed(element.attributeValue("fixed"));
                        simpleChoice.setText(this.convertXmlString(element, "<div>", "</simpleChoice>"));
                        simpleChoiceList.add(simpleChoice);
                    }
                }else if(divXml != null && "data".equals(divXml.attributeValue("questionType")) && divXml.element("div").elements("p").size() == 3){
                    fillNum++;
                    // 题型
                    questionType = "fill";
                    // 填空题
                    prompt = this.convertXmlString(divXml, "<div>", "</div>");
                }else{
                    otherNum++;
                    continue;
                }
                // 答案
                correctList = new ArrayList<>();
                List<Element> correctsXml = root.element("responseDeclaration").element("correctResponse").elements("value");
                if(correctsXml != null){
                    for(int j = 0; j < correctsXml.size(); j++){
                        correctList.add(correctsXml.get(j).getText());
                    }
                }
                // 解析
                List<Element> modalFeedbacksXml = root.elements("modalFeedback");
                List<ModalFeedback> modalFeedbackList = new ArrayList<>();
                if(modalFeedbacksXml != null){
                    for(int j = 0; j < modalFeedbacksXml.size(); j++){
                        ModalFeedback modalFeedback = new ModalFeedback();
                        text  = modalFeedbacksXml.get(j).asXML();
                        int start = text.indexOf("<div>");
                        int end = text.lastIndexOf("</modalFeedback>");

                        if(end > start){
                            text = text.substring(start,end);
                            text = text.replaceAll(" ","");
                        }else{
                            text = "";
                        }
                        modalFeedback.setContent(text);
                        modalFeedback.setIdentifier(modalFeedbacksXml.get(j).attributeValue("identifier"));
                        modalFeedback.setShowHide(modalFeedbacksXml.get(j).attributeValue("showHide"));
                        modalFeedbackList.add(modalFeedback);
                    }
                }
                // 单选题
                questions.setQuestionUrl(indexSrcList.get(i));
                questions.setQuestionType(questionType);
                questions.setCorrects(correctList);
                questions.setPrompt(prompt);
                questions.setSimpleChoiceList(simpleChoiceList);
                questions.setModalFeedbacks(modalFeedbackList);
                questions.setCategory(category);
                logger.info("UploadController.others:Train {} [Prompt:{}].",questionType,prompt);
            }catch (Exception e){
                e.printStackTrace();
            }
            questionsDao.saveQuestions(questions,Integer.parseInt(dicType));
        }
        logger.info("UploadController.others:Train {} Count [total:{},choice:{},fill:{},other:{}].",category,total,choiceNum,fillNum,otherNum);
    }

    /**
     * 更新图片
     *
     * @param dir
     * @param dicType
     * @param resType
     * @return
     */
    private String updateResource(String dir, String dicType, String resType){
        String imgDir = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/") +
                File.separator + "resource" + File.separator + dir;
        File file = new File(imgDir);
        File[] files = file.listFiles();
        int count = 0;
        int excptionCount = 0;
        List<String> failedFiles = new ArrayList<>();
        for (File f : files){
            List<LcBaseDatas> lcBaseDatasList = lcBaseDatasDao.searchByType(Integer.parseInt(dicType),resType,f.getName());
            logger.info("UploadController.others:[key:{}].",f.getName());
            try {
                wordResourceService.reUploadFile(f.getPath(), lcBaseDatasList.get(0).getValue());
                logger.info("UploadController.others:[value:{}]",lcBaseDatasList.get(0).getValue());
                count++;
            }catch (Exception e){
                failedFiles.add(f.getName());
                excptionCount++;
                e.printStackTrace();
            }
        }
        logger.error("失败的文件{}",failedFiles);
        return  "此次更新正常有:" + count + ",异常有:" + excptionCount + ":"+failedFiles.toString();
    }

    //替换特殊字符
    private String replaceSpecialChar(String content) {
        content = content.replace("\uE59A","");
        content = content.replace("\uE59B","");
        return content;
    }

}
