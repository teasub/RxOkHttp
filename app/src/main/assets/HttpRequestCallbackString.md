##StringHttpRequestCallback
<pre>
RequestParams params = new RequestParams(this);
        params.addFormDataPart("page", 1);
        params.addFormDataPart("limit", 12);
        HttpRequest.post( Api.NEW_GAME, params, new StringResponse())
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribe( new Observer<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseResponse response) {
                        ILogger.d(response.getData().toString());
                        mTvResult.setText(JsonFormatUtils.formatJson((String) response.getData()));
                    }
                } );
</pre>