package com.nd.dictionary.internal.api;

import com.nd.dictionary.internal.model.CSSession;
import com.nd.dictionary.internal.model.LCResource;
import com.nd.dictionary.internal.model.LCResourceList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

/**
 * LC api
 * Created by yanguanyu on 2016/9/1.
 */
public interface LCApi {

    //文言文字卡
    @GET("coursewareobjects/management/actions/query?include=TI,CG&coverage=Org/nd/&words=&category=$RA0504")
    Call<LCResourceList> getClassicWordCardList(@Query("prop") String prop, @Query("limit") String limit);

    //LC 生字卡
    @GET("coursewareobjects/management/actions/query?include=TI,LC,EDU,CG,CR&coverage=RSD/workspace/ASSEMBLE&words=&category=$RT0301")
    Call<LCResourceList> getNewWordCardList(@Query("prop") String prop, @Query("limit") String limit);

    //获取单个课件颗粒资源
    @GET("coursewareobjects/{id}?include=TI,CG&coverage=Org/nd/")
    Call<LCResource> getCourseWareObjectById(@Path("id") String id);

    //批量获取课件颗粒资源
    @GET("coursewareobjects/list?include=TI,CG&coverage=Org/nd/")
    Call<Map<String, LCResource>> getCourseWareObjectsByIdList(@Query("rid") List<String> idList);

    //获取资源素材
    @GET("assets/management/actions/query?include=TI,CG&coverage=Org/nd/&words=")
    Call<LCResourceList> getAssetList(@Query("category") String category, @Query("limit") String limit);

    //获取习题
    @GET("questions/management/actions/query?include=TI,LC,EDU,CG,CR&coverage=Org/rjs_dictionary/OWNER&words=")
    Call<LCResourceList> getQuestionList(@Query("category") String category, @Query("prop") String prop, @Query("limit") String limit);

    //获取上传资源的cs session
    @GET("{res_type}/none/uploadurl?uid=686872&renew=false&coverage=Org/nd/")
    Call<CSSession> getCSSession(@Path("res_type") String resType);
}
