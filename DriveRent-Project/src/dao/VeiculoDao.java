package dao;

import com.google.gson.reflect.TypeToken;
import model.entities.Veiculo;
import persistence.JsonDataManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDao implements PersistenciaDao<Veiculo> {

    private static final String ARQUIVO = "data/veiculos.json";

    private static final Type TIPO_LISTA =
            new TypeToken<List<Veiculo>>() {}.getType();

    private final List<Veiculo> veiculos;
    private final JsonDataManager jsonDataManager;

    public VeiculoDao() {
        this.jsonDataManager = new JsonDataManager();

        List<Veiculo> dados;

        try {
            dados = jsonDataManager.carregar(
                    ARQUIVO,
                    TIPO_LISTA
            );

            if (dados == null) {
                dados = new ArrayList<>();
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(
                    "Não foi possível carregar os veículos.",
                    e
            );
        }

        this.veiculos = dados;
    }

    @Override
    public void salvar(Veiculo veiculo) {
        veiculos.add(veiculo);
        persistir();
    }

    @Override
    public Veiculo buscarPorId(String id) {
        return veiculos.stream()
                .filter(v -> v.getPlaca().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Veiculo> listarTodos() {
        return new ArrayList<>(veiculos);
    }

    @Override
    public void atualizar(Veiculo veiculo) {

        for (int i = 0; i < veiculos.size(); i++) {

            if (veiculos.get(i)
                    .getPlaca()
                    .equals(veiculo.getPlaca())) {

                veiculos.set(i, veiculo);
                persistir();
                return;
            }
        }
    }

    @Override
    public void deletar(String id) {
        veiculos.removeIf(
                v -> v.getPlaca().equals(id)
        );

        persistir();
    }

    private void persistir() {
        jsonDataManager.salvar(
                veiculos,
                ARQUIVO,
                TIPO_LISTA
        );
    }
}