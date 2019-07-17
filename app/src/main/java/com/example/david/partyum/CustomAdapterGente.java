package com.example.david.partyum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterGente extends ArrayAdapter<User> {

    private ArrayList<User> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView ivFotoGente;
        TextView tvUsuarioGente;
        TextView tvNombreGente;
    }

    public CustomAdapterGente(ArrayList<User> data, Context context) {
        super(context, R.layout.gente_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapterGente.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapterGente.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.gente_item, parent, false);
            viewHolder.ivFotoGente = (ImageView) convertView.findViewById(R.id.ivFotoGente);
            viewHolder.tvUsuarioGente = (TextView) convertView.findViewById(R.id.tvUsuarioGente);
            viewHolder.tvNombreGente = (TextView) convertView.findViewById(R.id.tvNombreGente);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapterGente.ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.tvUsuarioGente.setText(user.getUsuario());
        viewHolder.tvNombreGente.setText(user.getNombre()+" "+user.getApellidos());
        // Return the completed view to render on screen
        return convertView;
    }
}
