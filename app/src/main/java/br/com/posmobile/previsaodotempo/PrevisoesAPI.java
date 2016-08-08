package br.com.posmobile.previsaodotempo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alexandre on 07/08/16.
 */

public interface PrevisoesAPI {

    @GET("daily?mode=json&lang=pt&units=metric&cnt=14")
    Call<Previsoes> getPrevisoes(@Query("q") String cidade, @Query("APPID") String appid);

}
