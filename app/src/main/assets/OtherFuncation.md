##配置RxOkHttp
<pre>
List<Part> commomParams = new ArrayList<>();
Headers commonHeaders = new Headers.Builder().build();

RxOkHttpConfiguration.Builder builder = new RxOkHttpConfiguration.Builder()
		.setCommenParams(commomParams)
		.setCommenHeaders(commonHeaders)
		.setTimeout(Constants.REQ_TIMEOUT)
				//.setCookieJar(CookieJar.NO_COOKIES)
				//.setCertificates(...)
				//.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
		.setDebug(true);
RxOkHttp.getInstance().init(builder.build());
</pre>