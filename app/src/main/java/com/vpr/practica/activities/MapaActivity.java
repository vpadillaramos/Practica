package com.vpr.practica.activities;

/*import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.vpr.practica.R;

public class MapaActivity extends Activity implements MapboxMap.OnMapClickListener, OnMapReadyCallback, View.OnClickListener {

    // Componentes
    MapView mapaView;
    MapboxMap mapaBox;
    FloatingActionButton btUbicacion;
    LocationServices locationServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establezco el token de MapBox
        MapboxAccountManager.start(this, "pk.eyJ1IjoieHJldngiLCJhIjoiY2pwdTY2c3VkMDIzaTN4bXRjcmhvcmZ6MSJ9.zXqfCHB4jHA-PZ-PrqT4qg");

        setContentView(R.layout.activity_mapa);

        // Referencio al MapView
        mapaView = findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);

        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapaBox = mapboxMap;
                anadirListeners();
            }
        });

        // Boton flotante
        btUbicacion = findViewById(R.id.btUbicacion);
        btUbicacion.setOnClickListener(this);
    }

    private void anadirListeners(){
        mapaBox.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        // Añadimos un marker donde hacemos click
        mapaBox.addMarker(new MarkerOptions().setPosition(point));

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapaBox = mapboxMap;
        locationServices = LocationServices.getLocationServices(this);
        Location lastLocation = locationServices.getLastLocation();

        CameraPosition posicion = new CameraPosition.Builder()
                .target(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                .zoom(16)
                .tilt(30)
                .build();

        mapaBox.animateCamera(CameraUpdateFactory.newCameraPosition(posicion), 7000);
    }

    @Override
    public void onClick(View v) {
        Location lastLocation = locationServices.getLastLocation();

        if(lastLocation != null)
            mapaBox.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

        // Resalta la posicion actual del usuario
        mapaBox.setMyLocationEnabled(true);
    }
}*/

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.vpr.practica.R;


public class MapaActivity extends AppCompatActivity implements MapboxMap.OnMapClickListener, View.OnClickListener {
    //Componentes
    MapView mapaView;
    MapboxMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        MapboxAccountManager.start(this, "pk.eyJ1IjoieHJldngiLCJhIjoiY2pwdTY2c3VkMDIzaTN4bXRjcmhvcmZ6MSJ9.zXqfCHB4jHA-PZ-PrqT4qg");

        setContentView(R.layout.activity_mapa);

        mapaView = (MapView) findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);


        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapa = mapboxMap;
                anadirListeners();
            }
        });

        //Boton flotante
        FloatingActionButton btUbicacion = (FloatingActionButton) findViewById(R.id.btUbicacion);
        btUbicacion.setOnClickListener(this);
    }


    private void anadirListeners(){
        mapa.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        //Añadimos un marker donde hacemos click
        LatLng posicionMarker = mapa.addMarker(new MarkerOptions().setPosition(point)).getPosition();

        Toast.makeText(this, String.valueOf(posicionMarker.getLatitude()), Toast.LENGTH_LONG);
    }



    @Override
    public void onClick(View view) {

    }
}
