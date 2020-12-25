package com.ueniweb.techsuperficial.nearbyme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.adapter.PlacesAdapter;
import com.ueniweb.techsuperficial.nearbyme.listener.NearByPlacesResponse;
import com.ueniweb.techsuperficial.nearbyme.model.Result;
import com.ueniweb.techsuperficial.nearbyme.webservices.GetNearByPlacesApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements NearByPlacesResponse {

    Context mcontext;
    @BindView(R.id.filter_tv)
    TextView filterTv;
    @BindView(R.id.places_rv)
    RecyclerView placesRv;
    PlacesAdapter placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        initVariable();
        setPlaceRv();
        callGEtPlacesApi();
    }



    private void initVariable() {
        mcontext=HomeActivity.this;
        placesAdapter=new PlacesAdapter(mcontext);

    }
    private void callGEtPlacesApi() {
        GetNearByPlacesApi getNearByPlacesApi=new GetNearByPlacesApi(mcontext,this);
        getNearByPlacesApi.callGetDistributerListApi(1000,"restaurant","-33.8670522,151.1957362",
                "AIzaSyC1MUU1jDFB227nre1JmEqaxqWY7N6rOGE");
    }


    private void setPlaceRv() {

        placesRv.setLayoutManager(new LinearLayoutManager(mcontext));
        placesRv.setItemAnimator(new DefaultItemAnimator());
        placesRv.setAdapter(placesAdapter);

    }
    @OnClick({R.id.filter_tv, R.id.places_rv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.filter_tv:
                Intent intent=new Intent(HomeActivity.this,FiltersActivity.class);
                startActivity(intent);
                break;
            case R.id.places_rv:
                break;
        }
    }

    @Override
    public void NearByPlaces(boolean isRecieved, ArrayList result) {
        if (isRecieved){
            placesAdapter.setList(result);
        }
    }
}
