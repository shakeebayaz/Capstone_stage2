package com.digital.ayaz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digital.ayaz.Utils.Constants;
import com.digital.ayaz.activity.PlaceDetailActivity;
import com.digital.ayaz.R;
import com.digital.ayaz.Utils.PlaceListDetail;

import java.util.List;

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> {

    Context context;
    private LayoutInflater mInflater;
    List<PlaceListDetail> list;
    int choice;

    public PlaceListAdapter(Context c, List<PlaceListDetail> list, int choice) {
        this.context = c;
        this.list = list;
        mInflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.choice = choice;
    }

    @Override
    public PlaceListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycle_view_item, viewGroup, false);
        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place_id = list.get(vh.getAdapterPosition()).getPlace_id();

                Intent intent = new Intent(context, PlaceDetailActivity.class);
                intent.putExtra("place_id", place_id);
                intent.putExtra("choice", choice);
                context.startActivity(intent);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(PlaceListAdapter.ViewHolder viewHolder, int i) {
        PlaceListDetail placeListDetail = list.get(i);
        String url;
        if (placeListDetail.getPhoto_endPointUrl() != null) {
            url= Constants.PLACE_API_SMALL_IMAGE_URL+placeListDetail.getPhoto_endPointUrl().get(0)+"&key="+context.getResources().getString(R.string.google_key);
        } else {
            url = placeListDetail.getIcon_url();
        }
        viewHolder.place_address.setText(placeListDetail.getPlace_address());
        viewHolder.place_id.setText(placeListDetail.getPlace_id());
        viewHolder.place_name.setText(placeListDetail.getPlace_name());
        if (placeListDetail.getPlace_rating() != null) {
            viewHolder.rating.setRating(Float.parseFloat(String.valueOf(placeListDetail.getPlace_rating())));
        }

        Glide.with(context).load(url).into(viewHolder.place_pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView place_address, place_name, place_id;
        ImageView place_pic;
        RatingBar rating;

        public ViewHolder(View itemView) {
            super(itemView);

            place_address = (TextView) itemView.findViewById(R.id.place_Address);
            place_name = (TextView) itemView.findViewById(R.id.place_name);
            place_id = (TextView) itemView.findViewById(R.id.place_id);
            place_pic = (ImageView) itemView.findViewById(R.id.place_pic);
            rating = (RatingBar) itemView.findViewById(R.id.rating);

        }
    }
}
