package com.vpr.practica.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import com.vpr.practica.R;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.util.Util;

import java.text.ParseException;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.vpr.practica.util.Constantes.ASISTIDO;
import static com.vpr.practica.util.Constantes.CANCELADO;
import static com.vpr.practica.util.Constantes.CARTEL;
import static com.vpr.practica.util.Constantes.DATABASE_NAME;
import static com.vpr.practica.util.Constantes.FECHA;
import static com.vpr.practica.util.Constantes.GRUPOS;
import static com.vpr.practica.util.Constantes.HORA;
import static com.vpr.practica.util.Constantes.LUGAR;
import static com.vpr.practica.util.Constantes.PRECIO;
import static com.vpr.practica.util.Constantes.TABLA_CONCIERTO;
import static com.vpr.practica.util.Constantes.VERSION;

public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creo la tabla conciertos
        db.execSQL("CREATE TABLE " + TABLA_CONCIERTO +
        "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        GRUPOS + " TEXT, " +
        FECHA + " TEXT, " +
        HORA + " TEXT, " +
        LUGAR + " TEXT, " +
        PRECIO + " REAL, " +
        CARTEL + " BLOB, " +
        ASISTIDO + " INTEGER, " +
        CANCELADO + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CONCIERTO);
        onCreate(db);
    }

    public void guardar(Concierto concierto){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GRUPOS, concierto.getGrupos());
        if(concierto.getFecha() == null)
            values.put(FECHA, R.string.date_missing);
        else
            values.put(FECHA, Util.formateaFecha(concierto.getFecha()));
        values.put(HORA, concierto.getHora());
        values.put(LUGAR, concierto.getLugar());
        values.put(PRECIO, concierto.getPrecio());
        values.put(CARTEL, Util.getBytes(concierto.getCartel()));
        values.put(ASISTIDO, Util.setBooleanDb(concierto.isAsistido()));
        values.put(CANCELADO, Util.setBooleanDb(concierto.isCancelado()));

        db.insertOrThrow(TABLA_CONCIERTO, null, values);
        db.close();
    }

    public void eliminarConcierto(Concierto concierto){
        SQLiteDatabase db = getWritableDatabase();

        String[] argumentos = new String[]{String.valueOf(concierto.getId())};
        db.delete(TABLA_CONCIERTO, _ID + " = ?", argumentos);
        db.close();
    }

    public void modificarConcierto(Concierto concierto){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GRUPOS, concierto.getGrupos());
        if(concierto.getFecha() == null)
            values.put(FECHA, R.string.date_missing);
        else
            values.put(FECHA, Util.formateaFecha(concierto.getFecha()));
        values.put(HORA, concierto.getHora());
        values.put(LUGAR, concierto.getLugar());
        values.put(PRECIO, concierto.getPrecio());
        values.put(CARTEL, Util.getBytes(concierto.getCartel()));
        values.put(ASISTIDO, Util.setBooleanDb(concierto.isAsistido()));
        values.put(CANCELADO, Util.setBooleanDb(concierto.isCancelado()));

        String[] argumentos = new String[]{String.valueOf(concierto.getId())};
        db.update(TABLA_CONCIERTO, values, _ID + " = ?", argumentos);
        db.close();
    }

    /**
     * Devuelve un ArrayList de los conciertos que no estan ni cancelados ni asistidos
     * @return
     */
    public ArrayList<Concierto> getConciertos(){
        final String[] SELECT = {_ID, GRUPOS, FECHA, HORA, LUGAR, PRECIO, CARTEL,
                ASISTIDO, CANCELADO};
        final String WHERE = ASISTIDO + " == 0 AND " + CANCELADO + " == 0";
        final String ORDER_BY = "fecha";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_CONCIERTO, SELECT, WHERE, null, null, null, ORDER_BY);

        ArrayList<Concierto> listaConciertos = new ArrayList<Concierto>();
        Concierto concierto = null;
        while(cursor.moveToNext()){
            concierto = new Concierto();
            concierto.setId(cursor.getLong(0));
            concierto.setGrupos(cursor.getString(1));
            try {
                concierto.setFecha(Util.parsearFecha(cursor.getString(2)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            concierto.setHora(cursor.getString(3));
            concierto.setLugar(cursor.getString(4));
            concierto.setPrecio(cursor.getFloat(5));
            concierto.setCartel(Util.getBitmap(cursor.getBlob(6)));
            concierto.setAsistido(Util.getBoolean(cursor.getInt(7)));
            concierto.setCancelado(Util.getBoolean(cursor.getInt(8)));

            listaConciertos.add(concierto);
        }
        cursor.close();
        db.close();
        return listaConciertos;
    }

    /**
     * Devuelve un ArrayList de los conciertos asistidos o cancelados
     * @return
     */
    public ArrayList<Concierto> getConciertosEstado(){
        final String[] SELECT = {_ID, GRUPOS, FECHA, HORA, LUGAR, PRECIO, CARTEL,
                ASISTIDO, CANCELADO};
        final String WHERE = ASISTIDO + " == 1 OR " + CANCELADO + " == 1";
        final String ORDER_BY = "fecha";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_CONCIERTO, SELECT,WHERE,null,null,null,ORDER_BY);

        ArrayList<Concierto> conciertos = new ArrayList<Concierto>();
        Concierto concierto = null;
        while(cursor.moveToNext()){
            concierto = new Concierto();
            concierto = new Concierto();
            concierto.setId(cursor.getLong(0));
            concierto.setGrupos(cursor.getString(1));
            try {
                concierto.setFecha(Util.parsearFecha(cursor.getString(2)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            concierto.setHora(cursor.getString(3));
            concierto.setLugar(cursor.getString(4));
            concierto.setPrecio(cursor.getFloat(5));
            concierto.setCartel(Util.getBitmap(cursor.getBlob(6)));
            concierto.setAsistido(Util.getBoolean(cursor.getInt(7)));
            concierto.setCancelado(Util.getBoolean(cursor.getInt(8)));

            conciertos.add(concierto);
        }

        cursor.close();
        db.close();
        return conciertos;
    }

    /**
     * Devuelve el numero de conciertos asistidos
     * @return
     */
    public int getConciertosAsistidos(){
        final String[] SELECT = {"COUNT(*)"};
        final String WHERE = ASISTIDO + " == 1";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_CONCIERTO, SELECT, WHERE, null,null,null,null);
        cursor.moveToNext();

        return cursor.getInt(0);
    }

    public int getConciertosCancelados(){
        final String[] SELECT = {"COUNT(*)"};
        final String WHERE = CANCELADO + " == 1";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_CONCIERTO, SELECT, WHERE, null, null, null, null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }
}
