package com.ueniweb.techsuperficial.nearbyme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.ImageLoader;
import com.ueniweb.techsuperficial.nearbyme.listener.PlaceClickListener;
import com.ueniweb.techsuperficial.nearbyme.model.Result;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {
    private Context mContext;
    ArrayList<Result> placeslist;
    PlaceClickListener placeClickListener;

    public PlacesAdapter(Context mContext,PlaceClickListener placeClickListener) {
        this.placeClickListener=placeClickListener;
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
        holder.conslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng=new LatLng(result.getGeometry().getLocation().getLat(),result.getGeometry().getLocation().getLng());
                placeClickListener.PlaceClicked(holder.getAdapterPosition(),latLng);
            }
        });
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
        @BindView(R.id.conslayout)
        ConstraintLayout conslayout;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
