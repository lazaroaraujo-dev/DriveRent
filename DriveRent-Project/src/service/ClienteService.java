package service;

import dao.PersistenciaDao;
import exception.DadosInvalidosException;
import exception.EntidadeNaoEncontradaException;
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

        validarCpf(cliente.getCpf());

        validarNome(cliente.getNome());

        if (clienteDao.buscarPorId(cliente.getCpf()) != null){
            throw new DadosInvalidosException("Já existe um cliente cadastrado com este CPF.");
        }
        clienteDao.salvar(cliente);
    }

    public void atualizar(Cliente clienteAtualizado){
        if (clienteAtualizado == null){
            throw new DadosInvalidosException("Os dados do cliente não podem ser nulos");
        }
        validarCpf(clienteAtualizado.getCpf());

        if (clienteDao.buscarPorId(clienteAtualizado.getCpf()) == null){
            throw new EntidadeNaoEncontradaException("O clienteAtualizado não possui cadastro no sistema.");
        }
        validarNome(clienteAtualizado.getNome());

        clienteDao.atualizar(clienteAtualizado);
    }
    private void validarCpf(String cpf){
        if (cpf == null || cpf.isEmpty()){
            throw new DadosInvalidosException("O CPF do cliente é obrigatório.");
        }
        if (cpf.length() != 11 || !cpf.matches("\\d+")){
            throw new DadosInvalidosException("O CPF deve conter exatamente 11 números!");
        }
    }
    private void validarNome(String nome){
        if (nome == null || nome.isEmpty()){
            throw new DadosInvalidosException("O nome do cliente é obrigatório.");
        }
    }
}
