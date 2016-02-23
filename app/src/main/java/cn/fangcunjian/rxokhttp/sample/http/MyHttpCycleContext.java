package cn.fangcunjian.rxokhttp.sample.http;

import android.content.Context;
import cn.fangcunjian.rxokhttp.HttpCycleContext;

/**
 * Create by Mcin on 16/2/23
 */
public interface MyHttpCycleContext extends HttpCycleContext{
     Context getContext();
}
