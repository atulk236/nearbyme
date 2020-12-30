package com.ueniweb.techsuperficial.nearbyme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.ImageLoader;
import com.ueniweb.techsuperficial.nearbyme.model.Result;
import com.ueniweb.techsuperficial.nearbyme.rest.ServerApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetail extends AppCompatActivity {
    @BindView(R.id.place_iv)
    ImageView placeIv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_des_tv)
    TextView titleDesTv;
    String resultstr;
    Result result;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        resultstr = intent.getStringExtra("result");
        result = ServerApiClient.buildGSONConverter().fromJson(resultstr, Result.class);
        init();
    }

    private void init() {
        initVariable();
        setView();
    }

    private void setView() {
        titleTv.setText(result.getName());
        titleDesTv.setText(result.getTypes().get(0));
        ImageLoader.loadImage(result.getIcon(), placeIv, mcontext);
    }

    private void initVariable() {
        mcontext = PlaceDetail.this;
    }
}
