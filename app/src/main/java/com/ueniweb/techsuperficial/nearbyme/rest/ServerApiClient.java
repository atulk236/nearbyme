package com.ueniweb.techsuperficial.nearbyme.rest;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ueniweb.techsuperficial.nearbyme.BuildConfig;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.DateDeserializer;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.DateSerializer;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ServerApiClient {
    private static ServerApiClient uniqueInstance;
    private static RestApiService retrofit_requestInterface;
    private static RestApiService retrofit_maskCallRequestInterface;

    private static GsonConverterFactory buildGSONConverterFactory() {
        return GsonConverterFactory.create(buildGSONConverter());
    }

    public static Gson buildGSONConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateSerializer());
        gsonBuilder.setDateFormat(DateFormat.LONG);
        return gsonBuilder.create();
    }

    public static RestApiService getApi() {
        if (uniqueInstance == null)
            uniqueInstance = new ServerApiClient();
        uniqueInstance.getRetrofitApiClient();
        return retrofit_requestInterface;
    }

    public static RestApiService getApiForMaskCall() {
        if (uniqueInstance == null)
            uniqueInstance = new ServerApiClient();
        uniqueInstance.getRetrofitMaskCallApiClient();
        return retrofit_maskCallRequestInterface;
    }

    private void getRetrofitMaskCallApiClient() {
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient
                    .Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor(interceptor).build();


        } catch (Exception exception1) {
        }
    }

    private void getRetrofitApiClient() {
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient
                    .Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor(interceptor).build();

            retrofit_requestInterface = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL)
                    .client(httpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(buildGSONConverterFactory())
                    .build().create(RestApiService.class);

        } catch (Exception exception1) {
        }
    }

    private void getRetrofitLoginApiClient(Interceptor loginInterceptor) {
        try {
            Log.d(">>>>>>>>>>", "1111111111111");
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient
                    .Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor(loginInterceptor)
                    .addInterceptor(interceptor)
                    .build();

            retrofit_requestInterface = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL)
                    .client(httpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(buildGSONConverterFactory())
                    .build().create(RestApiService.class);

        } catch (Exception exception1) {
        }
    }
}
