package com.ueniweb.techsuperficial.nearbyme.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.CircleTransform;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.FilterSession;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.PermissionsUtils;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.ShowDialog;
import com.ueniweb.techsuperficial.nearbyme.adapter.PlacesAdapter;
import com.ueniweb.techsuperficial.nearbyme.dialog.Place_dialog;
import com.ueniweb.techsuperficial.nearbyme.listener.NearByPlacesResponse;
import com.ueniweb.techsuperficial.nearbyme.listener.PlaceClickListener;
import com.ueniweb.techsuperficial.nearbyme.model.Result;
import com.ueniweb.techsuperficial.nearbyme.webservices.GetNearByPlacesApi;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements NearByPlacesResponse, LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, PlaceClickListener {
    Context mcontext;
    @BindView(R.id.filter_tv)
    TextView filterTv;
    @BindView(R.id.places_rv)
    RecyclerView placesRv;
    PlacesAdapter placesAdapter;
    FilterSession filterSession;
    ArrayList<Result> resultlist;
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
    private GoogleMap mGoogleMap;
    LatLng mLatLng;

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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                    } else {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    private void initVariable() {
        mcontext = HomeActivity.this;
        placesAdapter = new PlacesAdapter(mcontext, this);
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
        //for static location
        GetNearByPlacesApi getNearByPlacesApi = new GetNearByPlacesApi(mcontext, this);
        getNearByPlacesApi.callGetDistributerListApi(3000, type, "28.6304,77.2177",
                "yourkey");

        //for dynamic location
       /* if (longlat != null && !longlat.equals("")) {
            GetNearByPlacesApi getNearByPlacesApi = new GetNearByPlacesApi(mcontext, this);
            getNearByPlacesApi.callGetDistributerListApi(3000, type, longlat,
                    "your key");
        }*/

    }

    private void setPlaceRv() {
        placesRv.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false));
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

            for (int i = 0; i < resultlist.size(); i++) {

                mLatLng = new LatLng(resultlist.get(i).getGeometry().getLocation().getLat(),
                        resultlist.get(i).getGeometry().getLocation().getLng());

                renderMarker(resultlist.get(i).getIcon(), mLatLng, i);

            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        ShowDialog.dismissSweetDialog();
        //for dynamic location
       /* mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        moveMapinitial(mLatLng);
        longlat = location.getLatitude() + "," + location.getLongitude();
        UpdateListViaFiltered();*/

        //for static location(for now used static long lat)
        String[] latlong =  "28.6304,77.2177".split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng locationf = new LatLng(latitude, longitude);
        moveMapinitial(locationf);
        UpdateListViaFiltered();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // getting GPS status
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                ShowDialog.dismissSweetDialog();
                ShowDialog.showErrorDialog(mcontext, "check your location is enabled or not!");
            } else {
                if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                } else {
                    mGoogleMap = googleMap;
                    mGoogleMap.setMyLocationEnabled(true);
                    mGoogleMap.setPadding(0, 0, 0, 500);
                    //for dynamic location
                  /*  if (mLatLng != null) {
                        moveMap(mLatLng);
                    }*/

                  //for static location
                    String[] latlong =  "28.6304,77.2177".split(",");
                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng locationf = new LatLng(latitude, longitude);
                    moveMap(locationf);

                    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            // mLatLng = latLng;
                            //  mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //  UpdateListViaFiltered();

                        }
                    });
                    mGoogleMap.setOnMarkerClickListener(this::onMarkerClick);
                    mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {

                            Location location = mGoogleMap.getMyLocation();

                            if (location != null) {
                                //for dynamic location
                              /*  mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                longlat = location.getLatitude() + "," + location.getLongitude();
                                moveMapinitial(mLatLng);*/
                               // UpdateListViaFiltered();

                            }
                            //for static location
                            String[] latlong =  "28.6304,77.2177".split(",");
                            double latitude = Double.parseDouble(latlong[0]);
                            double longitude = Double.parseDouble(latlong[1]);
                            LatLng locationf = new LatLng(latitude, longitude);
                            longlat="28.6304,77.2177";
                            moveMapinitial(locationf);
                            UpdateListViaFiltered();

                            return false;
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveMap(LatLng latLng) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(80));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void moveMapinitial(LatLng latLng) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void renderMarker(String image, LatLng latLng, int position) {
        Log.e("LOCATION", "renderMarker: " + latLng);
        if (image != null && !TextUtils.isEmpty(image)) {
            LatLng finalLatLng = latLng;
            Picasso.with(mcontext)
                    .load(image)
                    .transform(new CircleTransform())
                    .resize(150, 150)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                    .position(finalLatLng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                            marker.setTag(position);
                        }


                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            Log.e("HomeActivity", "onBitmapFailed: ");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            Log.e("HomeActivity", ": placeHolderDrawable");

                        }
                    });
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        //moveMap(marker.getPosition());
        int position = (int) marker.getTag();
        Result result = resultlist.get(position);
        LatLng newlatlng;
        newlatlng = new LatLng(result.getGeometry().getLocation().getLat(),
                result.getGeometry().getLocation().getLng());

        String[] latlong =  "28.6304,77.2177".split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        LatLng locationf = new LatLng(latitude, longitude);
        moveMap(locationf);
        placesRv.getLayoutManager().scrollToPosition(position);
        Place_dialog place_dialog = new Place_dialog(mcontext, result);
        place_dialog.show();
        return false;
    }

    @Override
    public void PlaceClicked(int position, LatLng latLng) {
        moveMap(latLng);
        Result result = resultlist.get(position);
        Place_dialog place_dialog = new Place_dialog(mcontext, result);
        place_dialog.show();
    }

}
