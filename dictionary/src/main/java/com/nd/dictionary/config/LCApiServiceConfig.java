package com.nd.dictionary.config;

import com.nd.dictionary.internal.api.LCApi;
import com.nd.gaea.client.http.BearerAuthorizationProvider;
import com.nd.gaea.util.WafJsonMapper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Resource;
import java.io.IOException;

/**
 *
 * Created by yanguanyu on 2016/9/1.
 */

@Configuration
public class LCApiServiceConfig {

    @Resource
    private BearerAuthorizationProvider bearerAuthorizationProvider;

    @Value("${lc.url_prefix}")
    private String lcUrlPrefix;
    @Value("${lc.product_url_prefix}")
    private String lcProductUrlPrefix;

    @Bean
    public OkHttpClient lcOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(logging);
        builder.addInterceptor(new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("Content-Type", "application/json");
                if (bearerAuthorizationProvider!=null) {
                    builder.addHeader(org.apache.http.HttpHeaders.AUTHORIZATION, bearerAuthorizationProvider.getAuthorization());
                    if (!StringUtils.isEmpty(bearerAuthorizationProvider.getUserid())) {
                        builder.addHeader(BearerAuthorizationProvider.USERID, bearerAuthorizationProvider.getUserid());
                    }
                }
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        });
        return builder.build();
    }


    @Bean
    public Retrofit lcRetrofit(@Qualifier("lcOkHttpClient") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(lcUrlPrefix)
                .addConverterFactory(JacksonConverterFactory.create(WafJsonMapper.getMapper()))
                .client(okHttpClient)
                .build();
    }

    @Bean
    public LCApi lcApi(@Qualifier("lcRetrofit") Retrofit lcRetrofit) {
        return lcRetrofit.create(LCApi.class);
    }

    @Bean
    public Retrofit productLCRetrofit(@Qualifier("lcOkHttpClient") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(lcProductUrlPrefix)
                .addConverterFactory(JacksonConverterFactory.create(WafJsonMapper.getMapper()))
                .client(okHttpClient)
                .build();
    }

    @Bean
    public LCApi productLCApi(@Qualifier("productLCRetrofit") Retrofit lcRetrofit) {
        return lcRetrofit.create(LCApi.class);
    }
}
