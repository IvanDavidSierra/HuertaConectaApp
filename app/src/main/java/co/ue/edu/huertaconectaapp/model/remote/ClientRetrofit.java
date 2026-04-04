package co.ue.edu.huertaconectaapp.model.remote;

import co.ue.edu.huertaconectaapp.model.api.ApiService;
import co.ue.edu.huertaconectaapp.model.api.ValueApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientRetrofit {
    public static Retrofit retrofit = null;
    public static ApiService getService(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(ValueApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
