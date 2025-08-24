package com.elearning.elearning_support.utils.converter;

import java.io.IOException;
import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GsonIntegerTypeAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter jsonWriter, Integer integer) throws IOException {
        if (Objects.isNull(integer)) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(integer);
    }

    @Override
    public Integer read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String stringValue = jsonReader.nextString();
        try {
            return Integer.valueOf(stringValue);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
