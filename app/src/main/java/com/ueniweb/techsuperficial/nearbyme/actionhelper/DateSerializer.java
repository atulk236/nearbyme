package com.ueniweb.techsuperficial.nearbyme.actionhelper;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joda.time.DateTime;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateSerializer implements JsonSerializer<DateTime> {

    @Override
    public JsonElement serialize(final DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5.30"));
        return src == null ? null : new JsonPrimitive(sdf.format(src.toDate()));
    }
}