package co.ue.edu.huertaconectaapp.controller;

import co.ue.edu.huertaconectaapp.model.AuthResult;
import co.ue.edu.huertaconectaapp.model.UserLogin;
import co.ue.edu.huertaconectaapp.model.UserRegister;
import co.ue.edu.huertaconectaapp.model.api.ApiService;
import co.ue.edu.huertaconectaapp.model.remote.ClientRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthController {
    public interface AuthListener{
        void onSuccess(AuthResult result);
        void onError(String message);
    }
    private ApiService apiService;
    public AuthController(){
        apiService = ClientRetrofit.getService();
    }

    public void register(UserRegister userRegister, AuthListener listener){
        apiService.register(userRegister).enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                if(response.isSuccessful()){
                    listener.onSuccess(response.body());
                }else {
                    listener.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable throwable) {

                listener.onError(throwable.getMessage());
            }
        });
    }

    public void login(UserLogin userLogin, AuthListener listener){
        apiService.login(userLogin).enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                if (response.isSuccessful()){
                    listener.onSuccess(response.body());
                }else{
                    listener.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable throwable) {

                listener.onError(throwable.getMessage());
            }
        });
    }
}
