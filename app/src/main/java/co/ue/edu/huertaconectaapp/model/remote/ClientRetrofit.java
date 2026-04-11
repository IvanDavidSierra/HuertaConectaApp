package co.ue.edu.huertaconectaapp.model.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.ue.edu.huertaconectaapp.model.api.ApiService;
import co.ue.edu.huertaconectaapp.model.api.ValueApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientRetrofit {

    private static Retrofit retrofit = null;

    public static ApiService getService() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ValueApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
