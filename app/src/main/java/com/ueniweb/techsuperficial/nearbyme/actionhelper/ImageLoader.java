package com.ueniweb.techsuperficial.nearbyme.actionhelper;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class ImageLoader {

    public static void loadImage(String imageuri, ImageView iv, Context context) {
        if (!imageuri.isEmpty()){
            Picasso.with(context)
                    .load(imageuri)
                    .noPlaceholder()
//                .resize(512, 512)
                    .fit()
                    //  .error(R.drawable.)
                    .centerInside()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(iv, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(context)
                                    .load(imageuri)
                                    .noPlaceholder()
                                    /*.placeholder(R.drawable.)
                                    .error(R.drawable.)*/
                                    .fit()
                                    .centerInside()
                                    .into(iv, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
        }

        }

}
