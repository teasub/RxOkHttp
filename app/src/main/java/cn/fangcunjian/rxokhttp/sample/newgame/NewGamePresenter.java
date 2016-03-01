package cn.fangcunjian.rxokhttp.sample.newgame;

import android.support.annotation.Nullable;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import cn.fangcunjian.rxokhttp.BaseResponse;
import cn.fangcunjian.rxokhttp.HttpRequest;
import cn.fangcunjian.rxokhttp.RequestParams;
import cn.fangcunjian.rxokhttp.sample.http.Api;
import cn.fangcunjian.rxokhttp.sample.http.MyHttpCycleContext;
import cn.fangcunjian.rxokhttp.sample.http.model.GameInfo;
import cn.fangcunjian.rxokhttp.sample.http.model.NewGameResponse;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by Mcin on 16/1/22.
 */
public class NewGamePresenter extends MvpBasePresenter<NewGameView> {

    private final static  String TAG = NewGamePresenter.class.getSimpleName();
    @Inject
    public NewGamePresenter(){}

    public void requestData(final int page, MyHttpCycleContext context) {
        RequestParams params = new RequestParams(context);
        params.addFormDataPart("page", page);
        params.addFormDataPart("limit", 12);
        HttpRequest.get( Api.NEW_GAME, params, new BaseResponse<NewGameResponse>(){})
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {
                        getView().showContent();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e,true);
                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        NewGameResponse gameResponse = (NewGameResponse) response.getData();
                        if (null != gameResponse && null != gameResponse.getData()){
                            List<GameInfo> lists =gameResponse.getData();
                            getView().setData(lists);

                        }else {
                            getView().showError(new Exception(gameResponse.getMsg()),true);
                        }
                    }
                } );

    }

    @Override
    public void attachView(NewGameView view) {
        super.attachView( view );
    }

    @Nullable
    @Override
    public NewGameView getView() {
        return super.getView();
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView( retainInstance );
    }
}
