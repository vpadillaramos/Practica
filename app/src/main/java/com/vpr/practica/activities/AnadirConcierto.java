package com.vpr.practica.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vpr.practica.R;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.db.Database;
import com.vpr.practica.util.Constantes;
import com.vpr.practica.util.Util;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Calendar;

public class AnadirConcierto extends Activity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {
    //Constantes
    private static final int CARTEL = 1;
    private final int RES_IMAGEN_DEFAULT = R.mipmap.ic_launcher_round;

    //Atributos
    private String accion;
    private long idConcierto;
    private boolean flagImagenSeleccionada = false;
    private CheckBox chbAsistido;
    private CheckBox chbCancelado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asigno un layout u otro dependiento de la orientacion del dispositivo
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setContentView(R.layout.activity_anadir_concierto);

        //En este modo el icono por defecto no se ve, pero se puede pulsar para seleccionar una imagen
        //El editor avisa que en landscape puede dar algun error
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.landscpae_activity_anadir_concierto);



        //Componentes
        Button btGuardar = findViewById(R.id.btGuardar);
        Button btFecha = findViewById(R.id.btFecha);
        Button btHora = findViewById(R.id.btHora);
        //ImageView ivCartel = findViewById(R.id.ivCartel);
        chbAsistido = findViewById(R.id.chbAsistido);
        chbCancelado = findViewById(R.id.chbCancelado);

        // Para mostrar el mapa
        Button btUbicacion = findViewById(R.id.btUbicacion);

        //Listeners
        btUbicacion.setOnClickListener(this);
        btGuardar.setOnClickListener(this);
        btFecha.setOnClickListener(this);
        btHora.setOnClickListener(this);
        //ivCartel.setOnClickListener(this);
        chbAsistido.setOnCheckedChangeListener(this);
        chbCancelado.setOnCheckedChangeListener(this);

        accion = getIntent().getStringExtra("accion");
        if(accion.equals("modificar")){
            Concierto concierto = (Concierto) getIntent().getSerializableExtra("concierto");
            //Bitmap cartel = Util.getBitmap(getIntent().getByteArrayExtra("cartel"));
            //rellenarCampos(concierto, cartel);
        }
    }

    /**
     * Metodo que rellena los componentes del activity añadir concierto para ser modificado
     * @param concierto
     * @param
     */
    //public void rellenarCampos(Concierto concierto, Bitmap cartel){
    public void rellenarCampos(Concierto concierto){
        //Componentes
        EditText etGrupos = findViewById(R.id.etGrupos);
        TextView tvFecha = findViewById(R.id.tvFecha);
        TextView tvHora = findViewById(R.id.tvHora);
        //EditText etLugar = findViewById(R.id.etLugar);
        EditText etPrecio = findViewById(R.id.etPrecio);
        //ImageView ivCartel = findViewById(R.id.ivCartel);

        //Relleno los campos
        idConcierto = concierto.getId();
        etGrupos.setText(concierto.getGrupos());
        if(concierto.getFecha() == null)
            tvFecha.setText(getResources().getString(R.string.date_missing));
        else
            tvFecha.setText(Util.formateaFecha(concierto.getFecha()));
        tvHora.setText(concierto.getHora());
        //etLugar.setText(concierto.getLugar());
        etPrecio.setText(String.valueOf(concierto.getPrecio()));
        chbAsistido.setChecked(concierto.isAsistido());
        chbCancelado.setChecked(concierto.isCancelado());
        //ivCartel.setImageBitmap(cartel);
    }

    public void limpiar(){
        //Componentes
        EditText etGrupos = findViewById(R.id.etGrupos);
        TextView tvFecha = findViewById(R.id.tvFecha);
        TextView tvHora = findViewById(R.id.tvHora);
        //EditText etLugar = findViewById(R.id.etLugar);
        EditText etPrecio = findViewById(R.id.etPrecio);
        CheckBox chbAsistido = findViewById(R.id.chbAsistido);
        CheckBox chbCancelado = findViewById(R.id.chbCancelado);
        //ImageView ivCartel = findViewById(R.id.ivCartel);

        //limpio
        etGrupos.setText("");
        tvFecha.setText("");
        tvHora.setText("");
        //etLugar.setText("");
        etPrecio.setText("");
        chbAsistido.setChecked(false);
        chbCancelado.setChecked(false);
        //ivCartel.setImageBitmap(null); //quito la imagen elegida por el usuario
        //ivCartel.setBackgroundResource(RES_IMAGEN_DEFAULT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btUbicacion:
                Intent intentMapa = new Intent(this, MapaActivity.class);
                startActivity(intentMapa);
                break;
            case R.id.btGuardar:
                //Componentes
                EditText etGrupos = findViewById(R.id.etGrupos);
                TextView tvFecha = findViewById(R.id.tvFecha);
                TextView tvHora = findViewById(R.id.tvHora);
                //EditText etLugar = findViewById(R.id.etLugar);
                EditText etPrecio = findViewById(R.id.etPrecio);
                CheckBox chbAsistido = findViewById(R.id.chbAsistido);
                CheckBox chbCancelado = findViewById(R.id.chbCancelado);
                //ImageView ivCartel = findViewById(R.id.ivCartel);

                //CONTROL DE DATOS
                if(etGrupos.getText().toString().equals("")){
                    etGrupos.requestFocus();
                    Toast.makeText(this, R.string.bands_missing, Toast.LENGTH_LONG).show();
                    return;
                }

                //si voy a modificar un concierto esto no hace falta ya que ya tiene una imagen
                /*if(!accion.equals("modificar")){
                    if(!flagImagenSeleccionada){
                        Toast.makeText(this, R.string.poster_missing, Toast.LENGTH_LONG).show();
                        return;
                    }
                }*/

                if(tvFecha.getText().toString().equals("")) {
                    if(!accion.equals("modificar")){
                        Toast.makeText(this, R.string.date_required, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if(tvHora.getText().toString().equals(""))
                    tvHora.setText(R.string.time_missing);

                /*if(etLugar.getText().toString().equals(""))
                    etLugar.setText(R.string.place_missing);*/

                if(etPrecio.getText().toString().equals(""))
                    etPrecio.setText(R.string.price_missing);

                if(!Util.isFloat(etPrecio.getText().toString())){
                    Toast.makeText(this, R.string.wrong_decimal_format, Toast.LENGTH_LONG).show();
                    return;
                }


                Concierto concierto = new Concierto();
                concierto.setGrupos(etGrupos.getText().toString());
                if(!tvFecha.getText().toString().equals(R.string.date_missing)){
                    try {
                        concierto.setFecha(Util.parsearFecha(tvFecha.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                    concierto.setFecha(null);

                concierto.setHora(tvHora.getText().toString());
                //concierto.setLugar(etLugar.getText().toString());
                try {
                    concierto.setPrecio(Util.parseDecimal(etPrecio.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //concierto.setCartel(((BitmapDrawable)ivCartel.getDrawable()).getBitmap());
                concierto.setAsistido(chbAsistido.isChecked());
                concierto.setCancelado(chbCancelado.isChecked());

                /*Database db = new Database(this);

                switch (accion){
                    case "modificar":
                        concierto.setId(idConcierto);
                        db.modificarConcierto(concierto);
                        Toast.makeText(this, R.string.modified_concert, Toast.LENGTH_LONG).show();
                        break;

                    case "nuevo":
                        db.guardar(concierto);
                        limpiar();
                        Toast.makeText(this, R.string.added_concert, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }*/

                switch (accion){
                    case "nuevo":
                        limpiar();
                        GuardaConcierto guardaConcierto = new GuardaConcierto();
                        guardaConcierto.execute(concierto.getGrupos(), Util.formateaFecha(concierto.getFecha()),
                                concierto.getHora(), String.valueOf(concierto.getLatitud()),
                                String.valueOf(concierto.getLongitud()), String.valueOf(concierto.getPrecio()),
                                "1","0");
                        // TODO añadir asistido y cancelado
                        Toast.makeText(this, R.string.added_concert, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }

                break;

            /*case R.id.ivCartel:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CARTEL);
                break;*/

            case R.id.btFecha:
                Calendar calendarioInicial = Calendar.getInstance();
                int ano = calendarioInicial.get(Calendar.YEAR);
                int mes = calendarioInicial.get(Calendar.MONTH);
                int dia = calendarioInicial.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        TextView tvFecha = findViewById(R.id.tvFecha);
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
                        TextView tvHora = findViewById(R.id.tvHora);
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

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if((resultCode == RESULT_OK) && (data != null)){
            switch (requestCode){
                case CARTEL:
                    //Obtengo el Uri de la imagen seleccionada
                    Uri imagenSeleccionada = data.getData();
                    String[] ruta = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(imagenSeleccionada, ruta,
                            null, null, null);
                    cursor.moveToFirst();

                    //Obtengo la ruta de la imagen
                    int indice = cursor.getColumnIndex(ruta[0]);
                    String rutaCartel = cursor.getString(indice);
                    cursor.close();

                    //Cargo la imagen en el ImageView
                    ImageView ivCartel = findViewById(R.id.ivCartel);
                    ivCartel.setImageBitmap(BitmapFactory.decodeFile(rutaCartel));
                    flagImagenSeleccionada = true;
                    break;

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

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
