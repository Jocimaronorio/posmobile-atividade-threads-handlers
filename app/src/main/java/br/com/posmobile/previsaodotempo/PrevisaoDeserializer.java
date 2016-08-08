package br.com.posmobile.previsaodotempo;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.posmobile.previsaodotempo.Previsao;

/**
 * Created by alexandre on 07/08/16.
 */

public class PrevisaoDeserializer implements JsonDeserializer<Previsao> {
    @Override
    public Previsao deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Previsao previsaoTemp = new Previsao();
        previsaoTemp.setPeriodo(jsonObject.getAsJsonPrimitive("dt").getAsLong() * 1000);
        previsaoTemp.setTemperatura(jsonObject.getAsJsonObject("temp").getAsJsonPrimitive("day").getAsString() + "°");
        previsaoTemp.setIcone(jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().getAsJsonPrimitive("icon").getAsString());
        previsaoTemp.setDescricao(jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().getAsJsonPrimitive("description").getAsString());
        return previsaoTemp;
    }
}
