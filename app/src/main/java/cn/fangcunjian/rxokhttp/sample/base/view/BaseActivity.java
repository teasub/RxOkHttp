/*
 *  Copyright 2015 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.fangcunjian.rxokhttp.sample.base.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import butterknife.ButterKnife;
import cn.fangcunjian.rxokhttp.HttpTaskHandler;
import cn.fangcunjian.rxokhttp.sample.Global;
import cn.fangcunjian.rxokhttp.sample.http.MyHttpCycleContext;
import cn.finalteam.toolsfinal.DeviceUtils;
import icepick.Icepick;

/**
 * Base class for Activities which already setup butterknife and icepick
 *
 * @author Hannes Dorfmann
 */
public class BaseActivity extends AppCompatActivity implements MyHttpCycleContext {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate( savedInstanceState );
        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
        Global.SCREEN_WIDTH = dm.widthPixels;
        Global.SCREEN_HEIGHT = dm.heightPixels;
        Icepick.restoreInstanceState( this, savedInstanceState );
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind( this );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        Icepick.saveInstanceState( this, outState );
    }

    protected void injectDependencies() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
