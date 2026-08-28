package persistence;

import com.google.gson.*;
import model.entities.CarroPasseio;
import model.entities.Moto;
import model.entities.Utilitario;
import model.entities.Veiculo;

import java.lang.reflect.Type;

public class VeiculoTypeAdapter implements JsonSerializer<Veiculo>, JsonDeserializer<Veiculo> {

    @Override
    public JsonElement serialize(
            Veiculo veiculo,
            Type tipo,
            JsonSerializationContext contexto) {

        JsonObject json = contexto.serialize(
                veiculo,
                veiculo.getClass()
        ).getAsJsonObject();

        if (veiculo instanceof Moto) {
            json.addProperty("tipo", "MOTO");

        } else if (veiculo instanceof CarroPasseio) {
            json.addProperty("tipo", "CARRO_PASSEIO");

        } else if (veiculo instanceof Utilitario) {
            json.addProperty("tipo", "UTILITARIO");

        } else {
            throw new JsonParseException(
                    "Tipo de veículo não suportado."
            );
        }

        return json;
    }

    @Override
    public Veiculo deserialize(
            JsonElement json,
            Type tipo,
            JsonDeserializationContext contexto) throws JsonParseException {

        JsonObject objeto = json.getAsJsonObject();

        JsonElement tipoElement = objeto.get("tipo");

        if (tipoElement == null) {
            throw new JsonParseException(
                    "O veículo não possui o campo 'tipo'."
            );
        }

        String tipoVeiculo = tipoElement.getAsString();

        return switch (tipoVeiculo) {

            case "MOTO" ->
                    contexto.deserialize(objeto, Moto.class);

            case "CARRO_PASSEIO" ->
                    contexto.deserialize(objeto, CarroPasseio.class);

            case "UTILITARIO" ->
                    contexto.deserialize(objeto, Utilitario.class);

            default ->
                    throw new JsonParseException(
                            "Tipo de veículo desconhecido: " + tipoVeiculo
                    );
        };
    }
}