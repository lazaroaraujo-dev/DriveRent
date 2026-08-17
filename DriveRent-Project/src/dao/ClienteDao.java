package dao;

import model.entities.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteDao implements PersistenciaDao<Cliente> {
    private static final String ARQUIVO = "data/cliente.json";
    private final List<Cliente> clienteDaoList;

    public ClienteDao() {
        this.clienteDaoList = new ArrayList<>();
    }

    @Override
    public void salvar(Cliente clienteObj){
        clienteDaoList.add(clienteObj);
    }

    @Override
    public Cliente buscarPorId(String id) {
        for (Cliente cliente : clienteDaoList){
            if (id.equals(cliente.getCpf())){
                return cliente;
            }
        }return null;
    }

    @Override
    public List<Cliente> listarTodos() {
        return this.clienteDaoList;
    }

    @Override
    public void atualizar(Cliente obj) {
         for (int i = 0; i < clienteDaoList.size(); i++) {
             if (clienteDaoList.get(i).getCpf().equals(obj.getCpf())) {
                 clienteDaoList.set(i, obj);
                 break;
            }
        }
    }

    @Override
    public void deletar(String id) {
        for (Cliente cliente : clienteDaoList){
            if (cliente.getCpf().equals(id)){
                clienteDaoList.remove(cliente);
                break;
            }
        }
    }
}
