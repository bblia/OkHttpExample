OkHttpExample is a simple sample application which uses Retrofit, OkHttp, and Dagger injection to load a the repositories under @bblia. The OkHttp client is constructed in NetModule and then passed into the Retrofit builder. 

I've added a few simple interceptors to the OkHttp client builder:

1. UserAgentInterceptor -- adds a user agent string with the app name and version to every api call
2. CacheInterceptor -- checks the network state (connection or no connection) and adds a cache policy based on that.
3. HttpLoggingInterceptor -- if app is in debug mode, log network responses to Logcat.
