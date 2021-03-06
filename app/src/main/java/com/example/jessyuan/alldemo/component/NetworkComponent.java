package com.example.jessyuan.alldemo.component;

import com.google.gson.Gson;

import android.app.Application;

import com.example.jessyuan.alldemo.module.ApplicationModule;
import com.example.jessyuan.alldemo.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/** This component involves retrofit, okhttpclient, and gson, and provide there to other module or
 * component
 * Created by JessYuan on 15/12/2016.
 */

@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class})
public interface NetworkComponent {
    Retrofit getRetrofit();
    OkHttpClient getOkhttpClient();
    Gson getGson();
}
