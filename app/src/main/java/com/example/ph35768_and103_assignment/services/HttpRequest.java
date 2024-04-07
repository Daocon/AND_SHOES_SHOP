package com.example.ph35768_and103_assignment.services;

import static com.example.ph35768_and103_assignment.services.ApiServices.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {

    private ApiServices requestInterface;

    public HttpRequest() {
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiServices.class);
    }

    public ApiServices callApi() {
        return requestInterface;
    }

}
