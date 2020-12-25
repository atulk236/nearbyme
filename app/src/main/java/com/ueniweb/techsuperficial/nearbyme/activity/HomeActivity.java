package com.ueniweb.techsuperficial.nearbyme.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.FilterSession;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.PermissionsUtils;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.ShowDialog;
import com.ueniweb.techsuperficial.nearbyme.adapter.PlacesAdapter;
import com.ueniweb.techsuperficial.nearbyme.listener.NearByPlacesResponse;
import com.ueniweb.techsuperficial.nearbyme.webservices.GetNearByPlacesApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeActivity extends AppCompatActivity implements NearByPlacesResponse, LocationListener {

    Context mcontext;
    @BindView(R.id.filter_tv)
    TextView filterTv;
    @BindView(R.id.places_rv)
    RecyclerView placesRv;
    PlacesAdapter placesAdapter;
    FilterSession filterSession;
    ArrayList resultlist;
    @BindView(R.id.nodata_tv)
    TextView nodata_tv;
    LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    String longlat;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5 * 1; // 5 second
    String filter_selected;

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
        if (PermissionsUtils.checkLocationPermission(HomeActivity.this) &&
                PermissionsUtils.checkFineLocation(HomeActivity.this)) {
        }
        if (filter_selected != null && !filter_selected.equals("")) {
            getLocation();
        } else {
            placesRv.setVisibility(View.GONE);
            nodata_tv.setVisibility(View.VISIBLE);
            Toast.makeText(mcontext, "Please apply filters!", Toast.LENGTH_SHORT).show();
        }

    }

    public Location getLocation() {
        try {
            ShowDialog.showSweetDialog(mcontext, "Getting your location", "Please Wait", SweetAlertDialog.PROGRESS_TYPE);
            locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                ShowDialog.dismissSweetDialog();
                ShowDialog.showErrorDialog(mcontext, "check your location is enabled or not!");
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //check the network permission
                    if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mcontext, new String[]
                                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                101);
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        //check the network permission
                        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    private void initVariable() {
        mcontext = HomeActivity.this;
        placesAdapter = new PlacesAdapter(mcontext);
        filterSession = new FilterSession(mcontext);
        resultlist = new ArrayList();
        resultlist.clear();
        filter_selected = filterSession.getData(FilterSession.FILTER_SELECTED);

    }

    private void UpdateListViaFiltered() {
        switch (filter_selected) {
            case "all":
                callGEtPlacesApi("gym");
                callGEtPlacesApi("cafe");
                callGEtPlacesApi("restaurant");
                break;
            case "gym":
                callGEtPlacesApi("gym");
                break;
            case "cafe":
                callGEtPlacesApi("cafe");

                break;
            case "restaurant":
                callGEtPlacesApi("restaurant");
                break;

            case "gym_cafe":
                callGEtPlacesApi("gym");
                callGEtPlacesApi("cafe");
                break;
            case "gym_restaurant":
                callGEtPlacesApi("gym");
                callGEtPlacesApi("restaurant");

                break;
            case "cafe_restaurant":
                callGEtPlacesApi("cafe");
                callGEtPlacesApi("restaurant");
                break;
            default:
                placesRv.setVisibility(View.GONE);
                nodata_tv.setVisibility(View.VISIBLE);
                Toast.makeText(mcontext, "Please apply filters!", Toast.LENGTH_SHORT).show();

        }
    }

    private void callGEtPlacesApi(String type) {
        if (longlat != null && !longlat.equals("")) {
            GetNearByPlacesApi getNearByPlacesApi = new GetNearByPlacesApi(mcontext, this);
            getNearByPlacesApi.callGetDistributerListApi(1000, type, longlat,
                    "AIzaSyC1MUU1jDFB227nre1JmEqaxqWY7N6rOGE");
        } else {
            GetNearByPlacesApi getNearByPlacesApi = new GetNearByPlacesApi(mcontext, this);
            getNearByPlacesApi.callGetDistributerListApi(1000, type, "28.7041,77.1025",
                    "AIzaSyC1MUU1jDFB227nre1JmEqaxqWY7N6rOGE");
        }

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
                Intent intent = new Intent(HomeActivity.this, FiltersActivity.class);
                startActivity(intent);
                break;
            case R.id.places_rv:
                break;
        }
    }

    @Override
    public void NearByPlaces(boolean isRecieved, ArrayList result) {
        if (isRecieved) {
            resultlist.addAll(result);
            placesAdapter.setList(resultlist);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        ShowDialog.dismissSweetDialog();
        longlat = location.getLatitude() + "," + location.getLongitude();
        UpdateListViaFiltered();

    }
}
