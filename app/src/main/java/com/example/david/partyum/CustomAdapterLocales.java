package com.example.david.partyum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterLocales extends ArrayAdapter<Local> {

    private ArrayList<Local> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView ivFotoLocal;
        TextView tvNombreLocal;
        TextView tvDireccionLocal;
    }

    public CustomAdapterLocales(ArrayList<Local> data, Context context) {
        super(context, R.layout.locales_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Local local = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapterLocales.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapterLocales.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.locales_item, parent, false);
            viewHolder.ivFotoLocal = (ImageView) convertView.findViewById(R.id.ivFotoLocal);
            viewHolder.tvNombreLocal = (TextView) convertView.findViewById(R.id.tvNombreLocal);
            viewHolder.tvDireccionLocal = (TextView) convertView.findViewById(R.id.tvDireccionLocal);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapterLocales.ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.tvNombreLocal.setText(local.getNombre());
        viewHolder.tvDireccionLocal.setText(local.getDireccion());
        // Return the completed view to render on screen
        return convertView;
    }
}
