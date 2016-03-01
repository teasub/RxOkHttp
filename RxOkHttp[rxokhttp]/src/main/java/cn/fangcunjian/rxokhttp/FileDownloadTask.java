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

import net.fangcunjian.mosby.utils.io.FileUtils;

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
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Create by Mcin on 16/2/22
 */
public class FileDownloadTask {

    private OkHttpClient okHttpClient;
    private String url;
    private File target;
    //开始下载时间，用户计算加载速度
    private long previousTime;

    public FileDownloadTask(String url, File target) {
        this.url = url;
        this.okHttpClient = RxOkHttp.getInstance().getOkHttpClient();
        this.target = target;

        FileUtils.mkdirs( target.getParentFile() );
        if (target.exists()) {
            target.delete();
        }
    }


    protected Observable<ProgressEvent> fileDonwload() {
        return Observable.create( new Observable.OnSubscribe<ProgressEvent>() {
            @Override
            public void call(Subscriber<? super ProgressEvent> subscriber) {
                //构造请求
                final Request request = new Request.Builder()
                        .url( url )
                        .build();

                try {
                    Response response = okHttpClient.newCall( request ).execute();
                    long total = response.body().contentLength();
                    saveFile( response, subscriber);
                    if (total == target.length()) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError( e );
                }
            }
        } )
                .doOnSubscribe( new Action0() {
                    @Override
                    public void call() {
                        previousTime = System.currentTimeMillis();
                    }
                } )
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread() );
    }



    protected void onProgressUpdate(long[] values, Subscriber<? super ProgressEvent> subscriber) {
        if (values != null && values.length >= 2) {
            long sum = values[0];
            long total = values[1];

            int progress = (int) (sum * 100.0f / total);
            //计算下载速度
            long totalTime = (System.currentTimeMillis() - previousTime) / 1000;
            if (totalTime == 0) {
                totalTime += 1;
            }
            long networkSpeed = sum / totalTime;
            subscriber.onNext(new ProgressEvent((long)progress, (int)networkSpeed, false));

        }
    }



    public String saveFile(Response response, Subscriber<? super ProgressEvent> subscriber) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
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
                onProgressUpdate(value, subscriber);
            }
            fos.flush();
            subscriber.onCompleted();
            return target.getAbsolutePath();
        } catch (Exception e){
            subscriber.onError(e);
        }finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                subscriber.onError(e);
            }
        }
        return null;
    }
}
