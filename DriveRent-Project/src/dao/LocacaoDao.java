package dao;

import com.google.gson.reflect.TypeToken;
import model.entities.Locacao;
import persistence.JsonDataManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocacaoDao implements PersistenciaDao<Locacao> {

    private static final String ARQUIVO = "data/locacoes.json";

    private final List<Locacao> locacoes;
    private final JsonDataManager jsonDataManager;

    public LocacaoDao() {
        this.jsonDataManager = new JsonDataManager();

        Type tipoLista = new TypeToken<List<Locacao>>() {}.getType();

        List<Locacao> dados;

        try {
            dados = jsonDataManager.carregar(
                    ARQUIVO,
                    tipoLista
            );

            if (dados == null) {
                dados = new ArrayList<>();
            }

        } catch (RuntimeException e) {
            throw new RuntimeException(
                    "Não foi possível carregar as locações.",
                    e
            );
        }

        this.locacoes = dados;
    }

    @Override
    public void salvar(Locacao locacao) {
        locacoes.add(locacao);
        persistir();
    }

    @Override
    public Locacao buscarPorId(String id) {
        return locacoes.stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Locacao> listarTodos() {
        return new ArrayList<>(locacoes);
    }

    @Override
    public void atualizar(Locacao locacao) {

        for (int i = 0; i < locacoes.size(); i++) {

            if (locacoes.get(i)
                    .getId()
                    .equals(locacao.getId())) {

                locacoes.set(i, locacao);
                persistir();
                return;
            }
        }
    }

    @Override
    public void deletar(String id) {
        locacoes.removeIf(
                l -> l.getId().equals(id)
        );

        persistir();
    }

    private void persistir() {
        jsonDataManager.salvar(
                locacoes,
                ARQUIVO
        );
    }
}