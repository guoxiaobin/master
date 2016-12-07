package com.nd.dictionary.modules.classic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nd.dictionary.internal.api.LCApi;
import com.nd.dictionary.internal.model.LCResource;
import com.nd.dictionary.internal.model.LCResourceList;
import com.nd.dictionary.internal.model.WordCard;
import com.nd.gaea.util.WafJsonMapper;
import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文言文词典Service
 * Created by yanguanyu on 2016/9/7.
 */
@Service
public class ClassicDictionaryService {

    private static final Logger logger = LoggerFactory.getLogger(ClassicDictionaryService.class);

    private static final String WORDCARD_REF_PATH_PREFIX = "${ref-path}/";

    @Value("${cs.product_url_prefix}")
    private String csProductUrlPrefix;
    @Value("${cs.product_static_url_prefix}")
    private String csProductStaticUrlPrefix;

    @Resource
    private LCApi lcApi;
    @Resource
    private LCApi productLCApi;

    public Object getAllClassicWordCardFromLC() {
        Map<String, String> result = new HashMap<>(2000);
        String limit = "(%d,%d)";
        Response<LCResourceList> response = null;
        try {
            response = lcApi.getClassicWordCardList(null, limit).execute();
        } catch (IOException e) {
            logger.error("lcApi getClassicWordCardList error ", e);
        }
        if (response == null || !response.isSuccessful()) {
            throw new RestfulException(ErrorCode.INTERNAL_SERVER_ERROR, "getAllClassicWordCardFromLC error");
        }
        LCResourceList lcResourceList = response.body();
        for (LCResource lcResource : lcResourceList.getItems()) {

        }
        return null;
    }

    private Map<String, Object> buildClassicWordCard(LCResource lcResource) throws IOException {
        String originalJson = WafJsonMapper.toJson(lcResource.getCustomProperties().get("original"));
        Map<String, Object> wordCard = WafJsonMapper.getMapper().readValue(originalJson,
                new TypeReference<Map<String, Object>>() {});

        wordCard.put("identifier", lcResource.getIdentifier());

        List<String> lcResourceTags = lcResource.getTags();
        List<Map<String, Object>> tagList = new ArrayList<>();
        if (lcResourceTags != null) {
            for (String tagStr : lcResourceTags) {
                Map<String, Object> tag = WafJsonMapper.getMapper().readValue(tagStr,
                        new TypeReference<Map<String, Object>>() {});
                tagList.add(tag);
            }
        }
        wordCard.put("tags", tagList);
        return wordCard;
    }

    public Map<String, List<Map<String, Object>>> fetchMultimediaInfo(String word) throws IOException {
        Map<String, List<Map<String, Object>>> multimedia = new HashMap<>();
        multimedia.put("picture_assets", new ArrayList<Map<String, Object>>());
        multimedia.put("video_assets", new ArrayList<Map<String, Object>>());
        String prop = String.format("title eq %s", word);
        String limit = "(0,1)";
        Response<LCResourceList> response = productLCApi.getNewWordCardList(prop, limit).execute();
        if (!response.isSuccessful()) {
            logger.error("get new word card error, {}", response.errorBody().toString());
        }
        LCResourceList lcResourceList = response.body();
        if (lcResourceList.getTotal() > 0) {
            LCResource lcResource = lcResourceList.getItems().get(0);
            String location = lcResource.getTechInfo().getHref().getLocation();
            parseNewWordCardRelations(location, multimedia);
        }
        return multimedia;
    }

    private void parseNewWordCardRelations(String location, Map<String, List<Map<String, Object>>> multimedia) throws IOException {
        // FIXME: 这里ref-path比较特殊不是用下划线；生字卡资源只有生产环境有
        location = location.replace(WORDCARD_REF_PATH_PREFIX, csProductStaticUrlPrefix);
        location = location.replace("main.xml", "resources/relations.json");
        String content = StringUtil.getContent(location);
        WordCard wordCard = WafJsonMapper.getMapper().readValue(content, WordCard.class);
        for(WordCard.SpellAsset spellAsset : wordCard.getSpellAssets()) {
            for (WordCard.MultimediaAsset pictureAsset : spellAsset.getSpellMultimediaPictureAssets()) {
                multimedia.get("picture_assets").add(buildMultimediaAsset(pictureAsset, spellAsset.getTarget().getTitle()));
            }
            for (WordCard.MultimediaAsset videoAsset : spellAsset.getSpellMultimediaVideoAssets()) {
                multimedia.get("video_assets").add(buildMultimediaAsset(videoAsset, spellAsset.getTarget().getTitle()));
            }
        }
    }

    private Map<String, Object> buildMultimediaAsset(WordCard.MultimediaAsset multimediaAsset, String targetSpell) {
        Map<String, Object> asset = new HashMap<>();
        asset.put("title", multimediaAsset.getTarget().getTitle());
        asset.put("format", multimediaAsset.getTarget().getFormat());
        String loc = multimediaAsset.getTarget().getLocation();
        asset.put("location", loc.replace(WORDCARD_REF_PATH_PREFIX, csProductStaticUrlPrefix));
        asset.put("target_spell", targetSpell);
        return asset;
    }

    public Object getWordIndexFromLC() {
        throw new UnsupportedOperationException();
    }

    public Object getSpellIndexFromLC() {
        throw new UnsupportedOperationException();
    }

    public Object getStrokeIndexFromLC() {
        throw new UnsupportedOperationException();
    }

    public Object getFunctionWordIndexFromLC() {
        throw new UnsupportedOperationException();
    }

    public Object getLittleKnowledgeFromLC() {
        throw new UnsupportedOperationException();
    }

    public Object getDiscriminationFromLC() {
        throw new UnsupportedOperationException();
    }

    public Object getHintFromLC() {
        throw new UnsupportedOperationException();
    }

    public Object getEditorialNote() {
        throw new UnsupportedOperationException();
    }

    public Object getIllustrateFromLC() {
        throw new UnsupportedOperationException();
    }
}
