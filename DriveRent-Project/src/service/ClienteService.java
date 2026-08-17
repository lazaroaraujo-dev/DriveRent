package service;

import dao.PersistenciaDao;
import exception.DadosInvalidosException;
import model.entities.Cliente;

public class ClienteService {
    private final PersistenciaDao<Cliente> clienteDao;

    public ClienteService(PersistenciaDao<Cliente> clienteDao){
        this.clienteDao = clienteDao;
    }

    public void cadastrar(Cliente cliente){
        if (cliente==null){
            throw new DadosInvalidosException("Os dados do cliente não podem ser nulos.");
        }

        // Validação do CPF

        if (cliente.getCpf() == null || cliente.getCpf().isEmpty()){
            throw new DadosInvalidosException("O CPF do cliente é obrigatório.");
        }
        if (cliente.getCpf().length() != 11 || !cliente.getCpf().matches("\\d+")){
            throw new DadosInvalidosException("O CPF deve conter exatamente 11 números!");
        }
        if (cliente.getNome() == null || cliente.getNome().isEmpty()){
            throw new DadosInvalidosException("O nome do cliente é obrigatório.");
        }
        if (clienteDao.buscarPorId(cliente.getCpf()) != null){
            throw new DadosInvalidosException("Já existe um cliente cadastrado com este CPF.");
        }
        clienteDao.salvar(cliente);
    }
}
