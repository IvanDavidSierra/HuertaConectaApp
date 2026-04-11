package co.ue.edu.huertaconectaapp.controller;

import co.ue.edu.huertaconectaapp.model.HuertaApiResponse;
import co.ue.edu.huertaconectaapp.model.HuertaRequest;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaApiResponse;
import co.ue.edu.huertaconectaapp.model.UsuarioHuertaRequest;
import co.ue.edu.huertaconectaapp.model.api.ApiService;
import co.ue.edu.huertaconectaapp.model.remote.ClientRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HuertaController {

    public interface HuertaListener {
        void onSuccess(int huertaId);
        void onError(String message);
    }

    public interface VinculacionListener {
        void onSuccess(int idUsuariosHuertas);
        void onError(String message);
    }

    private final ApiService apiService;

    public HuertaController() {
        apiService = ClientRetrofit.getService();
    }

    public void crearHuerta(HuertaRequest request, HuertaListener listener) {
        apiService.crearHuerta(request).enqueue(new Callback<HuertaApiResponse>() {
            @Override
            public void onResponse(Call<HuertaApiResponse> call, Response<HuertaApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body().getHuertaId());
                } else {
                    listener.onError("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<HuertaApiResponse> call, Throwable t) {
                listener.onError("Sin conexión: " + t.getMessage());
            }
        });
    }

    public void vincularUsuarioHuerta(int idUsuario, int idHuerta,
                                      String fechaVinculacion, VinculacionListener listener) {
        UsuarioHuertaRequest request = new UsuarioHuertaRequest(idUsuario, idHuerta, fechaVinculacion);
        apiService.vincularUsuarioHuerta(request).enqueue(new Callback<UsuarioHuertaApiResponse>() {
            @Override
            public void onResponse(Call<UsuarioHuertaApiResponse> call, Response<UsuarioHuertaApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body().getId());
                } else {
                    listener.onError("Error al vincular: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UsuarioHuertaApiResponse> call, Throwable t) {
                listener.onError("Sin conexión: " + t.getMessage());
            }
        });
    }
}
