package com.com.spectrotankapplication.DataLayer.Retrofit;

import com.spectrotank.DataLayer.Model.Login;
import com.spectrotank.DataLayer.Model.TokenResponse;
import com.spectrotank.DataLayer.db.Item;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface RetrofitService {

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("auth/login")
    Call<TokenResponse> login(
            @Body Login login);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("sync")
    Call<String> sync(
            @Body List<Item> items);

}