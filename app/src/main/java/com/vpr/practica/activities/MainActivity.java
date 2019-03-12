package com.vpr.practica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vpr.practica.R;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asigno un layout u otro dependiento de la orientacion del dispositivo
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_main);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscape_activity_main);

        //Componentes
        Button btAnadir = findViewById(R.id.btAnadir);
        Button btVer = findViewById(R.id.btVer);
        Button btConciertosEstado = findViewById(R.id.btConciertosEstado);

        //Listeners
        btAnadir.setOnClickListener(this);
        btVer.setOnClickListener(this);
        btConciertosEstado.setOnClickListener(this);

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
            case R.id.btConciertosEstado:
                Intent intent = new Intent(this, ListaConciertosEstado.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }

    /**
     * Metodo que crea y muestra un AlertDialog personalizado con informacion sobre la aplicacion
     */
    public void showAcercaDe(){
        View view = getLayoutInflater().inflate(R.layout.dialogo_acerca_de, null, false);

        TextView tvDescripcion = view.findViewById(R.id.tvDescripcion);
        tvDescripcion.setText(R.string.about_description);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_title);
        builder.setView(view);
        builder.create();
        builder.show();
    }

    //Creo el menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAcercaDe:
                showAcercaDe();
                return true;

            default:
                return false;
        }
    }
}
