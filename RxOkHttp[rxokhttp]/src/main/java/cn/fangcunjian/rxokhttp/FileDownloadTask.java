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

import net.fangcunjian.mosby.utils.StringUtils;
import net.fangcunjian.mosby.utils.io.FileUtils;
import net.fangcunjian.mosby.utils.logger.ILogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Create by Mcin on 16/2/22
 */
public class FileDownloadTask {

    private OkHttpClient okHttpClient;
    private String url;
    private File target;
    //开始下载时间，用户计算加载速度
    private long previousTime;

    private PublishSubject<ProgressEvent> mDownloadProgress = PublishSubject.create();

    public FileDownloadTask(String url, File target) {
        this.url = url;
        this.okHttpClient = RxOkHttp.getInstance().getOkHttpClient();
        this.target = target;


        ILogger.d("addres2= " + mDownloadProgress.toString());
        FileUtils.mkdirs( target.getParentFile() );
        if (target.exists()) {
            target.delete();
        }
    }


    public PublishSubject<ProgressEvent> fileDonwload() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //构造请求
                final Request request = new Request.Builder()
                        .url( url )
                        .build();

                try {
                    previousTime = System.currentTimeMillis();
                    Response response = okHttpClient.newCall( request ).execute();
                    String result = saveFile( response);
                    if (!StringUtils.isEmpty(result)){
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError( e );
                }
            }
        } )
                .onBackpressureDrop()
                .subscribeOn( Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ILogger.d("download commplete = " + s);
                    }
                });

        return this.mDownloadProgress;

    }

    protected void onProgressUpdate(long[] values) {
        if (values != null && values.length >= 2) {
            long sum = values[0];
            long total = values[1];

            int progress = (int) (sum * 100.0f / total);
//            ILogger.d("进度=" + progress);
            //计算下载速度
            long totalTime = (System.currentTimeMillis() - previousTime) / 1000;
            if (totalTime == 0) {
                totalTime += 1;
            }
            long networkSpeed = sum / totalTime;
            mDownloadProgress.onNext(new ProgressEvent(networkSpeed, progress, false));
        }
    }



    private String saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[4098];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            FileUtils.mkdirs( target.getParentFile() );

            fos = new FileOutputStream( target );
            long[] value = new long[2];
            while ((len = is.read( buf )) != -1) {
                sum += len;
                fos.write( buf, 0, len );
                value[0] = sum;
                value[1] = total;
                onProgressUpdate(value);
            }
            fos.flush();
            mDownloadProgress.onCompleted();
            return target.getAbsolutePath();
        } catch (Exception e){
            mDownloadProgress.onError(e);
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                mDownloadProgress.onError(e);
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                mDownloadProgress.onError(e);
            }
        }
        return null;
    }
}
