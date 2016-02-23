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


import java.lang.reflect.Type;

import okhttp3.Headers;

/**
 * 返回数据相应基类
 * Create by Mcin on 16/02/22
 */
public class BaseResponse<T> {
    public static final int ERROR_RESPONSE_NULL = 1001;
    public static final int ERROR_RESPONSE_JSON_EXCEPTION = 1002;
    public static final int ERROR_RESPONSE_UNKNOWN = 1003;
    public static final int ERROR_RESPONSE_TIMEOUT = 1004;
    protected Type type;
    protected Headers headers;
    private  T data;

    public BaseResponse(){
        type = ClassTypeReflect.getModelClazz(getClass());
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
