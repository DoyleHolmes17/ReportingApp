package com.simap.dishub.far;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Denny on 11/15/2016.
 */

public interface RestApi {
    @GET("c_report_hp")
    Call<ModelReport> loadListReport();
}
