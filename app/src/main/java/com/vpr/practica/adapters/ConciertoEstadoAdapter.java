package com.vpr.practica.adapters;

import android.content.Context;
import android.graphics.Color;
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

public class ConciertoEstadoAdapter extends BaseAdapter {
    //Atributos
    private List<Concierto> listaConciertosEstado;
    private int idLayout;
    private LayoutInflater inflater;
    private String sinFecha;

    //Constructor
    public ConciertoEstadoAdapter(Context contexto, int idLayout, List<Concierto> listaConciertosEstado){
        inflater = LayoutInflater.from(contexto);
        this.idLayout = idLayout;
        this.listaConciertosEstado = listaConciertosEstado;
        sinFecha = contexto.getResources().getString(R.string.date_missing);
    }

    static class ViewHolder{
        //ImageView ivCartel;
        TextView tvEstado;
        TextView tvGrupos;
        TextView tvTiempo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            convertView = inflater.inflate(idLayout, null);

            //referencio a los componentes
            holder = new ViewHolder();
            //holder.ivCartel = convertView.findViewById(R.id.ivCartel);
            holder.tvEstado = convertView.findViewById(R.id.tvEstado);
            holder.tvGrupos = convertView.findViewById(R.id.tvGrupos);
            holder.tvTiempo = convertView.findViewById(R.id.tvTiempo);

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        Concierto concierto = listaConciertosEstado.get(position);
        boolean estado = concierto.isAsistido();
        if(estado) {
            holder.tvEstado.setTextColor(Color.GREEN);
            holder.tvEstado.setText(R.string.assisted);
        }
        else {
            holder.tvEstado.setTextColor(Color.RED);
            holder.tvEstado.setText(R.string.canceled);
        }
        holder.tvGrupos.setText(concierto.getGrupos());

        if(concierto.getFecha() == null)
            holder.tvTiempo.setText(sinFecha + " - " +
                    concierto.getHora());
        else
            holder.tvTiempo.setText(Util.formateaFecha(concierto.getFecha()) + " - " +
                    concierto.getHora());

        //holder.ivCartel.setImageBitmap(concierto.getCartel());

        return convertView;
    }

    @Override
    public int getCount() {
        return listaConciertosEstado.size();
    }

    @Override
    public Object getItem(int position) {
        return listaConciertosEstado.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaConciertosEstado.get(position).getId();
    }


}
