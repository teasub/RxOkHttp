/*
 * Copyright (C) 2016 Mcin(teajson@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.fangcunjian.rxokhttp;

import android.text.TextUtils;

import net.fangcunjian.mosby.utils.logger.ILogger;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import cn.fangcunjian.rxokhttp.https.HttpsCerManager;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

/**
 * Create by Mcin on 16/2/22
 */
public class RxOkHttp {

    private OkHttpClient okHttpClient;

    private static RxOkHttp rxOkHttp;
    private RxOkHttpConfiguration configuration;

    private RxOkHttp() {
    }

    public synchronized void init(RxOkHttpConfiguration configuration) {
        this.configuration = configuration;

        long timeout = configuration.getTimeout();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(false)
                .followRedirects(true);
        if ( getHostnameVerifier() != null ) {
            builder.hostnameVerifier(configuration.getHostnameVerifier());
        }

        List<InputStream> certificateList = configuration.getCertificateList();
        if (certificateList != null && certificateList.size() > 0) {
            HttpsCerManager httpsCerManager = new HttpsCerManager(builder);
            httpsCerManager.setCertificates(certificateList);
        }

        CookieJar cookieJar = configuration.getCookieJar();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }

        if(configuration.getCache() != null) {
            builder.cache(configuration.getCache());
        }

        if (configuration.getAuthenticator() != null){
            builder.authenticator(configuration.getAuthenticator());
        }
        if (configuration.getCertificatePinner() != null) {
            builder.certificatePinner(configuration.getCertificatePinner());
        }
        builder.followRedirects(configuration.isFollowRedirects());
        builder.followSslRedirects(configuration.isFollowSslRedirects());
        builder.retryOnConnectionFailure(configuration.isRetryOnConnectionFailure());
        if (configuration.getNetworkInterceptorList() != null) {
            builder.networkInterceptors().addAll(configuration.getNetworkInterceptorList());
        }
        if (configuration.getInterceptorList() != null) {
            builder.interceptors().addAll(configuration.getInterceptorList());
        }

        if(configuration.getProxy() != null){
            builder.proxy(configuration.getProxy());
        }
        ILogger.DEBUG = configuration.isDebug();
        Configuration.DEBUG = configuration.isDebug();

        okHttpClient = builder.build();
    }

    public static RxOkHttp getInstance() {
        if (rxOkHttp == null) {
            rxOkHttp = new RxOkHttp();
        }
        return rxOkHttp;
    }

    /**
     * 修改公共请求参数信息
     * @param key
     * @param value
     */
    public void updateCommonParams(String key, String value) {
        List<Part> commonParams = configuration.getCommonParams();
        if (commonParams != null){
            for (Part param:commonParams) {
                if (param != null && TextUtils.equals(param.getKey(), key)){
                    param.setValue(value);
                    break;
                }
            }
        }
    }

    /**
     * 修改公共header信息
     * @param key
     * @param value
     */
    public void updateCommonHeader(String key, String value) {
        Headers headers = configuration.getCommonHeaders();
        if (headers != null) {
            configuration.commonHeaders = headers.newBuilder().set(key, value).build();
        }
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public List<Part> getCommonParams() {
        return configuration.getCommonParams();
    }

    public List<InputStream> getCertificateList() {
        return configuration.getCertificateList();
    }

    public HostnameVerifier getHostnameVerifier() {
        return configuration.getHostnameVerifier();
    }

    public long getTimeout() {
        return configuration.getTimeout();
    }

    public Headers getCommonHeaders() {
        return configuration.getCommonHeaders();
    }
}
