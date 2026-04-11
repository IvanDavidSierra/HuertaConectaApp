package co.ue.edu.huertaconectaapp.model.api;

import java.util.List;

import co.ue.edu.huertaconectaapp.model.AuthResult;
import co.ue.edu.huertaconectaapp.model.DashboardResponse;
import co.ue.edu.huertaconectaapp.model.Huerta;
import co.ue.edu.huertaconectaapp.model.HuertaApiResponse;
import co.ue.edu.huertaconectaapp.model.HuertaRequest;
import co.ue.edu.huertaconectaapp.model.UserLogin;
import co.ue.edu.huertaconectaapp.model.UserRegister;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaApiResponse;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaDetalle;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // ── Autenticación ─────────────────────────────────────────────────────────
    @POST("auth/register")
    Call<AuthResult> register(@Body UserRegister userRegister);

    @POST("auth/login")
    Call<AuthResult> login(@Body UserLogin userLogin);

    // ── Huertas ───────────────────────────────────────────────────────────────
    @GET("huertas")
    Call<List<Huerta>> getHuertas();

    @GET("huertas/{id}")
    Call<Huerta> getHuertaById(@Path("id") int id);

    @POST("huertas")
    Call<HuertaApiResponse> crearHuerta(@Body HuertaRequest huertaRequest);

    // ── Usuarios-Huertas ──────────────────────────────────────────────────────
    @POST("usuarios-huertas")
    Call<UsuarioHuertaApiResponse> vincularUsuarioHuerta(@Body UsuarioHuertaRequest request);

    @GET("usuarios-huertas/user/{userId}")
    Call<List<UsuarioHuertaDetalle>> getMisHuertas(@Path("userId") int userId);

    // ── Dashboard ─────────────────────────────────────────────────────────────
    @GET("dashboard/counts")
    Call<DashboardResponse> getDashboardCounts();
}
