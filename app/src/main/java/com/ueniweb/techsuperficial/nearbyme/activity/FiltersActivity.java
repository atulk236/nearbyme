package com.ueniweb.techsuperficial.nearbyme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.FilterSession;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FiltersActivity extends AppCompatActivity {
    Context mcontext;
    @BindView(R.id.select_allcb)
    CheckBox selectAllcb;
    @BindView(R.id.gymcb)
    CheckBox gymcb;
    @BindView(R.id.Cafecb)
    CheckBox Cafecb;
    @BindView(R.id.restaurantcb)
    CheckBox restaurantcb;
    @BindView(R.id.submit_btn)
    TextView submitBtn;
    @BindView(R.id.clear_tv)
    TextView clearTv;
    FilterSession filterSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initVariable();
        setView();
    }

    private void setView() {
        String filter_selected = filterSession.getData(FilterSession.FILTER_SELECTED);
        switch (filter_selected) {
            case "all":
                selectAllcb.setChecked(true);
                gymcb.setChecked(true);
                Cafecb.setChecked(true);
                restaurantcb.setChecked(true);
                break;
            case "gym":
                gymcb.setChecked(true);

                break;
            case "cafe":
                Cafecb.setChecked(true);
                break;
            case "restaurant":
                restaurantcb.setChecked(true);
                break;

            case "gym_cafe":
                gymcb.setChecked(true);
                Cafecb.setChecked(true);
                break;
            case "gym_restaurant":
                restaurantcb.setChecked(true);
                gymcb.setChecked(true);

                break;
            case "cafe_restaurant":
                restaurantcb.setChecked(true);
                Cafecb.setChecked(true);

                break;

        }
    }

    private void initVariable() {
        mcontext = FiltersActivity.this;
        filterSession = new FilterSession(mcontext);


    }


    @OnClick({R.id.select_allcb, R.id.gymcb, R.id.Cafecb, R.id.restaurantcb, R.id.submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_allcb:
                if (selectAllcb.isChecked()) {
                    gymcb.setChecked(true);
                    Cafecb.setChecked(true);
                    restaurantcb.setChecked(true);
                } else {
                    gymcb.setChecked(false);
                    Cafecb.setChecked(false);
                    restaurantcb.setChecked(false);
                }

                break;
            case R.id.gymcb:
                break;
            case R.id.Cafecb:
                break;
            case R.id.restaurantcb:
                break;
            case R.id.submit_btn:
                if (selectAllcb.isChecked() || (gymcb.isChecked() && Cafecb.isChecked() && restaurantcb.isChecked())) {
                    selectAllcb.setChecked(true);
                    gymcb.setChecked(true);
                    Cafecb.setChecked(true);
                    restaurantcb.setChecked(true);
                    filterSession.setData(FilterSession.FILTER_SELECTED, "all");
                } else if (gymcb.isChecked()) {
                    if (Cafecb.isChecked()) {
                        filterSession.setData(FilterSession.FILTER_SELECTED, "gym_cafe");
                    } else if (restaurantcb.isChecked()) {
                        filterSession.setData(FilterSession.FILTER_SELECTED, "gym_restaurant");
                    } else {
                        filterSession.setData(FilterSession.FILTER_SELECTED, "gym");

                    }
                } else if (Cafecb.isChecked()) {
                    filterSession.setData(FilterSession.FILTER_SELECTED, "cafe");
                    if (gymcb.isChecked()) {
                        filterSession.setData(FilterSession.FILTER_SELECTED, "gym_cafe");
                    } else if (restaurantcb.isChecked()) {
                        filterSession.setData(FilterSession.FILTER_SELECTED, "cafe_restaurant");
                    }
                } else if (restaurantcb.isChecked()) {
                    filterSession.setData(FilterSession.FILTER_SELECTED, "restaurant");
                    if (gymcb.isChecked()) {
                        filterSession.setData(FilterSession.FILTER_SELECTED, "gym_restaurant");
                    } else if (Cafecb.isChecked()) {
                        filterSession.setData(FilterSession.FILTER_SELECTED, "cafe_restaurant");
                    }

                }
                else {
                    onViewClicked();
                }
                startHomeActivity();
                break;
        }


    }

    private void startHomeActivity() {
        Intent intent = new Intent(FiltersActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.clear_tv)
    public void onViewClicked() {
        gymcb.setChecked(false);
        Cafecb.setChecked(false);
        restaurantcb.setChecked(false);
        selectAllcb.setChecked(false);
        filterSession.clearData();
    }
}
