package com.com.spectrotankapplication.DataLayer.Retrofit;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RestClient {


    private static RetrofitService retrofitService;

    public static RetrofitService getRetrofitService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalKeys.EndPoints.BaseURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        retrofitService = retrofit.create(RetrofitService.class);

        return retrofitService;
    }

    public static RetrofitService getRetrofitService(String token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
//                .addInterceptor(new GzipRequestInterceptor())
                .addNetworkInterceptor(new MyOkHttpInterceptor(token))
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalKeys.EndPoints.BaseURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        retrofitService = retrofit.create(RetrofitService.class);

        return retrofitService;
    }

    public static class MyOkHttpInterceptor implements Interceptor {

        String token;

        public MyOkHttpInterceptor(String token) {
            this.token = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request newRequest;
            newRequest = originalRequest.newBuilder()
                    .header("token", "bearer " + token)
                    .build();

            return chain.proceed(newRequest);
        }
    }

}