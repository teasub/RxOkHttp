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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Http请求辅助类，用于处理UI已经销毁，但后台线程还在进行的问题；
 * Create by Mcin on 16/2/22
 */
public class HttpTaskHandler {

    /** 正在请求的任务集合 */
    private static Map<String, List<HttpTask>> httpTaskMap;
    private static Map<String, List<HttpTask>> rxHttpTaskMap;
    /** 单例请求处理器 */
    private static HttpTaskHandler httpTaskHandler = null;

    private HttpTaskHandler() {
        httpTaskMap = new ConcurrentHashMap<>();
        rxHttpTaskMap = new ConcurrentHashMap<>();
    }

    /**
     * 获得处理器实例
     */
    public static HttpTaskHandler getInstance() {
        if (httpTaskHandler == null) {
            httpTaskHandler = new HttpTaskHandler();
        }
        return httpTaskHandler;
    }



    /**
     * 移除KEY
     * @param key
     */
    public void removeTask(String key) {

        if (rxHttpTaskMap.containsKey(key)) {
            List<HttpTask> tasks = rxHttpTaskMap.get(key);
            if (tasks != null && tasks.size() > 0) {
                for (HttpTask task : tasks) {
                    HttpRequest.cancel(task.getUrl());
                    /*if (!task.isCancelled()) {
                        task.cancel(true);
                    }*/
                }
            }

            //移除对应的Key
            rxHttpTaskMap.remove(key);
        }
    }



    /**
     * 将请求放到Map里面
     * @param key
     * @param task
     */
    public void addTask(String key, HttpTask task) {
        if (rxHttpTaskMap.containsKey(key)) {
            List<HttpTask> tasks = rxHttpTaskMap.get(key);
            tasks.add(task);
            rxHttpTaskMap.put(key, tasks);
        } else {
            List<HttpTask> tasks = new ArrayList<>();
            tasks.add(task);
            rxHttpTaskMap.put(key, tasks);
        }
    }

    /**
     * 判断是否存在
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return httpTaskMap.containsKey(key);
    }

    /**
     * 判断是否存在
     * @param key
     * @return
     */
    public boolean containsRx(String key) {
        return rxHttpTaskMap.containsKey(key);
    }
}
