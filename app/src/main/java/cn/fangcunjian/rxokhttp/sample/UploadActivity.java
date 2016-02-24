package cn.fangcunjian.rxokhttp.sample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.fangcunjian.rxokhttp.BaseResponse;
import cn.fangcunjian.rxokhttp.HttpRequest;
import cn.fangcunjian.rxokhttp.ProgressEvent;
import cn.fangcunjian.rxokhttp.RequestParams;
import cn.fangcunjian.rxokhttp.RxBus;
import cn.fangcunjian.rxokhttp.sample.base.view.BaseActivity;
import cn.fangcunjian.rxokhttp.sample.http.model.UploadResponse;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.utils.ILogger;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import us.feras.mdv.MarkdownView;

/**
 * Create by Mcin on 16/2/23
 */
public class UploadActivity extends BaseActivity {

    @Bind(R.id.pb_upload)
    ProgressBar mPbUpload;
    @Bind(R.id.btn_upload)
    Button mBtnUpload;
    @Bind(R.id.mv_code)
    MarkdownView mMvCode;

    private RxBus mRxBus;
    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        setTitle("文件上传");

        mRxBus = RxBus.getDefault();
        mSubscriptions = new CompositeSubscription();

        mMvCode.loadMarkdownFile("file:///android_asset/Upload.md", "file:///android_asset/css-themes/classic.css");
    }

    @OnClick(R.id.btn_upload)
    public void upload() {
        GalleryFinal.openGallerySingle(0, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                uploadFile(new File(resultList.get(0).getPhotoPath()));
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    private void uploadFile(File file) {
        String userId = "3097424";
        RequestParams params = new RequestParams(this);
        params.addFormDataPart("file", file);
        params.addFormDataPart("userId", userId);
        params.addFormDataPart("token", "NTCrWFKFCn1r8iaV3K0fLz2gX9LZS1SR");
        params.addFormDataPart("udid", "f0ba33e4de8a657d");
        params.addFormDataPart("sign", "39abfa9af6f6e3c8776b01ae612bc14c");
        params.addFormDataPart("version", "2.1.0");
        params.addFormDataPart("mac", "8c:3a:e3:5e:68:e0");
        params.addFormDataPart("appId", "paojiao_aiyouyou20");
        params.addFormDataPart("imei", "359250051610200");
        params.addFormDataPart("model", "Nexus 5");
        params.addFormDataPart("cid", "paojiao");
        String fileuploadUri = "http://uploader.paojiao.cn/avatarAppUploader?userId=" + userId;

        HttpRequest.post(fileuploadUri, params, new BaseResponse<UploadResponse>(){})
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Observer<BaseResponse>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getBaseContext(), "Rxhttp上传成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), "上传失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {

                    }
                } );



        ConnectableObservable<Object> eventEmitter = mRxBus.toObserverable().publish();

        mSubscriptions//
                .add(eventEmitter.subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event instanceof ProgressEvent){
                            ProgressEvent progress = (ProgressEvent)event;
                            ILogger.d("## = " + String.valueOf(progress.getProgress()));
                            mPbUpload.setProgress(progress.getProgress());
                        }
                    }
                }));
        mSubscriptions.add(eventEmitter.connect());


    }
}
