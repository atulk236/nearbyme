package com.ueniweb.techsuperficial.nearbyme.actionhelper;


import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowDialog {

    private static SweetAlertDialog sweetAlertDialog;

    public static void showSweetDialog(Context context, String title, String message, int alertType) {
        dismissSweetDialog();
        sweetAlertDialog = new SweetAlertDialog(context, alertType);
        sweetAlertDialog.setTitleText(title)
                .setContentText(message)
                .setCancelable(true);
        sweetAlertDialog.show();
    }

    public static void dismissSweetDialog() {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
    }

    public static void showErrorDialog(Context context, String message) {
        showSweetDialog(context, "Error", message, SweetAlertDialog.ERROR_TYPE);
    }

}