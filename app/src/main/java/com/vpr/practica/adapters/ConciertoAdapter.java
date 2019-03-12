package com.vpr.practica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vpr.practica.R;
import com.vpr.practica.base.Concierto;
import com.vpr.practica.util.Util;

import java.util.List;

public class ConciertoAdapter extends BaseAdapter {
    //Atributos
    private List<Concierto> listConciertos;
    private LayoutInflater inflater;
    private int idLayout;
    private String sinFecha;

    //Constructor
    public ConciertoAdapter(Context contexto, int idLayout, List<Concierto> listConciertos){
        inflater = LayoutInflater.from(contexto);
        this.idLayout = idLayout;
        this.listConciertos = listConciertos;
        sinFecha = contexto.getResources().getString(R.string.date_missing);
    }

    static class ViewHolder{
        //ImageView ivCartel;
        TextView tvGrupos;
        TextView tvTiempo;
        TextView tvLugar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(idLayout, null);
            //Referencio a los componentes
            holder = new ViewHolder();
            //holder.ivCartel = convertView.findViewById(R.id.ivCartel);
            holder.tvGrupos = convertView.findViewById(R.id.tvGrupos);
            holder.tvTiempo = convertView.findViewById(R.id.tvTiempo);
            holder.tvLugar = convertView.findViewById(R.id.tvLugar);

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Concierto concierto = listConciertos.get(position);
        holder.tvGrupos.setText(concierto.getGrupos());
        //holder.ivCartel.setImageBitmap(concierto.getCartel());
        if(concierto.getFecha() == null)
            holder.tvTiempo.setText(sinFecha + " - " +
                    concierto.getHora());
        else
            holder.tvTiempo.setText(Util.formateaFecha(concierto.getFecha()) + " - " +
            concierto.getHora());
        holder.tvLugar.setText(concierto.getLugar() + " - " + concierto.getPrecio() + "€");

        return convertView;
    }

    @Override
    public int getCount() {
        return listConciertos.size();
    }

    @Override
    public Object getItem(int position) {
        return listConciertos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listConciertos.get(position).getId();
    }
}
