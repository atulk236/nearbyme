package com.ueniweb.techsuperficial.nearbyme.webservices;

import android.content.Context;
import android.widget.Toast;

import com.ueniweb.techsuperficial.nearbyme.actionhelper.ShowDialog;
import com.ueniweb.techsuperficial.nearbyme.listener.NearByPlacesResponse;
import com.ueniweb.techsuperficial.nearbyme.model.Result;
import com.ueniweb.techsuperficial.nearbyme.model.ServerResponse;
import com.ueniweb.techsuperficial.nearbyme.rest.RestApiService;
import com.ueniweb.techsuperficial.nearbyme.rest.ServerApiClient;

import org.jetbrains.annotations.NotNull;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetNearByPlacesApi implements Callback<ServerResponse<Result>> {
    private Context mcontext;
    NearByPlacesResponse mlistener;

    public GetNearByPlacesApi(Context mcontext, NearByPlacesResponse nearByPlacesResponse) {
        this.mcontext = mcontext;
        this.mlistener = nearByPlacesResponse;
    }

    public void callGetDistributerListApi(long radius, String type, String latlong, String key) {
        //ShowDialog.showSweetDialog(mcontext, "Processing", "Please Wait", SweetAlertDialog.PROGRESS_TYPE);
        RestApiService restApiService = ServerApiClient.getApi();
        Call<ServerResponse<Result>> call = restApiService.getNearbyPlaces(radius, type, latlong, key);
        call.enqueue(this);
    }


    @Override
    public void onResponse(@NotNull Call<ServerResponse<Result>> call, Response<ServerResponse<Result>> response) {
        if (response.isSuccessful()) {
            ShowDialog.dismissSweetDialog();
            mlistener.NearByPlaces(true, response.body().getResults());
        } else {
            ShowDialog.dismissSweetDialog();
            Toast.makeText(mcontext, "Something wents wrong", Toast.LENGTH_SHORT).show();
            mlistener.NearByPlaces(false, null);
        }

    }

    @Override
    public void onFailure(Call<ServerResponse<Result>> call, Throwable t) {
        Toast.makeText(mcontext, "Something went swrong", Toast.LENGTH_SHORT).show();

    }
}
