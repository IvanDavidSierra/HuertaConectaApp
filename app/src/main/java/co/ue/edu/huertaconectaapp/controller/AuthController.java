package co.ue.edu.huertaconectaapp.controller;

import android.telecom.Call;

import javax.security.auth.callback.Callback;

import co.ue.edu.huertaconectaapp.model.AuthResult;
import co.ue.edu.huertaconectaapp.model.UserLogin;
import co.ue.edu.huertaconectaapp.model.UserRegister;
import co.ue.edu.huertaconectaapp.model.api.ApiService;
import co.ue.edu.huertaconectaapp.model.remote.ClientRetrofit;

public class AuthController {
    private ApiService apiService;
    public AuthController(){
        apiService = ClientRetrofit.getService();
    }
    public void register(UserRegister userRegister, retrofit2.Callback<AuthResult> callback){
        retrofit2.Call<AuthResult> call = apiService.register(userRegister);
        call.enqueue(callback);
    }
    public void login(UserLogin userLogin, retrofit2.Callback<AuthResult> callback){
        retrofit2.Call<AuthResult> call = apiService.login(userLogin);
        call.enqueue(callback);
    }
}
