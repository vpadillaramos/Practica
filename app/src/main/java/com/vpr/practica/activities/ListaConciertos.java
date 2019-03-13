package com.vpr.practica.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.vpr.practica.R;
import com.vpr.practica.adapters.ConciertoAdapter;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.util.Constantes;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaConciertos extends AppCompatActivity {

    //Atributos
    public List<Concierto> listConciertos;
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

        lvConciertos = (ListView) findViewById(R.id.lvConciertos);
        recojoConciertos = new ArrayList<>();
        adapter = new ConciertoAdapter(this, R.layout.item_concierto, recojoConciertos);
        lvConciertos.setAdapter(adapter);
        registerForContextMenu(lvConciertos);

        // Cargo los conciertos
        CargaConciertos cargaConciertos = new CargaConciertos();
        cargaConciertos.execute();
    }

    // AsyncTask<URI url, Object request, Class<T> responseType
    private class CargaConciertos extends AsyncTask<Concierto, Void, Void> {

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


}
