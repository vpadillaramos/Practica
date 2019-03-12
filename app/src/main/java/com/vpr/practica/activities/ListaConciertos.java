package com.vpr.practica.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vpr.practica.R;
import com.vpr.practica.adapters.ConciertoAdapter;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.db.Database;
import com.vpr.practica.util.Constantes;
import com.vpr.practica.util.Util;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaConciertos extends AppCompatActivity {

    //Atributos
    private List<Concierto> listConciertos;
    private List<Concierto> recojoConciertos;
    private ConciertoAdapter adapter;
    private ListView lvConciertos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asigno un layout u otro dependiento de la orientacion del dispositivo
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_lista_conciertos);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscape_activity_lista_conciertos);


        //Componentes
        Database db = new Database(this);
        listConciertos = db.getConciertos();

        lvConciertos = (ListView) findViewById(R.id.lvConciertos);
        recojoConciertos = new ArrayList<>();
        adapter = new ConciertoAdapter(this, R.layout.item_concierto, recojoConciertos);
        lvConciertos.setAdapter(adapter);
        registerForContextMenu(lvConciertos);

        // Cargo los conciertos
        CargaConciertos cargaConciertos = new CargaConciertos();
        cargaConciertos.execute();
    }

    //METODOS
    public void refrescar(){
        listConciertos.clear();

        Database db = new Database(this);
        listConciertos.addAll(db.getConciertos());
        adapter.notifyDataSetChanged();
    }

    // AsyncTask<URI url, Object request, Class<T> responseType
    private class CargaConciertos extends AsyncTask<Concierto, Void, Void>{

        private ProgressDialog dialog;

        @Override
        protected Void doInBackground(Concierto... conciertos) {
            // iniicializo la lista de eventos para rellenarla con los datos
            listConciertos = new ArrayList<>();

            // Llamo al servicio web
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Concierto[] concis = restTemplate.getForObject(Constantes.SERVER_URL + "/conciertos", Concierto[].class);

            // Relleno la lista y el adapter
            listConciertos.addAll(Arrays.asList(concis));
            for(Concierto concierto : listConciertos)
                recojoConciertos.add(concierto);

            return null;
        }

        // Notifico los cambios
        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        refrescar();
    }

    //Menu contextual al pulsar un elemento de la lista
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int posicion = menuInfo.position;

        switch (item.getItemId()){
            case R.id.itemModificar:
                Intent intent = new Intent(this, AnadirConcierto.class);
                Concierto concierto = listConciertos.get(posicion);

                intent.putExtra("accion","modificar");
                intent.putExtra("concierto",concierto);
                //intent.putExtra("cartel", Util.getBytes(concierto.getCartel()));
                startActivity(intent);
                return true;

            case R.id.itemEliminar:
                Database db = new Database(this);
                db.eliminarConcierto(listConciertos.get(posicion));
                refrescar();
                return true;

            default:
                return false;
        }
    }

    //Menu de la parte superios qu permite añadir un nuevo concierto

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAnadir:
                Intent intent = new Intent(this, AnadirConcierto.class);
                intent.putExtra("accion","nuevo");
                startActivity(intent);
                return true;
            case R.id.itemVerEstadoConciertos:
                Intent intentVerEstado = new Intent(this, ListaConciertosEstado.class);
                startActivity(intentVerEstado);
                return true;

            case R.id.itemContadores:

                return true;
            default:
                return false;
        }
    }
}
