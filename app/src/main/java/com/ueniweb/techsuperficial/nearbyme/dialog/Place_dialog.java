package com.ueniweb.techsuperficial.nearbyme.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ueniweb.techsuperficial.nearbyme.R;
import com.ueniweb.techsuperficial.nearbyme.actionhelper.ImageLoader;
import com.ueniweb.techsuperficial.nearbyme.model.Result;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Place_dialog extends Dialog {
    @BindView(R.id.place_iv)
    ImageView placeIv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_des_tv)
    TextView titleDesTv;
    String resultstr;
    Result result;
    Context mcontext;


    public Place_dialog(@NonNull Context context, Result result) {
        super(context);
        this.mcontext = context;
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.place_dialog);
        ButterKnife.bind(this);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER|Gravity.TOP);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setview();
    }

    private void setview() {
        titleTv.setText(result.getName());
        titleDesTv.setText(result.getTypes().get(0));
        ImageLoader.loadImage(result.getIcon(), placeIv, mcontext);
    }
}
