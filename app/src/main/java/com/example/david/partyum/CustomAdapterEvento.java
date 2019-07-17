package com.example.david.partyum;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapterEvento extends ArrayAdapter<Evento>{

    private ArrayList<Evento> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvFechaEvento;
        TextView tvTituloEvento;
        TextView tvDescripcionEvento;
        ImageView ivImagenEvento;
    }

    public CustomAdapterEvento(ArrayList<Evento> data, Context context) {
        super(context, R.layout.eventos_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Evento evento = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.eventos_item, parent, false);
            viewHolder.tvFechaEvento = (TextView) convertView.findViewById(R.id.tvFechaEvento);
            viewHolder.ivImagenEvento = (ImageView) convertView.findViewById(R.id.ivImagenEvento);
            viewHolder.tvTituloEvento = (TextView) convertView.findViewById(R.id.tvTituloEvento);
            viewHolder.tvDescripcionEvento = (TextView) convertView.findViewById(R.id.tvDescripcionEvento);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.tvFechaEvento.setText(evento.getFecha()+"  ("+evento.getHora()+")");
        viewHolder.tvTituloEvento.setText(evento.getTitulo());
        //viewHolder.tvDescripcionEvento.setText(evento.getDescripcion());
        // Return the completed view to render on screen
        return convertView;
    }
}

