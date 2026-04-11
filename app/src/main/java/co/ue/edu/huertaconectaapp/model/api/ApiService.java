package co.ue.edu.huertaconectaapp.model.api;

import co.ue.edu.huertaconectaapp.model.AuthResult;
import co.ue.edu.huertaconectaapp.model.HuertaApiResponse;
import co.ue.edu.huertaconectaapp.model.HuertaRequest;
import co.ue.edu.huertaconectaapp.model.UserLogin;
import co.ue.edu.huertaconectaapp.model.UserRegister;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaApiResponse;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("auth/register")
    Call<AuthResult> register(@Body UserRegister userRegister);

    @POST("auth/login")
    Call<AuthResult> login(@Body UserLogin userLogin);


    @POST("huertas")
    Call<HuertaApiResponse> crearHuerta(@Body HuertaRequest huertaRequest);

    @POST("usuarios-huertas")
    Call<UsuarioHuertaApiResponse> vincularUsuarioHuerta(@Body UsuarioHuertaRequest request);
}
