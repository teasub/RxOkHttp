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

package cn.fangcunjian.rxokhttp.sample;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fangcunjian.rxokhttp.BaseResponse;
import cn.fangcunjian.rxokhttp.HttpRequest;
import cn.fangcunjian.rxokhttp.RequestParams;
import cn.fangcunjian.rxokhttp.StringResponse;
import cn.fangcunjian.rxokhttp.sample.base.view.BaseActivity;
import cn.fangcunjian.rxokhttp.sample.http.Api;
import cn.finalteam.galleryfinal.utils.ILogger;
import cn.finalteam.toolsfinal.JsonFormatUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import us.feras.mdv.MarkdownView;

/**
 * Create by Mcin on 16/2/23
 */
public class HttpRequestCallbackStringActivity extends BaseActivity {

    @Bind(R.id.mv_code)
    MarkdownView mMvCode;
    @Bind(R.id.tv_result)
    TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_request_callback);
        ButterKnife.bind(this);

        setTitle("接口String回调");

        RequestParams params = new RequestParams(this);
        params.addFormDataPart("page", 1);
        params.addFormDataPart("limit", 12);
        HttpRequest.post( Api.NEW_GAME, params, new StringResponse())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Observer<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        ILogger.d(response.getData().toString());
                        mTvResult.setText(JsonFormatUtils.formatJson((String) response.getData()));
                    }
                } );

        mMvCode.loadMarkdownFile("file:///android_asset/HttpRequestCallbackString.md", "file:///android_asset/css-themes/classic.css");
    }
}
