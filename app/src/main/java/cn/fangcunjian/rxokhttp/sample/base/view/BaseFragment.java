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

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import butterknife.ButterKnife;
import cn.fangcunjian.rxokhttp.sample.Global;
import cn.fangcunjian.rxokhttp.sample.http.MyHttpCycleContext;
import cn.finalteam.toolsfinal.DeviceUtils;
import icepick.Icepick;

/**
 * Base Fragment for this app that uses Butterknife, Icepick and dependency injection
 *
 * @author Hannes Dorfmann
 */
public abstract class BaseFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpFragment<V, P> implements MyHttpCycleContext {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @LayoutRes protected abstract int getLayoutRes();

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    Icepick.restoreInstanceState(this, savedInstanceState);
      DisplayMetrics dm = DeviceUtils.getScreenPix(getActivity());
      Global.SCREEN_WIDTH = dm.widthPixels;
      Global.SCREEN_HEIGHT = dm.heightPixels;
    return inflater.inflate(getLayoutRes(), container, false);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Icepick.saveInstanceState(this, outState);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    injectDependencies();
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public P createPresenter() {
        return null;
    }

    /**
   * Inject the dependencies
   */
  protected void injectDependencies() {

  }

}
