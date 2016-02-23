/*
 * Copyright (C) 2016 Mcin(Mcin@gmail.com), Inc.
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

import butterknife.Bind;
import cn.fangcunjian.rxokhttp.sample.base.view.BaseActivity;
import us.feras.mdv.MarkdownView;

/**
 * Create by Mcin on 16/2/23
 */
public class OtherFuncationActivity extends BaseActivity {

    @Bind(R.id.mv_code)
    MarkdownView mMvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_function);

        mMvCode.loadMarkdownFile("file:///android_asset/OtherFuncation.md", "file:///android_asset/css-themes/classic.css");
    }
}
