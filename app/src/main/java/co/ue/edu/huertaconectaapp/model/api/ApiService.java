package co.ue.edu.huertaconectaapp.model.api;

import co.ue.edu.huertaconectaapp.model.AuthResult;
import co.ue.edu.huertaconectaapp.model.UserLogin;
import co.ue.edu.huertaconectaapp.model.UserRegister;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/register")
    Call<AuthResult> register(@Body UserRegister userRegister);

    @POST("auth/login")
    Call<AuthResult> login(@Body UserLogin userLogin);
}
