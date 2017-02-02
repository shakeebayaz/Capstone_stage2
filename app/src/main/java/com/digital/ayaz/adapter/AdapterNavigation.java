package com.digital.ayaz.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digital.ayaz.R;

public class AdapterNavigation extends ArrayAdapter<String> {

    Context ctx;
    LayoutInflater inflater;
    int resourceId;
    private String[] names;
    private int[] icons = {R.drawable.ic_location, R.drawable.ic_hotel, R.drawable.ic_food};

    public AdapterNavigation(Context context, int resource, String[] name) {
        super(context, resource);

        this.ctx = context;
        this.resourceId = resource;
        this.names = name;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public String getItem(int position) {
        return names[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTxt.setText(names[position]);
        holder.nameTxt.setCompoundDrawables(ContextCompat.getDrawable(ctx, icons[position]), null, null, null);
        return convertView;
    }

    class ViewHolder {
        TextView nameTxt;
        ImageView imageView;

        ViewHolder(View view) {
            nameTxt = (TextView) view.findViewById(R.id.text_navigationlist);
        }


    }
}

