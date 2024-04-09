package com.example.ph35768_and103_assignment.services;

import com.example.ph35768_and103_assignment.model.Bill;
import com.example.ph35768_and103_assignment.model.Cart;
import com.example.ph35768_and103_assignment.model.Category;
import com.example.ph35768_and103_assignment.model.Response;
import com.example.ph35768_and103_assignment.model.Shoe;
import com.example.ph35768_and103_assignment.model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiServices {

    public static String BASE_URL = "http://10.0.2.2:3000/";

    //user api

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

    //category api

    @GET("categoryRouter/getListCategory")
    Call<Response<ArrayList<Category>>> getListCategory();

    //shoe api

    @GET("shoesRouter/getShoeByCategory/{id}")
    Call<Response<ArrayList<Shoe>>> getShoeByCategory(@Path("id") String id);

    @GET("shoesRouter/getFavoriteShoe")
    Call<Response<ArrayList<Shoe>>> getFavoriteShoe();

    @GET("shoesRouter/getShoeById/{id}")
    Call<Response<Shoe>> getShoeById(@Path("id") String id);

    //cart api
    @POST("cartRouter/addToCart")
    Call<Response<Cart>> addToCart(@Body Cart cart);

    @GET("cartRouter/getListCart")
    Call<Response<ArrayList<Cart>>> getListCart();

    @DELETE("cartRouter/deleteCart/{id}")
    Call<Response<Cart>> deleteCart(@Path("id") String id);

    @DELETE("cartRouter/deleteAllCart")
    Call<Response<Cart>> deleteAllCart();

    //bill api
    @POST("billRouter/createBill")
    Call<Response<Bill>> createBill(@Body Bill bill);
}
