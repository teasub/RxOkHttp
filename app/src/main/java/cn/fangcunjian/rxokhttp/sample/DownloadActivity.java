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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.fangcunjian.rxokhttp.HttpRequest;
import cn.fangcunjian.rxokhttp.ProgressEvent;
import cn.fangcunjian.rxokhttp.sample.base.view.BaseActivity;
import cn.finalteam.galleryfinal.utils.ILogger;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import us.feras.mdv.MarkdownView;

/**
 * Create by Mcin on 16/2/23
 */
public class DownloadActivity extends BaseActivity {

    @Bind(R.id.pb_download) ProgressBar mPbDownload;
    @Bind(R.id.btn_download) Button mBtnDownload;
    @Bind(R.id.mv_code) MarkdownView mMvCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        setTitle("简单文件下载");

        mMvCode.loadMarkdownFile("file:///android_asset/Download.md", "file:///android_asset/css-themes/classic.css");


    }

    @OnClick(R.id.btn_download)
    public void download() {
        mBtnDownload.setEnabled(false);
        String url = "http://www.91just.cn/upload/wordaily/apk/wordaily_1.1.036_20160519__360_release_signed_7zip_signed_Aligned.encrypted_signed_Aligned.apk";
        HttpRequest.download(url,new File("/sdcard/rootexplorer_140220.apk"))
                .distinct()
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Observer<ProgressEvent>() {
                    @Override
                    public void onCompleted() {
                        ILogger.d("下载完成");
                        mBtnDownload.setEnabled(true);
                        Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ILogger.d("失败" + e.toString());
                        mBtnDownload.setEnabled(true);
                        Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ProgressEvent progressEvent) {
                        ILogger.d("下载进度：" + progressEvent.getProgress() + "下载级难度=" + progressEvent.getNetworkSpeed());
                        mPbDownload.setProgress(progressEvent.getProgress());

                    }
                } );
    }
}
