package com.vpr.practica.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.vpr.practica.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Componentes
    private Button btAnadir;
    private Button btVer;
    private Button btMapaConciertos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asigno un layout u otro dependiento de la orientacion del dispositivo
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscape_activity_main);

        //Componentes
        btAnadir = (Button) findViewById(R.id.btAnadir);
        btVer = (Button) findViewById(R.id.btVer);
        btMapaConciertos = (Button) findViewById(R.id.btMapaConciertos);

        //Listeners
        btAnadir.setOnClickListener(this);
        btVer.setOnClickListener(this);
        btMapaConciertos.setOnClickListener(this);


        gestionarPreferencias();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btAnadir:
                Intent intAnadirConcierto = new Intent(this, AnadirConcierto.class);
                intAnadirConcierto.putExtra("accion","nuevo");
                startActivity(intAnadirConcierto);
                break;
            case R.id.btVer:
                Intent intListaConciertos = new Intent(this, ListaConciertos.class);
                startActivity(intListaConciertos);
                break;
            case R.id.btMapaConciertos:
                Intent intent = new Intent(this, MapaActivity.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        gestionarPreferencias();
    }

    private void gestionarPreferencias() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean fuenteGrande = preferences.getBoolean("fuenteGrande", false);
        boolean modoNoche = preferences.getBoolean("modoNoche", false);
        String tituloApp = preferences.getString("tituloApp", null);

        // FUENTE GRANDE
        if(fuenteGrande){
            btMapaConciertos.setTextSize(25);
            btVer.setTextSize(25);
            btAnadir.setTextSize(25);
        }
        else{
            btMapaConciertos.setTextSize(16);
            btVer.setTextSize(16);
            btAnadir.setTextSize(16);
        }

        // TITULO APP
        if(tituloApp.isEmpty())
            return;
        else
            this.setTitle(tituloApp);

        // MODO NOCHE
        View view = this.getWindow().getDecorView();
        if(modoNoche)
            view.setBackgroundColor(Color.BLACK);
        else
            view.setBackgroundColor(Color.WHITE);
    }

    //Creo el menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemPreferencias:
                Intent intentPreferencias = new Intent(this, Preferencias.class);
                startActivity(intentPreferencias);
                return true;
            case R.id.itemAbout:
                Intent intent = new Intent(this, AcercaDe.class);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }
}
