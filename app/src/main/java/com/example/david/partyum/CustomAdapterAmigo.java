package com.example.david.partyum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterAmigo extends ArrayAdapter<User> {

    private ArrayList<User> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView ivFotoAmigo;
        TextView tvUsuarioAmigo;
        TextView tvNombreAmigo;
        ImageView ivConectado;
    }

    public CustomAdapterAmigo(ArrayList<User> data, Context context) {
        super(context, R.layout.amigos_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapterAmigo.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapterAmigo.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.amigos_item, parent, false);
            viewHolder.ivFotoAmigo = (ImageView) convertView.findViewById(R.id.ivFotoAmigo);
            viewHolder.tvUsuarioAmigo = (TextView) convertView.findViewById(R.id.tvUsuarioAmigo);
            viewHolder.tvNombreAmigo = (TextView) convertView.findViewById(R.id.tvNombreAmigo);
            viewHolder.ivConectado = (ImageView) convertView.findViewById(R.id.ivConectado);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapterAmigo.ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.tvUsuarioAmigo.setText(user.getUsuario());
        viewHolder.tvNombreAmigo.setText(user.getNombre()+" "+user.getApellidos());
        if (user.getConectado()==1){
            viewHolder.ivConectado.setImageResource(R.drawable.conectado);
        } else {
            viewHolder.ivConectado.setImageResource(R.drawable.desconectado);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
