package DTOs;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {
    public LocalDate localDate;
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("year", localDate.getYear());
        jsonObject.addProperty("month", localDate.getMonthValue());
        jsonObject.addProperty("day", localDate.getDayOfMonth());
        return jsonObject;
    }
}
