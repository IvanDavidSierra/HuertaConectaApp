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

    public interface AuthListener {
        void onSuccess(AuthResult result);
        void onError(String message);
    }

    private final ApiService apiService;

    public AuthController() {
        apiService = ClientRetrofit.getService();
    }

    public void register(UserRegister userRegister, AuthListener listener) {
        apiService.register(userRegister).enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError(mensajeDeError(response.code(), "registro"));
                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
                listener.onError("Sin conexión con el servidor. Verifica tu red.");
            }
        });
    }

    public void login(UserLogin userLogin, AuthListener listener) {
        apiService.login(userLogin).enqueue(new Callback<AuthResult>() {
            @Override
            public void onResponse(Call<AuthResult> call, Response<AuthResult> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError(mensajeDeError(response.code(), "login"));
                }
            }

            @Override
            public void onFailure(Call<AuthResult> call, Throwable t) {
                listener.onError("Sin conexión con el servidor. Verifica tu red.");
            }
        });
    }

    private String mensajeDeError(int codigo, String operacion) {
        switch (codigo) {
            case 400: return "Datos incompletos o inválidos.";
            case 401: return "Correo o contraseña incorrectos.";
            case 404: return "Usuario no encontrado.";
            case 409: return "El correo ya está registrado. Intenta con otro.";
            case 500: return "Error interno del servidor. Intenta más tarde.";
            default:  return "Error inesperado (" + codigo + "). Intenta de nuevo.";
        }
    }
}
