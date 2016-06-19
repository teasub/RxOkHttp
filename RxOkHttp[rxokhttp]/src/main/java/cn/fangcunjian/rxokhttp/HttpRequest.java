/*
 * Copyright (C) 2016 Mcin(teajosn@gmail.com), Inc.
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

import net.fangcunjian.mosby.utils.StringUtils;

import java.io.File;

import okhttp3.Call;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * http请求类
 * Create by Mcin on 16/2/22
 */
public class HttpRequest {

    public static Observable<BaseResponse> get(String url) {
        return get(url, null, null);
    }

    public static Observable<BaseResponse> get(String url, RequestParams params) {
        return get(url, params, null);
    }

    public static Observable<BaseResponse> get(String url, BaseResponse response) {
        return get(url, null, response);
    }


    /**
     * Get请求
     * @param url
     * @param params
     * @param response 返回相应构造
     * @return
     */
    public static Observable<BaseResponse> get(String url, RequestParams params, BaseResponse response) {
        return executeRequest(Method.GET, url, params, response);
    }

    public static Observable<BaseResponse> post(String url) {
        return post(url, null, null);
    }

    public static Observable<BaseResponse> post(String url, RequestParams params) {
        return post(url, params, null);
    }

    public static Observable<BaseResponse> post(String url, BaseResponse response) {
        return post(url, null, response);
    }

    /**
     * Post请求
     * @param url
     * @param params
     * @param response
     */
    public static Observable<BaseResponse> post(String url, RequestParams params, BaseResponse response) {
        return executeRequest(Method.POST, url, params, response);
    }

    public static Observable<BaseResponse> put(String url) {
        return put(url, null, null);
    }

    public static Observable<BaseResponse> put(String url, RequestParams params) {
        return put(url, params, null);
    }

    public static Observable<BaseResponse> put(String url, BaseResponse response) {
        return put(url, null, response);
    }

    /**
     * put请求
     * @param url
     * @param params
     * @param response
     */
    public static Observable<BaseResponse>  put(String url, RequestParams params, BaseResponse response) {
        return executeRequest(Method.PUT, url, params, response);
    }

    public static Observable<BaseResponse> delete(String url) {
        return delete(url, null, null);
    }

    public static Observable<BaseResponse> delete(String url, RequestParams params) {
        return delete(url, params, null);
    }

    public static Observable<BaseResponse> delete(String url, BaseResponse response) {
        return delete(url, null, response);
    }

    /**
     * delete请求
     * @param url
     * @param params
     * @param response
     * @return
     */
    public static Observable<BaseResponse> delete(String url, RequestParams params, BaseResponse response) {
        return executeRequest(Method.DELETE, url, params, response);
    }

    public static Observable<BaseResponse> head(String url) {
        return head(url, null, null);
    }

    public static Observable<BaseResponse> head(String url, RequestParams params) {
        return head(url, params, null);
    }

    public static Observable<BaseResponse> head(String url, BaseResponse response) {
        return head(url, null, response);
    }

    /**
     * head请求
     * @param url
     * @param params
     * @param response
     * @return
     */
    public static Observable<BaseResponse> head(String url, RequestParams params, BaseResponse response) {
        return executeRequest(Method.HEAD, url, params, response);
    }

    public static Observable<BaseResponse> patch(String url) {
        return patch(url, null, null);
    }

    public static Observable<BaseResponse> patch(String url, RequestParams params) {
        return patch(url, params, null);
    }

    public static Observable<BaseResponse> patch(String url, BaseResponse response) {
       return patch(url, null, response);
    }

    /**
     * patch请求
     * @param url
     * @param params
     * @param response
     * @return
     */
    public static Observable<BaseResponse> patch(String url, RequestParams params, BaseResponse response) {
        return executeRequest(Method.PATCH, url, params, response);
    }

    /**
     * 取消请求
     * @param url
     */
    public static void cancel(String url) {
        if ( !StringUtils.isEmpty(url) ) {
            Call call = OkHttpCallManager.getInstance().getCall(url);
            if ( call != null ) {
                call.cancel();
            }
            OkHttpCallManager.getInstance().removeCall(url);
        }
    }




    /**
     * 下载文件
     * @param url
     * @param target 保存的文件
     * @return
     */
    public static PublishSubject<ProgressEvent> download(String url, File target) {
        if (!StringUtils.isEmpty(url) && target != null) {
            FileDownloadTask task = new FileDownloadTask(url, target);
            return task.fileDonwload();
        }
        return null;
    }


    private static Observable<BaseResponse> executeRequest(Method method, String url, RequestParams params, BaseResponse response) {
        if (!StringUtils.isEmpty(url)) {
            HttpTask httpTask = new HttpTask(method, url, params, response);
            return httpTask.execute();
        }
        return null;
    }

}
