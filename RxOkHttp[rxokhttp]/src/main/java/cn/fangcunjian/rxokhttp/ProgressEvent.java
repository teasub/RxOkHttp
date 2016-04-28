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

/**
 * Created by churchull on 16/2/21.
 */
public class ProgressEvent {

    private long networkSpeed ;
    private int progress ;
    private boolean done ;

    public ProgressEvent(long networkSpeed, int progress, boolean done) {
        this.networkSpeed = networkSpeed;
        this.progress = progress;
        this.done = done;
    }


    public long getNetworkSpeed() {
        return networkSpeed;
    }

    public void setNetworkSpeed(long networkSpeed) {
        this.networkSpeed = networkSpeed;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
