package com.ueniweb.techsuperficial.nearbyme.actionhelper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateDeserializer implements JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        if (element == null)
            return null;
        String date = element.getAsString();
        if (date != null) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("IST"));
            try {
                return new DateTime(sdf.parse(date).getTime());
            } catch (ParseException e) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                try {
                    return new DateTime(sdf1.parse(date).getTime())
                            .withZone(DateTimeZone.getDefault());
                } catch (ParseException ex) {
                    return null;
                }
            }
        } else return null;
    }
}