package com.vpr.practica.activities;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.vpr.practica.R;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.util.Constantes;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapaActivity extends AppCompatActivity implements MapboxMap.OnMapClickListener {

    // Atributos
    private List<Concierto> listaConciertos;
    LocationServices servicioUbicacion;

    //Componentes
    MapView mapaView;
    MapboxMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        MapboxAccountManager.start(this, "pk.eyJ1IjoieHJldngiLCJhIjoiY2pwdTY2c3VkMDIzaTN4bXRjcmhvcmZ6MSJ9.zXqfCHB4jHA-PZ-PrqT4qg");

        setContentView(R.layout.activity_mapa);

        listaConciertos = new ArrayList<>();
        mapaView = (MapView) findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);


        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapa = mapboxMap;
                anadirListeners();
                mostrarConciertos();
            }
        });

        ubicarUsuario();
    }


    private void anadirListeners(){
        mapa.setOnMapClickListener(this);
    }

    private void mostrarConciertos(){
        CargaConciertos cargaConciertos = new CargaConciertos();
        cargaConciertos.execute();
    }

    private void ubicarUsuario(){
        servicioUbicacion = LocationServices.getLocationServices(this);

        //Boton flotante y evento
        FloatingActionButton btUbicacion = (FloatingActionButton) findViewById(R.id.btUbicacion);
        btUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapa != null){
                    Location lastLocation = servicioUbicacion.getLastLocation();
                    if (lastLocation != null)
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 10));

                    mapa.setMyLocationEnabled(true);

                }
            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemPreferencias:

                return true;
            case R.id.itemAbout:
                Intent intent = new Intent(this, AcercaDe.class);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }


    private class CargaConciertos extends AsyncTask<Concierto, Void, Void> {

        @Override
        protected Void doInBackground(Concierto... conciertos) {

            // Llamo al servicio web
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Concierto[] concis = restTemplate.getForObject(Constantes.SERVER_URL + "/conciertos", Concierto[].class);

            // Relleno la lista y el adapter
            listaConciertos.addAll(Arrays.asList(concis));

            // Añado los markers de los conciertos
            for(Concierto c : listaConciertos){
                mapa.addMarker(new MarkerOptions().setTitle(c.getGrupos()).setPosition(new LatLng(c.getLatitud(), c.getLongitud())));
            }
            return null;
        }
    }
}
