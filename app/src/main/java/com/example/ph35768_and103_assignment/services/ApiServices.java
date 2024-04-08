package com.example.ph35768_and103_assignment.services;

import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiServices {

    public static String BASE_URL = "http://10.0.2.2:3000/";

    @POST("userRouter/signin")
    Call<Response<User>> login(@Body User user);
    @Multipart
    @POST("userRouter/register")
    Call<Response<User>> register(@Part("address") RequestBody address,
                                  @Part("email") RequestBody email,
                                  @Part MultipartBody.Part avatar,
                                  @Part("name") RequestBody name,
                                  @Part("password") RequestBody password,
                                  @Part("phone") RequestBody phone);

    @Multipart
    @PUT("userRouter/updateUserWithImage/{id}")
    Call<Response<User>> updateUserWithImage(@Part("address") RequestBody address,
                                             @Part("email") RequestBody email,
                                             @Part MultipartBody.Part avatar,
                                             @Part("name") RequestBody name,
                                             @Part("password") RequestBody password,
                                             @Part("phone") RequestBody phone,
                                             @Path("id") String id);

    @DELETE("userRouter/deleteUser/{id}")
    Call<Response<User>> deleteUser(@Path("id") String id);
}
