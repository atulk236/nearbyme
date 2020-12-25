package com.ueniweb.techsuperficial.nearbyme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.ImageLoader;
import com.ueniweb.techsuperficial.nearbyme.model.Result;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {


    private Context mContext;
    ArrayList<Result> placeslist;

    public PlacesAdapter(Context mContext) {
        this.mContext = mContext;
        placeslist = new ArrayList<>();
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlacesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.places_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {
        Result result = placeslist.get(position);
        holder.titleTv.setText(result.getName());
        holder.titleDesTv.setText(result.getTypes().get(0));
        ImageLoader.loadImage(result.getIcon(), holder.placeIv, mContext);
    }

    @Override
    public int getItemCount() {
        return placeslist.size();
    }

    public void setList(ArrayList result) {
        placeslist.clear();
        placeslist.addAll(result);
        notifyDataSetChanged();
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.place_iv)
        ImageView placeIv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.title_des_tv)
        TextView titleDesTv;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
