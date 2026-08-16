package dao;

import java.util.List;

public interface DaoPersistencia<T> {
    void salvar(T obj);

    T buscarPorId(String cpf);

    List<T> listarTodos();

    void atualizar(T obj);

    void deletar(String id);
}
