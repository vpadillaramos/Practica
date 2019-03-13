package com.vpr.practica.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.vpr.practica.R;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.util.Constantes;
import com.vpr.practica.util.Util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Calendar;

public class AnadirConcierto extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, MapboxMap.OnMapClickListener {
    //Constantes
    private static final int CARTEL = 1;
    private final int RES_IMAGEN_DEFAULT = R.mipmap.ic_launcher_round;

    //Atributos
    private String accion;
    private long idConcierto;
    private boolean flagImagenSeleccionada = false;
    private Concierto conciertoGuardar;
    private boolean isLugarAnadido;

    // Componentes
    private EditText etGrupos;
    private TextView tvFecha;
    private TextView tvHora;
    private MapView mapaView;
    private MapboxMap mapaBox;
    private EditText etPrecio;
    private CheckBox chbAsistido;
    private CheckBox chbCancelado;
    private Button btGuardar;
    private Button btFecha;
    private Button btHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, "pk.eyJ1IjoieHJldngiLCJhIjoiY2pwdTY2c3VkMDIzaTN4bXRjcmhvcmZ6MSJ9.zXqfCHB4jHA-PZ-PrqT4qg");
        //Asigno un layout u otro dependiento de la orientacion del dispositivo
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_anadir_concierto);

        //En este modo el icono por defecto no se ve, pero se puede pulsar para seleccionar una imagen
        //El editor avisa que en landscape puede dar algun error
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscpae_activity_anadir_concierto);

        isLugarAnadido = false;
        conciertoGuardar = new Concierto();
        //Componentes
        etGrupos = (EditText) findViewById(R.id.etGrupos);
        tvFecha = (TextView) findViewById(R.id.tvFecha);
        tvHora = (TextView) findViewById(R.id.tvHora);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        chbAsistido = (CheckBox) findViewById(R.id.chbAsistido);
        chbCancelado = (CheckBox) findViewById(R.id.chbCancelado);

        btGuardar = (Button) findViewById(R.id.btGuardar);
        btFecha = (Button) findViewById(R.id.btFecha);
        btHora = (Button) findViewById(R.id.btHora);

        // Para mostrar el mapa

        mapaView = (MapView) findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);
        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mapaBox = mapboxMap;
                anadirListeners();
            }
        });


        accion = getIntent().getStringExtra("accion");
        if(accion.equals("modificar")){
            Concierto concierto = (Concierto) getIntent().getSerializableExtra("concierto");
            //Bitmap cartel = Util.getBitmap(getIntent().getByteArrayExtra("cartel"));
            //rellenarCampos(concierto, cartel);
        }
    }

    public void anadirListeners(){
        btGuardar.setOnClickListener(this);
        btFecha.setOnClickListener(this);
        btHora.setOnClickListener(this);
        chbAsistido.setOnCheckedChangeListener(this);
        chbCancelado.setOnCheckedChangeListener(this);
        mapaBox.setOnMapClickListener(this);
    }

    public void limpiar(){
        //limpio
        etGrupos.setText("");
        tvFecha.setText("");
        tvHora.setText("");
        //etLugar.setText("");
        etPrecio.setText("");
        chbAsistido.setChecked(false);
        chbCancelado.setChecked(false);
        mapaBox.clear();
        isLugarAnadido = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btUbicacion:
                Intent intentMapa = new Intent(this, MapaActivity.class);
                startActivity(intentMapa);
                break;
            case R.id.btGuardar:

                //CONTROL DE DATOS
                if(etGrupos.getText().toString().equals("")){
                    etGrupos.requestFocus();
                    Toast.makeText(this, R.string.bands_missing, Toast.LENGTH_LONG).show();
                    return;
                }

                if(tvFecha.getText().toString().equals("")) {
                    if(!accion.equals("modificar")){
                        Toast.makeText(this, R.string.date_required, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if(tvHora.getText().toString().equals(""))
                    tvHora.setText(R.string.time_missing);

                if(etPrecio.getText().toString().equals(""))
                    etPrecio.setText(R.string.price_missing);

                if(!Util.isFloat(etPrecio.getText().toString())){
                    Toast.makeText(this, R.string.wrong_decimal_format, Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isLugarAnadido){
                    Toast.makeText(this, R.string.place_required, Toast.LENGTH_LONG).show();
                    return;
                }

                conciertoGuardar.setGrupos(etGrupos.getText().toString());
                if(!tvFecha.getText().toString().equals(R.string.date_missing)){
                    try {
                        conciertoGuardar.setFecha(Util.parsearFecha(tvFecha.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                    conciertoGuardar.setFecha(null);

                conciertoGuardar.setHora(tvHora.getText().toString());
                try {
                    conciertoGuardar.setPrecio(Util.parseDecimal(etPrecio.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                conciertoGuardar.setAsistido(chbAsistido.isChecked());
                conciertoGuardar.setCancelado(chbCancelado.isChecked());

                switch (accion){
                    case "nuevo":
                        limpiar();
                        GuardaConcierto guardaConcierto = new GuardaConcierto();
                        guardaConcierto.execute(conciertoGuardar.getGrupos(), Util.formateaFecha(conciertoGuardar.getFecha()),
                                conciertoGuardar.getHora(), String.valueOf(conciertoGuardar.getLatitud()),
                                String.valueOf(conciertoGuardar.getLongitud()), String.valueOf(conciertoGuardar.getPrecio()),
                                "1","0");
                        // TODO añadir asistido y cancelado
                        Toast.makeText(this, R.string.added_concert, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }

                break;

            case R.id.btFecha:
                Calendar calendarioInicial = Calendar.getInstance();
                int ano = calendarioInicial.get(Calendar.YEAR);
                int mes = calendarioInicial.get(Calendar.MONTH);
                int dia = calendarioInicial.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        tvFecha.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                }, ano, mes, dia);

                dpd.show();

                break;

            case R.id.btHora:
                Calendar horaInicial = Calendar.getInstance();
                int hora = horaInicial.get(Calendar.HOUR_OF_DAY);
                int minuto = horaInicial.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String formato = String.format("%2d:%2d",hourOfDay,minute);
                        //tvHora.setText(hourOfDay + ":" + minute);
                        tvHora.setText(formato);
                    }
                }, hora, minuto, true);

                tpd.show();

                break;

            default:

                break;
        }
    }

    //Utilizo este listener para que el usuario solo pueda seleccionar asistido o cancelado
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(chbAsistido.isChecked())
            chbCancelado.setClickable(false);
        else
            chbCancelado.setClickable(true);

        if(chbCancelado.isChecked())
            chbAsistido.setClickable(false);
        else
            chbAsistido.setClickable(true);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        // Cada vez que se tapee limpio los anteriores markers, para que solo haya uno
        mapaBox.clear();

        // Añadimos un marker donde toque
        LatLng posicionMarker = mapaBox.addMarker(new MarkerOptions().setPosition(point)).getPosition();
        conciertoGuardar.setLatitud(posicionMarker.getLatitude());
        conciertoGuardar.setLongitud(posicionMarker.getLongitude());
        isLugarAnadido = true;
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

    private class GuardaConcierto extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // Relleno la url con los datos para guardarlos
            restTemplate.getForObject(Constantes.SERVER_URL + "/guardar_concierto?grupos=" + strings[0] +
            "&fecha=" + strings[1] + "&hora=" + strings[2] + "&latitud=" + strings[3] +
            "&longitud=" + strings[4] + "&precio=" + strings[5] + "&asistido=" + strings[6] +
            "&cancelado=" + strings[7], Void.class);

            return null;
        }
    }
}
