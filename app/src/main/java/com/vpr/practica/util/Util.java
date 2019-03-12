package com.vpr.practica.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    /**
     * Metodo para guardar una fecha Date en DB como String
     * @param fecha Date
     * @return fecha String
     */
    public static String formateaFecha(Date fecha){
        if(fecha == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }

    /**
     * Metodo para obtener una fecha Date de una DB pasado como String
     * @param fecha String
     * @return fecha Date
     * @throws ParseException
     */
    public static Date parsearFecha(String fecha) throws ParseException {
        if(fecha == null || fecha.isEmpty())
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(fecha);
    }

    /**
     * Metodo para guardar una imagen Bitmap en DB como byte[]
     * @param imagen Bitmap
     * @return imagen byte[]
     */
    public static byte[] getBytes(Bitmap imagen){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.PNG, 0, bos);

        return bos.toByteArray();
    }

    /**
     * Metodo para obetener una imagen Bitmap de una DB pasada como byte[]
     * @param imagen byte[]
     * @return imagen Bitmap
     */
    public static Bitmap getBitmap(byte[] imagen){
        return BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
    }

    /**
     * Metodo usado para obtener de la DB un booleano
     * @param b
     * @return
     */
    public static boolean getBoolean(int b){
        return  b == 0 ? false:true;
    }

    public static void mensajeToast(Context context, String mensaje){
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
    }

    /**
     * Metodo para guardar un boolean en SQLite como 1 si es true, y 0 si es false
     * @param b
     * @return
     */
    public static int setBooleanDb(boolean b){
        return b ? 1:0;
    }

    /**
     * Comprueba el formato de una cadena para que sea decimal. Funciona con . o con , como separador
     * @param cadena
     * @return
     */
    public static boolean isFloat(String cadena) {
        boolean resultado = false;
        if(cadena.matches("\\d*\\.\\d*") || cadena.matches("\\d*\\,\\d*"))
            resultado = true;
        return resultado;
    }

    /**
     * Pasandole una cadena que ha sido comprobada con isFloat, admite la , como separador
     * y lo convierte en float
     * @param decimal
     * @return
     * @throws ParseException
     */
    public static float parseDecimal(String decimal) throws ParseException {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(dfs);
        return df.parse(decimal).floatValue();
    }
}
