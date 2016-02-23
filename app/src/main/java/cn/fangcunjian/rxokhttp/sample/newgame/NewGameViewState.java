package cn.fangcunjian.rxokhttp.sample.newgame;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import java.util.List;

import cn.fangcunjian.rxokhttp.sample.http.model.GameInfo;

/**
 * Created by Mcin on 16/2/23.
 */
public class NewGameViewState implements LceViewState<List<GameInfo>,NewGameView> {

    @Override
    public void setStateShowContent(List<GameInfo> gameInfos) {

    }

    @Override
    public void setStateShowError(Throwable throwable, boolean b) {

    }

    @Override
    public void setStateShowLoading(boolean b) {

    }

    @Override
    public void apply(NewGameView newGameView, boolean b) {

    }
}
