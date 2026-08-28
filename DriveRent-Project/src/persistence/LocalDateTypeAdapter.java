package persistence;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateTypeAdapter
        implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public JsonElement serialize(
            LocalDate data,
            Type tipo,
            JsonSerializationContext contexto) {

        return new JsonPrimitive(data.toString());
    }

    @Override
    public LocalDate deserialize(
            JsonElement json,
            Type tipo,
            JsonDeserializationContext contexto) {

        return LocalDate.parse(json.getAsString());
    }
}