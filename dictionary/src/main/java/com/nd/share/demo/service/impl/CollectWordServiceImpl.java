package com.nd.share.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Messages;
import com.nd.share.demo.domain.CollectWord;
import com.nd.share.demo.domain.WordDetail;
import com.nd.share.demo.domain.WordsIndex;
import com.nd.share.demo.repository.dao.CollectWordDao;
import com.nd.share.demo.repository.dao.WordDetailDao;
import com.nd.share.demo.repository.dao.WordsIndexDao;
import com.nd.share.demo.service.CollectWordService;
import com.nd.share.demo.service.Entity.Word.Spells;
import com.nd.share.demo.service.Entity.Word.Word;
import com.nd.share.demo.service.Entity.collectWords.CollectWordOutPut;
import com.nd.share.demo.service.Entity.collectWords.CollectWordSpell;
import com.nd.share.demo.service.WordsInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 收藏词语
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Service
public class CollectWordServiceImpl implements CollectWordService {

    @Resource
    private CollectWordDao collectWordDao;
    @Resource
    private WordsInfoService wordsInfoService;

    @Resource
    private WordsIndexDao wordsIndexDao;
    @Resource
    private WordDetailDao wordDetailDao;

    /**
     * 收藏词语
     *
     * @param collectWord
     */
    @Override
    public int save(CollectWord collectWord) {
        long count = collectWordDao.findCollectWordCount(collectWord);

        // 词语未被收藏
        if (count == 0) {
            try {
                if (collectWord.getCharacter() == null || collectWord.getSpells() == null || collectWord.getSpells().size() == 0) {
                    List<String> ids = new ArrayList<String>();
                    ids.add(collectWord.getIdentifier());

                    List<Word> words = wordsInfoService.getListLCById(ids);

                    if (words.size() != 0) {
                        Word word = words.get(0);
                        String wordSpell = word.getSpells().get(0).getTitle();

                        String spellindex = getSpellIndex(wordSpell);
                        collectWord.setSpellindex(spellindex);
                        collectWord.setCharacter(words.get(0).getTitle());
                    } else {
                        return -1;
                    }
                } else {
                    String wordSpell = collectWord.getSpells().get(0).getSpell();
                    collectWord.setSpellindex(getSpellIndex(wordSpell));
                }

                collectWordDao.updateCollectWorks(collectWord);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("CollectWordServiceImpl, msg:%s", e.getMessage()));
            }
        } else {//词语已被收藏
            return 0;
        }
    }

    /**
     * 获取用户收藏字词
     *
     * @param timeStamp 客户端时间戳
     * @param userId    第三方用户编号
     * @param entrance  平台标识
     * @return
     */
    @Override
    public List<CollectWordOutPut> getCollectWords(String timeStamp, String userId, String entrance) {
        Date date = new Date();
        List<CollectWord> collectWords = new ArrayList<>();
        List<CollectWordOutPut> outputlist = new ArrayList<CollectWordOutPut>();

        try {
            long time = Long.parseLong(timeStamp);
            date.setTime(time);
            collectWords = collectWordDao.getCollectWorks(date, userId, entrance);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestfulException(ErrorCode.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR, String.format("CollectWordServiceImpl, msg:%s", e.getMessage()));
        }

        Map<String, Word> wordMaps = new HashMap<>();
        try {
            for (CollectWord c : collectWords) {
                List<WordDetail> wordDetailList = wordDetailDao.getWordDetailList(c.getIdentifier());
                if (wordDetailList != null && wordDetailList.size() > 0) {
                    Word word = JSONObject.parseObject(wordDetailList.get(0).getContent(), Word.class);
                    wordMaps.put(c.getIdentifier(), word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestfulException(ErrorCode.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR, String.format("CollectWordServiceImpl, msg:%s", e.getMessage()));
        }

        for (CollectWord i : collectWords) {
            CollectWordOutPut output = new CollectWordOutPut();
            // 获取词语详情
            Word word = wordMaps.get(i.getIdentifier());

            List<WordsIndex> wordsIndexList = wordsIndexDao.searchWordsIndex(i);
            // 词语释义
            StringBuffer explain = new StringBuffer();
            // 词语类型
            int type = 0;

            if(wordsIndexList == null){
                continue;
            }

            try {
                // 获取词语释义
                for (int j = 0; j < wordsIndexList.size(); j++) {
                    explain.append(wordsIndexList.get(j).getTitle());
                }
                // 获取词语类型
                for (int j = 0; j < wordsIndexList.size(); j++) {
                    WordsIndex wordsIndex = wordsIndexList.get(j);
                    if (wordsIndex.getTitle().equals(i.getCharacter())
                            && wordsIndex.getIdentifier().equals(i.getIdentifier())
                            && wordsIndex.getType() != 4) {
                        type = wordsIndex.getType();
                        break;
                    }
                }
                output.setExplain(explain.toString());
                output.setType(type);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestfulException(ErrorCode.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR, String.format("CollectWordServiceImpl, msg:%s", e.getMessage()));
            }

            output.setIdentifier(i.getIdentifier());
            output.setCreateTime(i.getCreateTime().getTime());
            String ch = i.getCharacter();
            try {
                if (ch != null && !ch.equals("") && i.getSpells() != null && i.getSpells().size() != 0) {
                    output.setCharacter(ch);
                    output.setSpells(i.getSpells());
                    output.setSpellindex(i.getSpellindex());
                } else {
                    output.setCharacter(word.getTitle());
                    output.setSpellindex(i.getSpellindex());
                    List<CollectWordSpell> cwspells = new ArrayList<CollectWordSpell>();
                    for (Spells j : word.getSpells()) {
                        CollectWordSpell cwspell = new CollectWordSpell();
                        cwspell.setPronounce(j.getMp3());
                        cwspell.setSpell(j.getTitle());
                        cwspells.add(cwspell);
                    }
                    output.setSpells(cwspells);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RestfulException(ErrorCode.INTERNAL_SERVER_ERROR, Messages.INTERNAL_SERVER_ERROR, String.format("CollectWordServiceImpl, msg:%s", e.getMessage()));
            }
            outputlist.add(output);
        }

        return outputlist;
    }

    /**
     * 根据读音获取首字母
     *
     * @param spell
     * @return
     */
    public String getSpellIndex(String spell) {
        String firstletter = spell.substring(0, 1);
        List<String> aGroup = new ArrayList<String>();
        List<String> oGroup = new ArrayList<String>();
        List<String> eGroup = new ArrayList<String>();

        aGroup.add("ā");
        aGroup.add("á");
        aGroup.add("ǎ");
        aGroup.add("à");
        aGroup.add("a");

        oGroup.add("o");
        oGroup.add("ō");
        oGroup.add("ó");
        oGroup.add("ǒ");
        oGroup.add("ò");

        eGroup.add("e");
        eGroup.add("ē");
        eGroup.add("é");
        eGroup.add("ě");
        eGroup.add("è");

        if (aGroup.contains(firstletter)) {
            return "A";
        } else if (oGroup.contains(firstletter)) {
            return "O";
        } else if (eGroup.contains(firstletter)) {
            return "E";
        } else {
            return firstletter.toUpperCase();
        }


    }

    /**
     * 获取词语是否已被收藏(0未收藏1已收藏)
     */
    @Override
    public int isCollectionWordExist(String identifier, String userId) {
        CollectWord collectWord = new CollectWord();
        collectWord.setIdentifier(identifier);
        collectWord.setUserId(userId);
        long count = collectWordDao.findCollectWordCount(collectWord);
        if (count == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 取消收藏
     *
     * @param identifier 词语uuid
     * @param userId     用户id
     * @return
     */
    @Override
    public int cancelCollection(String identifier, String userId) {
        CollectWord collectWord = new CollectWord();
        collectWord.setIdentifier(identifier);
        collectWord.setUserId(userId);
        int count = collectWordDao.cancelCollection(collectWord);
        // 成功
        if (count == 0) {
            return 0;
        } else if (count == 1) {
            // 失败
            return 1;
        } else {
            // 未收藏
            return 2;
        }
    }
}
