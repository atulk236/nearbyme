package com.ueniweb.techsuperficial.nearbyme.actionhelper;

import android.content.Context;
import android.content.SharedPreferences;

public class FilterSession {
    public static final String PREFER_NAME = "Filter";
    public static final String FILTER_SELECTED="Selectedfilter";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public FilterSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getData(String id) {
        return pref.getString(id, "");
    }

    public void setData(String id, String val) {
        editor.putString(id, val);
        editor.commit();
    }


    public void clearData(){
        editor.clear();
        editor.commit();
    }

}
