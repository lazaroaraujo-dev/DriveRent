package service;

import dao.PersistenciaDao;
import exception.DadosInvalidosException;
import exception.EntidadeNaoEncontradaException;
import exception.LocacaoAtivaException;
import model.entities.Cliente;
import model.entities.Locacao;
import model.enums.StatusLocacao;

import java.util.List;

public class ClienteService {

    private final PersistenciaDao<Cliente> clienteDao;
    private final PersistenciaDao<Locacao> locacaoDao;

    public ClienteService(
            PersistenciaDao<Cliente> clienteDao,
            PersistenciaDao<Locacao> locacaoDao
    ) {
        this.clienteDao = clienteDao;
        this.locacaoDao = locacaoDao;
    }

    public void cadastrar(Cliente cliente) {

        if (cliente == null) {
            throw new DadosInvalidosException(
                    "Os dados do cliente não podem ser nulos."
            );
        }

        validarCpf(cliente.getCpf());
        validarNome(cliente.getNome());
        validarTelefone(cliente.getTelefone());
        validarCnh(cliente.getCnh());

        if (clienteDao.buscarPorId(cliente.getCpf()) != null) {
            throw new DadosInvalidosException(
                    "Já existe um cliente cadastrado com este CPF."
            );
        }

        clienteDao.salvar(cliente);
    }

    public void atualizar(Cliente clienteAtualizado) {

        if (clienteAtualizado == null) {
            throw new DadosInvalidosException(
                    "Os dados do cliente não podem ser nulos."
            );
        }

        validarCpf(clienteAtualizado.getCpf());
        validarNome(clienteAtualizado.getNome());
        validarTelefone(clienteAtualizado.getTelefone());
        validarCnh(clienteAtualizado.getCnh());

        if (clienteDao.buscarPorId(clienteAtualizado.getCpf()) == null) {
            throw new EntidadeNaoEncontradaException(
                    "O cliente " + clienteAtualizado.getNome()
                            + " não possui cadastro no sistema."
            );
        }

        clienteDao.atualizar(clienteAtualizado);
    }

    public Cliente buscarPorCpf(String cpf) {

        validarCpf(cpf);

        Cliente cliente = clienteDao.buscarPorId(cpf);

        if (cliente == null) {
            throw new EntidadeNaoEncontradaException(
                    "Nenhum cliente encontrado com o CPF: " + cpf
            );
        }

        return cliente;
    }

    public List<Cliente> listarClientes() {
        return clienteDao.listarTodos();
    }

    public void removerCliente(String cpf) {

        buscarPorCpf(cpf);

        boolean possuiLocacaoAberta = locacaoDao.listarTodos()
                .stream()
                .anyMatch(l ->
                        l.getCliente().getCpf().equals(cpf)
                                && l.getStatusLocacao() == StatusLocacao.ATIVA
                );

        if (possuiLocacaoAberta) {
            throw new LocacaoAtivaException(
                    "Não é possível remover um cliente com locação em aberto."
            );
        }

        clienteDao.deletar(cpf);
    }

    // VALIDAÇÕES

    private void validarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new DadosInvalidosException(
                    "O CPF do cliente é obrigatório."
            );
        }
        if (cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            throw new DadosInvalidosException(
                    "O CPF deve conter exatamente 11 números."
            );
        }
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException(
                    "O nome do cliente é obrigatório."
            );
        }
    }

    private void validarTelefone(String telefone) {

        if (telefone == null || telefone.isBlank()) {
            throw new DadosInvalidosException(
                    "O telefone do cliente é obrigatório."
            );
        }

        String telefoneLimpo = telefone.replaceAll("\\D", "");

        if (telefoneLimpo.length() != 10 && telefoneLimpo.length() != 11) {
            throw new DadosInvalidosException(
                    "O telefone deve conter 10 ou 11 números."
            );
        }
    }

    private void validarCnh(String cnh) {

        if (cnh == null || cnh.isBlank()) {
            throw new DadosInvalidosException(
                    "A CNH do cliente é obrigatória."
            );
        }
        cnh = cnh.trim();
        if (!cnh.matches("\\d{11}")) {
            throw new DadosInvalidosException(
                    "A CNH deve conter exatamente 11 números."
            );
        }
        if (cnh.matches("(\\d)\\1{10}")) {
            throw new DadosInvalidosException(
                    "A CNH não pode conter todos os números iguais."
            );
        }
    }
}