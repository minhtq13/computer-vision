package com.elearning.elearning_support.utils.converter;

import java.io.IOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GsonLongTypeAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter jsonWriter, Long aLong) throws IOException {
        if (aLong == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(aLong);
    }

    @Override
    public Long read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String stringValue = jsonReader.nextString();
        try {
            return Long.valueOf(stringValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
