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

import android.app.Application;

import net.fangcunjian.mosby.utils.logger.ILogger;

import java.util.ArrayList;
import java.util.List;

import cn.fangcunjian.rxokhttp.Part;
import cn.fangcunjian.rxokhttp.RxOkHttp;
import cn.fangcunjian.rxokhttp.RxOkHttpConfiguration;
import cn.fangcunjian.rxokhttp.sample.dagger.AppComponent;
import cn.fangcunjian.rxokhttp.sample.dagger.DaggerAppComponent;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import okhttp3.Headers;

/**
 * Create by Mcin on 16/2/22
 */
public class AppContext extends Application {

    private static AppComponent mComponent;

    public static Application  mApp;


    @Override
    public void onCreate() {
        super.onCreate();
        ILogger.DEBUG = BuildConfig.DEBUG;

        mComponent = DaggerAppComponent.create();
        mApp = this;


        initRxOkHttp();

        initGalleryFinal();
    }

    private void initRxOkHttp() {

        List<Part> commomParams = new ArrayList<>();
        Headers commonHeaders = new Headers.Builder().build();

        RxOkHttpConfiguration.Builder builder = new RxOkHttpConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(Constants.REQ_TIMEOUT)
                        //.setCookieJar(CookieJar.NO_COOKIES)
                        //.setCertificates(...)
                        //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
                .setDebug(true);
        RxOkHttp.getInstance().init(builder.build());
    }


    private void initGalleryFinal() {
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
        .build();

        //配置imageloader
        ImageLoader imageloader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, ThemeConfig.CYAN)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    public static AppComponent getmComponent() {
        return mComponent;
    }
}
