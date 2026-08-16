package dao;

import java.util.List;

public interface PersistenciaDao<T> {
    void salvar(T obj);

    T buscarPorId(String cpf);

    List<T> listarTodos();

    void atualizar(T obj);

    void deletar(String id);
}
