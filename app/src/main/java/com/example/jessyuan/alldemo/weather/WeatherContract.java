package com.example.jessyuan.alldemo.weather;

import com.example.jessyuan.alldemo.base.BasePresenter;
import com.example.jessyuan.alldemo.base.BaseView;
import com.example.jessyuan.alldemo.model.Weather;

import java.util.List;

/**
 * Created by JessYuan on 27/12/2016.
 */

public interface WeatherContract {
    interface WeatherView extends BaseView {
        void showTitle(String title);
        void updateView(Weather weather);
        void updateTipsView(String string);
        void showLoading();
        void dismissLoading();
    }

    interface WeatherIndexView extends BaseView {
        void showCityWeather(List<String> list);
    }

    interface WeatherPresenter extends BasePresenter {
        void start();
        void startLocation();
        void queryWeather(String city);
        void stopLocation();
        void stop();
    }

    interface WeatherIndexPresenter extends BasePresenter {
        void saveCity(String city);
    }
}
