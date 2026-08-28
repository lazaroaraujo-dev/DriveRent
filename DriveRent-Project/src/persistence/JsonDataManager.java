package persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.entities.Veiculo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonDataManager {

    private final Gson gson;

    public JsonDataManager() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(
                        Veiculo.class,
                        new VeiculoTypeAdapter()
                )
                .registerTypeAdapter(
                        java.time.LocalDate.class,
                        new LocalDateTypeAdapter()
                )
                .create();
    }

    // SALVAR - versão normal
    // Usada por ClienteDao e outros objetos sem polimorfismo

    public <T> void salvar(T objeto, String arquivo) {

        File file = new File(arquivo);

        System.out.println(
                "SALVANDO ARQUIVO: " + file.getAbsolutePath()
        );

        File diretorio = file.getParentFile();

        if (diretorio != null && !diretorio.exists()) {
            diretorio.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {

            gson.toJson(objeto, writer);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao salvar dados no arquivo JSON.",
                    e
            );
        }
    }
    // SALVAR - versão que recebe o Type
    // Necessária para List<Veiculo>, devido à herança/polimorfismo
    public <T> void salvar(
            T objeto,
            String arquivo,
            Type tipo
    ) {

        File file = new File(arquivo);

        System.out.println(
                "SALVANDO ARQUIVO: " + file.getAbsolutePath()
        );

        File diretorio = file.getParentFile();

        if (diretorio != null && !diretorio.exists()) {
            diretorio.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {

            gson.toJson(
                    objeto,
                    tipo,
                    writer
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao salvar dados no arquivo JSON.",
                    e
            );
        }
    }

    public <T> T carregar(
            String arquivo,
            Type tipo) {

        File file = new File(arquivo);

        System.out.println(
                "Arquivo: " + file.getAbsolutePath()
        );

        System.out.println(
                "Existe? " + file.exists()
        );

        if (!file.exists()) {

            System.out.println(
                    "Arquivo não existe. Retornando null."
            );

            return null;
        }

        try (FileReader reader = new FileReader(file)) {

            return gson.fromJson(
                    reader,
                    tipo
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao carregar dados do arquivo JSON.",
                    e
            );
        }
    }
}