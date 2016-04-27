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


import android.text.TextUtils;

import net.fangcunjian.mosby.utils.JsonFormatUtils;
import net.fangcunjian.mosby.utils.StringUtils;
import net.fangcunjian.mosby.utils.gson.GsonQuick;
import net.fangcunjian.mosby.utils.logger.ILogger;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mcin on 15/12/18.
 * Rxjava封装Okhttp请求
 */
public class HttpTask<T> {

    public static final String DEFAULT_HTTP_TASK_KEY = "default_http_task_key";

    private String url;
    private RequestParams params;
    private Headers headers;
    private String requestKey;
    private Method method;
    private OkHttpClient okHttpClient;
    private BaseResponse date;


    HttpTask(Method method, String url, RequestParams params, BaseResponse date) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.date = date;
        if (params == null) {
            this.params = params = new RequestParams();
        }
        this.requestKey = params.getHttpTaskKey();
        if (StringUtils.isEmpty(requestKey)) {
            requestKey = DEFAULT_HTTP_TASK_KEY;
        }

        //将请求的URL及参数组合成一个唯一请求
        HttpTaskHandler.getInstance().addTask(this.requestKey, this);

        okHttpClient = RxOkHttp.getInstance().getOkHttpClient();

        if (params.headers != null) {
            headers = params.headers.build();
        }
    }





    /**
     * 请求网络
     * @return
     */
    private   Observable<ResponseData> doInBackground(){
        return Observable.create( new Observable.OnSubscribe<ResponseData>(){
            @Override
            public void call(Subscriber<? super ResponseData> subscriber) {
                Response response = null;
                ResponseData responseData = new ResponseData();
                try {
                    String srcUrl = url;
                    //构建请求Request实例
                    Request.Builder builder = new Request.Builder();

                    switch (method) {
                        case GET:
                            url = Utils.getFullUrl(url, params.getUrlParams(), params.isUrlEncoder());
                            builder.get();
                            break;
                        case DELETE:
                            url = Utils.getFullUrl(url, params.getUrlParams(), params.isUrlEncoder());
                            builder.delete();
                            break;
                        case HEAD:
                            url = Utils.getFullUrl(url, params.getUrlParams(), params.isUrlEncoder());
                            builder.head();
                            break;
                        case POST:
                            RequestBody body = params.getRequestBody();
                            if (body != null) {
                                builder.post(new ProgressRequestBody(body, HttpTask.this));
                            }
                            break;
                        case PUT:
                            RequestBody bodyPut = params.getRequestBody();
                            if (bodyPut != null) {
                                builder.put(new ProgressRequestBody(bodyPut, HttpTask.this));
                            }
                            break;
                        case PATCH:
                            RequestBody bodyPatch = params.getRequestBody();
                            if (bodyPatch != null) {
                                builder.put(new ProgressRequestBody(bodyPatch, HttpTask.this));
                            }
                            break;
                    }
                    builder.url(url).tag(srcUrl).headers(headers);
                    Request request = builder.build();
                    if (Configuration.DEBUG) {
                        ILogger.d("url=" + srcUrl + "?" + params.toString());
                    }
                    Call call = okHttpClient.newCall(request);
                    OkHttpCallManager.getInstance().addCall(url, call);
                    //执行请求
                    response = call.execute();
                } catch (Exception e) {
                    if (Configuration.DEBUG) {
                        ILogger.e("Exception=%s", e);
                    }
                    if (e instanceof SocketTimeoutException) {
                        responseData.setTimeout(true);
                    } else if (e instanceof InterruptedIOException && TextUtils.equals(e.getMessage(),
                            "timeout")) {
                        responseData.setTimeout(true);
                    }
                }

                //获取请求结果
                if (response != null) {
                    responseData.setResponseNull(false);
                    responseData.setCode(response.code());
                    responseData.setMessage(response.message());
                    responseData.setSuccess(response.isSuccessful());
                    String respBody = "";
                    try {
                        respBody = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    responseData.setResponse(respBody);
                    responseData.setHeaders(response.headers());
                } else {
                    responseData.setResponseNull(true);
                }

                subscriber.onNext(responseData);
                subscriber.onCompleted();
            }
        });

    }

    /**
     * 处理相应数据
     * @param responseData
     * @return
     */
    private Observable<BaseResponse> onPostExecute(final ResponseData responseData){
        return Observable.create( new Observable.OnSubscribe<BaseResponse>() {
            @Override
            public void call(Subscriber<? super BaseResponse> subscriber) {
                if (!responseData.isResponseNull()) {//请求得到响应
                    if (responseData.isSuccess()) {//成功的请求
                        String respBody = responseData.getResponse();
                        if (Configuration.DEBUG) {
                            ILogger.d("url=" + url + "\n result=" + JsonFormatUtils.formatJson(respBody));
                        }

                        if (StringUtils.isEmpty(respBody)) {
                            return ;
                        }

                        if (date.type == String.class) {
                            date.setHeaders(responseData.getHeaders());
                            date.setData(respBody);
                        }else {
                            Object obj = null;
                            try {
                                obj = GsonQuick.toObject(respBody, date.type);
                            } catch (Exception e) {
                                ILogger.e(e);
                            }
                            if (obj != null) {
                                date.setHeaders(responseData.getHeaders());
                                date.setData(obj);
                            }
                        }
                        subscriber.onNext(date);
                        subscriber.onCompleted();
                    } else {//请求失败
                        int code = responseData.getCode();
                        String msg = responseData.getMessage();
                        if (Configuration.DEBUG) {
                            ILogger.d("url=" + url + "\n response failure code=" + code + " msg=" + msg);
                        }
                        if (code == 504) {
                            subscriber.onError(new Exception(BaseResponse.ERROR_RESPONSE_TIMEOUT
                                    + "network error time out"));
                        } else {
                            subscriber.onError(new Exception(String.valueOf(code) + " " + msg));
                        }
                    }
                } else {//请求无响应
                    if (responseData.isTimeout()) {
                        subscriber.onError(new Exception(BaseResponse.ERROR_RESPONSE_TIMEOUT
                                + "network error time out"));
                    } else {
                        if (Configuration.DEBUG) {
                            ILogger.d("url=" + url + "\n response empty");
                        }
                        subscriber.onError(new Exception(BaseResponse.ERROR_RESPONSE_UNKNOWN
                                + "http exception"));
                    }
                }
            }
        } );
    }

    public Observable<BaseResponse> execute(){
        return doInBackground()
                .flatMap( new Func1<ResponseData, Observable<BaseResponse>>() {
                    @Override
                    public Observable<BaseResponse> call(ResponseData responseData) {
                        return onPostExecute(responseData);
                    }
                } )
                .subscribeOn( Schedulers.io());
    }


    public String getUrl() {
        return url;
    }


}
