package com.vpr.practica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vpr.practica.R;
import com.vpr.practica.adapters.ConciertoEstadoAdapter;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.db.Database;

import java.util.List;

public class ListaConciertosEstado extends Activity {
    //Atributos
    private List<Concierto> listaConciertosEstado;
    private ConciertoEstadoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Asigno un layout u otro dependiento de la orientacion del dispositivo
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_lista_conciertos_estado);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscape_activity_lista_conciertos_estado);

        //Componentes
        Database db = new Database(this);
        listaConciertosEstado = db.getConciertosEstado();

        ListView lvConciertos = findViewById(R.id.listaConciertosEstado);
        adapter = new ConciertoEstadoAdapter(this, R.layout.item_concierto_estado, listaConciertosEstado);
        lvConciertos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refrescar();
    }

    //METODOS

    /**
     * Refresca la lista de asistidos o cancelados a partir de la base de datos
     */
    public void refrescar(){
        listaConciertosEstado.clear();

        Database db = new Database(this);
        listaConciertosEstado.addAll(db.getConciertosEstado());
        adapter.notifyDataSetChanged();
    }

    /**
     * Muestra un dialogo con los conciertos pendientes, asistidos y cancelados
     */
    public void showContadores(){
        View layout = View.inflate(this, R.layout.dialogo_contadores, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(layout);
        builder.setTitle(R.string.status_counters);


        //Componentes
        TextView tvPendientes = layout.findViewById(R.id.tvPendientes);
        TextView tvAsistidos = layout.findViewById(R.id.tvAsistidos);
        TextView tvCancelados = layout.findViewById(R.id.tvCancelados);

        //Relleno los campos
        Database db = new Database(this);

        tvPendientes.setText(String.valueOf(db.getConciertos().size()));
        tvAsistidos.setText(String.valueOf(db.getConciertosAsistidos()));
        tvCancelados.setText(String.valueOf(db.getConciertosCancelados()));

        builder.create();
        builder.show();
    }

    //Menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_estados_conciertos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemConciertosPendientes:
                Intent intent = new Intent(this, ListaConciertos.class);
                startActivity(intent);
                return true;

            case R.id.itemContadores:
                showContadores();
                return true;

            default:
                return false;
        }
    }
}
