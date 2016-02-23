package cn.fangcunjian.rxokhttp.sample.newgame;


import android.os.Bundle;
import android.widget.GridView;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.fangcunjian.rxokhttp.sample.AppContext;
import cn.fangcunjian.rxokhttp.sample.R;
import cn.fangcunjian.rxokhttp.sample.adapter.NewGameListAdapter;
import cn.fangcunjian.rxokhttp.sample.base.view.BaseLceFragment;
import cn.fangcunjian.rxokhttp.sample.http.model.GameInfo;
import cn.fangcunjian.rxokhttp.sample.widget.swipeview.SwipeRefreshLayout;
import cn.fangcunjian.rxokhttp.sample.widget.swipeview.SwipeRefreshLayoutDirection;
import us.feras.mdv.MarkdownView;

/**
 * Created by Mcin on 16/2/23.
 */
public class NewGameListFragment extends BaseLceFragment<SwipeRefreshLayout, List<GameInfo>, NewGameView, NewGamePresenter>
            implements NewGameView,SwipeRefreshLayout.OnRefreshListener {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Bind(R.id.gv_game)
    GridView mGvGame;
    NewGameListAdapter mNewGameListAdapter;
    List<GameInfo> mGameList;
    @Bind(R.id.contentView)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.mv_code)
    MarkdownView mMvCode;
    private int mPage = 1;

    private NewGameComponent mNewGameComponent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mNewGameComponent = DaggerNewGameComponent.builder()
                .appComponent( AppContext.getmComponent())
                .build();

    }

    @Override
    public void onNewViewStateInstance() {
        super.onNewViewStateInstance();

        mGameList = new ArrayList<>();
        mNewGameListAdapter = new NewGameListAdapter(this, mGameList);
        mGvGame.setAdapter(mNewGameListAdapter);
        mSwipeLayout.setDirection( SwipeRefreshLayoutDirection.BOTH);
        mSwipeLayout.setOnRefreshListener(this);
        mMvCode.loadMarkdownFile("file:///android_asset/HttpRequestCallbackBean.md", "file:///android_asset/css-themes/classic.css");

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_new_game_list;
    }

    @Override
    public LceViewState<List<GameInfo>, NewGameView> createViewState() {
        return new NewGameViewState();
    }

    @Override
    public List<GameInfo> getData() {
        return mGameList;
    }

    @Override
    protected String getErrorMessage(Throwable throwable, boolean b) {
        return null;
    }

    @Override
    public NewGamePresenter createPresenter() {
        return mNewGameComponent.presenter();
    }

    @Override
    public void setData(List<GameInfo> gameInfos) {

        mGameList.addAll(gameInfos);
        mNewGameListAdapter.notifyDataSetChanged();

        if (gameInfos != null && gameInfos.size() > 0) {
            mSwipeLayout.setDirection( SwipeRefreshLayoutDirection.BOTH);
        } else {
            mSwipeLayout.setDirection(SwipeRefreshLayoutDirection.TOP);
        }
    }

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public void loadData(boolean b) {
        presenter.requestData(mPage, this);
    }


    @Override
    public void onRefresh(SwipeRefreshLayoutDirection direction) {
        if ( direction == SwipeRefreshLayoutDirection.TOP ) {
            presenter.requestData(1, this);
        } else {
            presenter.requestData(mPage, this);
        }
    }


}
