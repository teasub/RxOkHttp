##简单的文件下载
<pre>
String url = "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/2015/05_20/14/com.speedsoftware.rootexplorer_140220.apk";
        HttpRequest.download(url,new File("/sdcard/rootexplorer_140220.apk"))
                .subscribe( new Observer<ProgressEvent>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ProgressEvent progressEvent) {
                        if (null != progressEvent){
                            mPbDownload.setProgress(progressEvent.getProgress());
                        }

                    }
                } );
</pre>