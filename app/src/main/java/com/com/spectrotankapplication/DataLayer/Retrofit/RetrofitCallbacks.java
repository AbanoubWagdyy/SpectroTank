package com.com.spectrotankapplication.DataLayer.Retrofit;

import com.spectrotank.DataLayer.Model.TokenResponse;
import retrofit2.Call;

public interface RetrofitCallbacks {

    interface TokenResponseCallback {
        void onSuccess(TokenResponse result);

        void onFailure(Call<TokenResponse> call, Throwable t);
    }

    interface StringCallback {
        void onSuccess(String result);

        void onFailure(Call<String> call, Throwable t);
    }
}
