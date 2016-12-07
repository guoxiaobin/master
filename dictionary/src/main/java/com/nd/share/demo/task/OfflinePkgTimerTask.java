package com.nd.share.demo.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nd.dictionary.modules.classic.ClassicDictionaryService;
import com.nd.sdp.cs.sdk.Dentry;
import com.nd.share.demo.domain.*;
import com.nd.share.demo.repository.dao.*;
import com.nd.share.demo.service.AppendixService;
import com.nd.share.demo.service.AuthorwordsService;
import com.nd.share.demo.service.Entity.Word.Src_resources;
import com.nd.share.demo.service.Entity.Word.Word;
import com.nd.share.demo.service.WordsInfoService;
import com.nd.share.demo.service.cs.CsService;
import com.nd.share.demo.util.Compressor;
import com.nd.share.demo.util.StringUtil;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 离线包打包
 *
 * @author 郭晓斌(121017)
 * @version created on 20160613.
 */
@Component
public class OfflinePkgTimerTask {
    private static final Logger logger = LoggerFactory.getLogger(OfflinePkgTimerTask.class);

    @Resource
    private MongoTemplate mongoTemplate;


    @Resource
    private BaseDao baseDao;
    @Resource
    private OfflinePkgDao offlinePkgDao;
    @Resource
    private OfflineListInfoDao offlineListInfoDao;
    @Resource
    private WordUpdListDao wordUpdListDao;
    @Resource
    private ThresholdPkgDao thresholdPkgDao;
    @Resource
    private ThresholdStatusDao thresholdStatusDao;

    @Resource
    private CsService csService;
    @Resource
    private WordsInfoService wordsInfoService;
    @Resource
    private AppendixService appendixService;
    @Resource
    private AuthorwordsService authorwordsService;
    @Resource
    private ClassicDictionaryService classicDictionaryService;

    @Value(value = "${ref.path}")
    private String refPath;

    @Value(value = "${words.data}")
    private String wordsData;

    @Value(value = "${words.index.wordindex}")
    private String wordindex;
    @Value(value = "${words.index.spell}")
    private String spell;
    @Value(value = "${words.index.stroke}")
    private String stroke;
    @Value(value = "${words.index.emptywords}")
    private String emptyWords;

    @Value(value = "${others.knowledge}")
    private String knowledge;
    @Value(value = "${others.discriminate}")
    private String discriminate;
    @Value(value = "${others.hint}")
    private String hint;
    @Value(value = "${others.author}")
    private String author;
    @Value(value = "${others.appendixs}")
    private String appendixs;

    @Value(value = "${json}")
    private String json;
    @Value(value = "${jpg}")
    private String jpg;

    @Value(value = "${zipdownload.url}")
    private String zipUrl;
    @Value(value = "${zip.password}")
    private String zipPassword;

    @Value("${cs.service.name}")
    private String csName;

    // 是否打包增量包（0 否;1 是）
    private int incFlag = 0;
    // 打包状态（0 成功;1 打包中;2 不成功）
    public static int pkgStutas = 0;
    // 更新词条数
    private List<WordUpdList> wordUpdLists;


    /**
     * 离线包打包
     */
    public int packOfflinePkg() {
        // 打包中
        pkgStutas = 1;
        String exceptionDicType = "";

        try {
            // 获取辞典类型
            List<DictionaryTypeList> dictionaryTypeLists = baseDao.getDictionaryTypes();
            for (DictionaryTypeList d : dictionaryTypeLists) {
                // 辞典类型
                String dicType = d.getType();
                exceptionDicType = dicType;

                // 离线包日志
                String content = "[" + StringUtil.getDateStr(new Date()) + "] OfflinePkgTimerTask start.";
                this.writePkgLogger(dicType, content);

                // 获取离线包类型
                List<OfflineListInfo> offlineListInfos = offlineListInfoDao.getOfflineListInfos(d.getType());

                int count = 0;     // 增量包条数阀值
                int day = 0;       // 增量包天数阀值
                int pkgCount = 0;  // 增量包包数阀值
                int incCount = 0;  // 累计增量包数
                int incDay = 0;    // 累计增量包天数
                Date packTime = new Date();

                // 更新词条数
                wordUpdLists = wordUpdListDao.getWordUpdLists(dicType, packTime);
                int updWordCount = wordUpdLists != null ? wordUpdLists.size() : 0;

                // 根据包类型打包
                for (int i = 0; i < offlineListInfos.size(); i++) {
                    OfflineListInfo offlineListInfo = offlineListInfos.get(i);
                    // 获取阀值信息
                    List<ThresholdPkg> thresholdPkgs = thresholdPkgDao.getThresholdPkg(dicType, offlineListInfo.getType());
                    // 获取增量状态值
                    List<ThresholdStatus> thresholdStatuses = thresholdStatusDao.getThresholdStatus(dicType, offlineListInfo.getType());

                    // 取得阀值
                    if (!thresholdPkgs.isEmpty() && thresholdPkgs.size() > 0) {
                        count = thresholdPkgs.get(0).getCount();
                        day = thresholdPkgs.get(0).getDay();
                        pkgCount = thresholdPkgs.get(0).getPkgCount();
                    }
                    // 取得增量值
                    if (!thresholdStatuses.isEmpty() && thresholdStatuses.size() > 0) {
                        incCount = thresholdStatuses.get(0).getIncCount();
                        incDay = thresholdStatuses.get(0).getIncDay();
                    }

                    // 更新词条数>增量包条数阀值
                    if (Long.compare(updWordCount, count) >= 0) {
                        this.excIncrementalPkg(dicType, incCount, pkgCount, thresholdPkgs.get(0).getPackType(), offlineListInfo.getType(), thresholdStatuses.get(0));
                    } else {
                        // 未更新天数>更新天数阈值
                        if (Long.compare(incDay, day) >= 0) {
                            // 执行离线包打包
                            this.excIncrementalPkg(dicType, incCount, pkgCount, thresholdPkgs.get(0).getPackType(), offlineListInfo.getType(), thresholdStatuses.get(0));
                        } else {
                            // 待更新天数+1
                            incDay++;
                            // 更新数据
                            if (!thresholdStatuses.isEmpty() && thresholdStatuses.size() > 0) {
                                thresholdStatuses.get(0).setIncDay(incDay);
                                thresholdStatusDao.save(dicType, thresholdStatuses.get(0));
                            }
                        }
                    }
                }

                // 更新词条状态
                if (Integer.compare(1, incFlag) == 0) {
                    // 更新词条打包标识（词条更新列表）
                    for (WordUpdList w : wordUpdLists) {
                        w.setFlag(1);  // 已打包
                        w.setUpdateTime(new Date());
                        wordUpdListDao.updateWordStatus(dicType, w);
                    }
                }

                // 离线包日志
                content = String.format("[%s] OfflinePkgTimerTask.packOfflinePkg:OfflinePkg excuted Successfully.", StringUtil.getDateStr(new Date()));
                this.writePkgLogger(dicType, content);

                logger.info("OfflinePkgTimerTask.packOfflinePkg:[Dictionary {}] OfflinePkg excuted Successfully.", d.getType());
            }

            // 打包成功
            pkgStutas = 0;
        } catch (Exception e) {
            // 打包不成功
            pkgStutas = 2;
            // 离线包日志
            String content = String.format("[%s] OfflinePkgTimerTask.packOfflinePkg:OfflinePkg error.msg:%s.", StringUtil.getDateStr(new Date()), e.getMessage());
            this.writePkgLogger(exceptionDicType, content);
            logger.info("OfflinePkgTimerTask.packOfflinePkg:[Dictionary {}] OfflinePkg excuted Exception.", exceptionDicType);
        }
        return pkgStutas;
    }

    /**
     * 执行离线包打包
     *
     * @param dicType         辞典类型
     * @param incCount        增量包数
     * @param pkgCount        全量包阈值
     * @param packType        打包类型（1:增量包 2:全量包）
     * @param typeId          离线包类型
     * @param thresholdStatus 阈值状态表
     * @throws IOException
     */
    private void excIncrementalPkg(String dicType, int incCount, int pkgCount, int packType, String typeId, ThresholdStatus thresholdStatus) {

        List<OfflinePkg> offlinePkgs = offlinePkgDao.getLastVerPkgInfo(dicType, "version", typeId);
        // 词条数据集
        List<Word> wordList = new ArrayList<>();
        OfflinePkg offlinePkg = new OfflinePkg();
        int version = 0;
        Dentry dentry = null;
        if (!offlinePkgs.isEmpty() && offlinePkgs.size() > 0) {
            version = offlinePkgs.get(0).getVersion();
        }
        version++; // 版本+1

        // 增量包数>增量包阈值（全量包）
        if (Long.compare(incCount, pkgCount) >= 0) {
            // 获取词条数据集
            wordList = this.getWordUpdLists(2, dicType);
            // 全量包
            offlinePkg.setFlag("2");
            // 增量包数置0
            if(packType == 1){
                incCount = 0;
            }
        } else {
            // 获取词条数据集
            wordList = this.getWordUpdLists(1, dicType);
            // 增量包
            offlinePkg.setFlag("1");
            // 增量包数+1
            incCount++;
        }

        // 没有打包数据，不打包
        if (wordList.size() <= 0) {
            incFlag = 0;
            return;
        }


        // 打包
        incFlag = 1;

        try {
            // 打包处理
            dentry = this.createPkgStruct(dicType, typeId, version, wordList);
        } catch (ZipException e) {
            logger.error("OfflinePkgTimerTask.excIncrementalPkg:create zip type {} error.", typeId);
            e.printStackTrace();
        }

        // 离线包版本
        offlinePkg.setVersion(version);
        // 下载路径
        offlinePkg.setUrl(String.format(zipUrl, dentry != null ? dentry.getDentryId() : ""));
        // 离线包类型
        offlinePkg.setTypeId(typeId);
        // 包大小
        offlinePkg.setSize(dentry != null ? dentry.getInode().getSize() : 0);
        // 创建时间
        offlinePkg.setCreateTime(new Date());
        // 更新离线包明细信息
        offlinePkgDao.save(dicType, offlinePkg);

        // 更新增量阀值状态表
        thresholdStatus.setTypeId(typeId);
        // 打全量包
        thresholdStatus.setIncCount(incCount);
        // 更新天数置0
        thresholdStatus.setIncDay(0);
        thresholdStatusDao.save(dicType, thresholdStatus);
    }

    /**
     * 获取词条数据集
     *
     * @param pkgFlag 打包类型
     * @param dicType 辞典类型
     * @return
     */
    private List<Word> getWordUpdLists(int pkgFlag, String dicType) {
        List<Word> wordList = new ArrayList<>();

        // 文言文
        if ("Classic".equals(dicType)) {
            switch (pkgFlag) {
                // 增量包
                case 1:
                    List<String> wordIdList = new ArrayList<>();
                    // 获取更新词条Id
                    for (WordUpdList w : wordUpdLists) {
                        wordIdList.add(w.getWordId());
                    }
                    // 获取更新词条数据
                    Map<String, Word> words = wordsInfoService.getListLCByIds(wordIdList);
                    for(Map.Entry<String, Word> e : words.entrySet()){
                        wordList.add(e.getValue());
                    }
                    break;
                // 全量包
                case 2:
                    List<Word> wordListTemp = new ArrayList<>();
                    int startLimit = 0;
                    // 分批获取全量数据
                    for (int i = 0; i < 4; i++) {
                        startLimit = i * 500;
                        wordListTemp = wordsInfoService.getListLCByWord("", startLimit, 500);
                        if (wordListTemp != null && wordListTemp.size() > 0) {
                            wordList.addAll(wordListTemp);
                        }
                    }
                    break;
            }
        }

        return wordList;
    }

    /**
     * 打包处理
     *
     * @param dicType  辞典类型
     * @param typeId   包类型
     * @param version  包版本
     * @param wordList 更新词条数据集
     * @return
     * @throws ZipException
     */
    private Dentry createPkgStruct(String dicType, String typeId, int version, List<Word> wordList) throws ZipException {

        // 存放路径
        String url = OfflinePkgTimerTask.class.getResource("/").getPath() + "pkg/";
        String zipFileName = dicType + "_" + typeId + "_" + version + ".zip"; // 包名
        String zipRealPath = url + zipFileName;        // 加密包全路径
        String fileFullName = "";
        Dentry dentry = null;

        // 离线包日志
        String log = "[" + StringUtil.getDateStr(new Date()) + "] SysType:" + System.getProperty("os.name") + ",zipRealPath:" + zipRealPath;
        this.writePkgLogger(dicType, log);

        ZipFile zipFile = new ZipFile(zipRealPath);
        // 基础包
        if ("base".equals(typeId)) {


            // TODO 填充多媒体信息
//            for (Word word : wordList) {
//                try {
//                    Map<String, List<Map<String, Object>>> multimedia = classicDictionaryService.fetchMultimediaInfo(word.getTitle().substring(0,1));
//                    word.setMultimedia(multimedia);
//                } catch (IOException e) {
//                    logger.error(String.format("fetchMultimediaInfo error for %s", word.getTitle().substring(0,1)), e);
//                }
//            }

            String content = "";
            String tblWordDetail = BaseDao.Collection.WORDDETAIL.getName() + "_" + dicType;

            // 重置词语详情表
            if (wordList != null && wordList.size() > 0) {
                if (mongoTemplate.collectionExists(tblWordDetail)) {
                    mongoTemplate.dropCollection(tblWordDetail);
                }
            }

            for (Word w : wordList) {
                if (w.getTitle() == null) {
                    continue;
                }
                // 打包words
                content = JSONObject.toJSONString(w, SerializerFeature.WriteMapNullValue);
                fileFullName = wordsData + w.getIdentifier() + json;
                logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
                Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
                // 打包img
                this.downloadResource(dicType, w.getSrc_resources(), zipFile, "images", "WordDetail's");
                // 词语详情入库
                this.saveWordDetail(dicType, w.getIdentifier(), content, tblWordDetail);
            }

            // 创建词语详情索引
            this.createIndex("identifier",tblWordDetail);

            // 文件下载地址
            List<String> srcList = new ArrayList<>();
            // 图片url列表
            List<Src_resources> src_resources = new ArrayList<>();

            // 字索引
            fileFullName = wordindex + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            srcList = wordsInfoService.getIndexSrc("$RA0509", 0, 10);
            content = this.getJsonContent(srcList);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "WordIndex's");
            // 字索引入库
            this.saveWordsIndex(dicType, srcList);
            // 创建字索引索引
            String tblWordsIndex = BaseDao.Collection.WORDSINDEX.getName() + "_" + dicType;
            this.createIndex("identifier",tblWordsIndex);
            this.createIndex("title",tblWordsIndex);
            this.createIndex("type",tblWordsIndex);

            // 拼音索引
            fileFullName = spell + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            srcList = wordsInfoService.getIndexSrc("$RA0505", 0, 10);
            content = this.getJsonContent(srcList);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "Spell's");

            // 笔画索引
            fileFullName = stroke + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            srcList = wordsInfoService.getIndexSrc("$RA0507", 0, 10);
            content = this.getJsonContent(srcList);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "Stroke's");

            // 虚词索引
            fileFullName = emptyWords + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            srcList = wordsInfoService.getIndexSrc("$RA0506", 0, 10);
            content = this.getJsonContent(srcList);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "EmptyWords's");

            // 小知识
            fileFullName = knowledge + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            content = this.getOther(3);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "Knowledge's");

            // 辨析
            fileFullName = discriminate + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            content = this.getOther(4);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "Discriminate's");

            // 提示
            fileFullName = hint + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            content = this.getOther(5);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "Hint's");

            // 编者的话
            fileFullName = author + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            content = authorwordsService.getAuthorworJson().toJSONString();
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "Author's");

            // 附录/凡例
            fileFullName = appendixs + json;
            logger.info("OfflinePkgTimerTask.createPkgStruct:create file {}.", fileFullName);
            JSONObject jsonObject = appendixService.getAppendix();
            content = JSONObject.toJSONString(jsonObject);
            Compressor.addStreamToZip(dicType, fileFullName, content, "0", zipFile, zipPassword);
            // 获取图片信息
            src_resources = wordsInfoService.getSrcResources(content);
            // 下载图片
            this.downloadResource(dicType, src_resources, zipFile, "images", "Appendixs's");

            // 离线包日志
            log = "[" + StringUtil.getDateStr(new Date()) + "] dicType:" + dicType + ",typeId:" + typeId + "-> OK.";
            this.writePkgLogger(dicType, log);
        }

        // 资源包
        if ("resource".equals(typeId)) {
            for (Word w : wordList) {
                if (w.getTitle() == null) {
                    continue;
                }
                // 打包mp3
                this.downloadResource(dicType, w.getSrc_resources(), zipFile, "audios", "Resource's");
            }

            // 离线包日志
            log = "[" + StringUtil.getDateStr(new Date()) + "] dicType:" + dicType + ",typeId:" + typeId + "-> OK.";
            this.writePkgLogger(dicType, log);
        }

        // 离线包上传
        dentry = csService.uploadFile(zipRealPath, zipFileName);

        // 离线包日志
        log = "[" + StringUtil.getDateStr(new Date()) + "] dicType:" + dicType + ",typeId:" + typeId + ",CS upload OK.";
        this.writePkgLogger(dicType, log);

        // 删除本地离线包
        this.deleteFiles(zipRealPath);

        return dentry;
    }

    /**
     * 下载图片/音频资源
     *
     * @param dicType           辞典类型
     * @param src_resourcesList 资源信息
     * @param zipFile           压缩包对象
     * @param downloadType      下载文件类型（1：图片;2：音频）
     * @param fileType          资源类型
     */
    private void downloadResource(String dicType, List<Src_resources> src_resourcesList, ZipFile zipFile, String downloadType, String fileType) {
        String fileFullName = "";
        String content = "";
        for (Src_resources img : src_resourcesList) {
            if ("images".equals(downloadType)) {
                // 不打包音频
                if (img.getFile_name().contains("mp3")) {
                    continue;
                }
            }
            if ("audios".equals(downloadType)) {
                // 不打包图片
                if (img.getFile_name().contains("png")) {
                    continue;
                }
            }
            fileFullName = img.getFile_path().replace("${ref_path}/", "");
            content = refPath + fileFullName;
            logger.info("OfflinePkgTimerTask.downloadResource:download {} {}:{}.", fileType, downloadType, fileFullName);
            Compressor.addStreamToZip(dicType, downloadType + "/" + fileFullName, content, "1", zipFile, zipPassword);
        }
    }

    /**
     * 获取小知识/辨析/提示
     *
     * @param type 数据标识
     * @return
     */
    private String getOther(int type) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("items", appendixService.getOther(type));
        return JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 获取文件内容
     *
     * @param srcList 文件下载地址
     * @return
     */
    private String getJsonContent(List<String> srcList) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (srcList != null && srcList.size() > 0) {
            String src = srcList.get(0).replace("${ref_path}/", refPath);
            String content = StringUtil.getContent(src);
            jsonArray = JSONArray.parseArray(content);
        }
        jsonObject.put("items", jsonArray);
        return JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 删除本地离线包
     *
     * @param files 文件列表
     */
    private void deleteFiles(String... files) {
        for (String f : files) {
            File file = new File(f);
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    /**
     * 离线包日志
     *
     * @param dicType 辞典类型
     * @param content 内容
     */
    private void writePkgLogger(String dicType, String content) {
        PkgLogger pkgLogger = new PkgLogger();
        pkgLogger.setDicType(dicType);
        pkgLogger.setContent(content);
        pkgLogger.setCreateDate(new Date());
        mongoTemplate.save(pkgLogger);
    }

    /**
     * 字索引入库
     *
     * @param dicType 辞典类型
     * @param srcList 字索引uri
     */
    private void saveWordsIndex(String dicType, List<String> srcList) {
        JSONArray jsonArray = new JSONArray();
        if (srcList != null && srcList.size() > 0) {
            String src = srcList.get(0).replace("${ref_path}/", refPath);
            String content = StringUtil.getContent(src);
            jsonArray = JSONArray.parseArray(content);
        }

        String tblWordsIndex = BaseDao.Collection.WORDSINDEX.getName() + "_" + dicType;
        if (mongoTemplate.collectionExists(tblWordsIndex)) {
            mongoTemplate.dropCollection(tblWordsIndex);
        }

        // 入库
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString(), JSONObject.class);
            List<Map<String,Object>> references = (List<Map<String,Object>>)jsonObject.get("references");
            for(int j = 0; j < references.size(); j++){
                WordsIndex wordsIndex = new WordsIndex();
                wordsIndex.setIdentifier(references.get(j).get("id").toString());
                wordsIndex.setTitle(references.get(j).get("title").toString());
                wordsIndex.setType(Integer.parseInt(references.get(j).get("type").toString()));
                wordsIndex.setWord(jsonObject.get("word").toString());
                mongoTemplate.save(wordsIndex, tblWordsIndex);
            }
        }
    }

    /**
     * 词语详情入库
     *
     * @param dicType       辞典类型
     * @param identifier    词语ID
     * @param content       详情内容
     * @param tblWordDetail 表名
     */
    private void saveWordDetail(String dicType, String identifier, String content, String tblWordDetail) {
        // 入库
        WordDetail wordDetail = new WordDetail();
        wordDetail.setIdentifier(identifier);
        wordDetail.setContent(content);
        mongoTemplate.save(wordDetail, tblWordDetail);
    }

    /**
     * 创建索引
     *
     * @param field 字段名
     * @param tblName
     */
    private void createIndex(String field, String tblName){
        // 创建词语详情索引
        IndexOperations io = mongoTemplate.indexOps(tblName);
        Index index = new Index();
        index.on(field, Sort.Direction.ASC);
        index.named(field + "_index");
        io.ensureIndex(index);
    }
}
