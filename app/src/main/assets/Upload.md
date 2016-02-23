##上传文件
<pre>
File file = new File("/sdcard/DCIM/GalleryFinal/IMG20151201200821.jpg");
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
                .subscribeOn( Schedulers.io())
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
</pre>