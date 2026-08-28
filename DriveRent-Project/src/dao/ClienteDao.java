package dao;

import com.google.gson.reflect.TypeToken;
import model.entities.Cliente;
import persistence.JsonDataManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao implements PersistenciaDao<Cliente> {

    private static final String ARQUIVO = "data/clientes.json";

    private final List<Cliente> clientes;
    private final JsonDataManager jsonDataManager;

    public ClienteDao() {
        this.jsonDataManager = new JsonDataManager();

        Type tipoLista = new TypeToken<List<Cliente>>() {}.getType();

        List<Cliente> dados;

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
                    "Não foi possível carregar os clientes.",
                    e
            );
        }

        this.clientes = dados;
    }

    @Override
    public void salvar(Cliente cliente) {
        clientes.add(cliente);
        persistir();
    }

    @Override
    public Cliente buscarPorId(String id) {
        return clientes.stream()
                .filter(c -> c.getCpf().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    @Override
    public void atualizar(Cliente cliente) {

        for (int i = 0; i < clientes.size(); i++) {

            if (clientes.get(i).getCpf().equals(cliente.getCpf())) {
                clientes.set(i, cliente);
                persistir();
                return;
            }
        }
    }

    @Override
    public void deletar(String id) {

        clientes.removeIf(c -> c.getCpf().equals(id));

        persistir();
    }

    private void persistir() {
        jsonDataManager.salvar(clientes, ARQUIVO);
    }
}