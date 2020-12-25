package com.ueniweb.techsuperficial.nearbyme.rest;

import com.ueniweb.techsuperficial.nearbyme.model.Result;
import com.ueniweb.techsuperficial.nearbyme.model.ServerResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RestApiService {

   /* @POST("signup")
    @Headers("Content-Type: application/json")
    Call<ServerResponse<SignUpResponse>> userSignup(@Body SignUpRequest signUpRequest, @Query("isUserAvailable") boolean isUserAvailable);

    @PUT("signup/usernameAvailable")
    @Headers("Content-Type: application/json")
    Call<ServerResponse> checkUserAvailable(@Query("query") String username);*/
    @GET(ServerEndPointUrl.GETNEARBYPLACES)
    @Headers("Content-Type: application/json")
    Call<ServerResponse<Result>> getNearbyPlaces(@Query("radius") long radius,
                                                            @Query("type") String type,
                                                            @Query("location") String latlong,
                                                            @Query("key") String key);


}
