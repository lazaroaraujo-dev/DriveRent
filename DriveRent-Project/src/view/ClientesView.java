package view;

import exception.DadosInvalidosException;
import exception.EntidadeNaoEncontradaException;
import model.entities.Cliente;
import service.ClienteService;

import java.util.List;
import java.util.Scanner;

public class ClientesView {
    private final Scanner scanner;
    private final ClienteService clienteService;

    public ClientesView(Scanner scanner, ClienteService clienteService){
        this.scanner = scanner;
        this.clienteService = clienteService;
    }
    public void exibirMenu(){
        int opcao = -1;
        while (opcao!=0){
            System.out.println("=== Menu Cliente ===");
            System.out.println("1. Cadastrar cliente");
            System.out.println("2. Buscar cliente por CPF");
            System.out.println("3. Listar todos os clientes.");
            System.out.println("4. Atualizar cliente");
            System.out.println("5. Remover cliente");
            System.out.println("0. Voltar");
            System.out.println("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine().trim());

            switch (opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> buscarCliente();
                case 3 -> listarClientes();
                case 4 -> atualizarClientes();
                case 5 -> removerCliente();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        }

    }
    private void cadastrarCliente(){
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("CNH: ");
            String cnh = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            Cliente cliente = new Cliente(cnh,telefone,nome,cpf);
            clienteService.cadastrar(cliente);

            System.out.println("Cliente cadastrado com sucesso!");
        } catch (DadosInvalidosException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }
    private void buscarCliente(){
        try {
            System.out.print("Digite o CPF: ");
            String cpf = scanner.nextLine();
            Cliente cliente = clienteService.buscarPorCpf(cpf);
            System.out.println(cliente);
        } catch (DadosInvalidosException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }
    private void listarClientes(){
        List<Cliente> clientes = clienteService.listarClientes();
        if (clientes.isEmpty()){
            System.out.println("Nenhum cliente cadastrado.");
        }else {
            for (Cliente cliente : clientes){
                System.out.println(cliente);
            }
        }
    }
    private void atualizarClientes(){
        try {
            System.out.print("CPF do cliente a atualizar: ");
            String cpf = scanner.nextLine();

            Cliente clienteExistente = clienteService.buscarPorCpf(cpf);

            System.out.println("Dados atuais: "+clienteExistente);
            System.out.println("Deixe em branco para manter o valor atual: ");

            System.out.print("Novo nome (" +clienteExistente.getNome()+ "): ");
            String nome = scanner.nextLine();
            if (nome.isBlank()) nome = clienteExistente.getNome();

            System.out.print("Novo telefone (" +clienteExistente.getTelefone()+ "): ");
            String telefone = scanner.nextLine();
            if (telefone.isBlank()) telefone = clienteExistente.getTelefone();

            System.out.print("Nova CNH ("+clienteExistente.getCnh()+"): ");
            String cnh = scanner.nextLine();
            if (cnh.isBlank()) cnh = clienteExistente.getCnh();

            Cliente cliente = new Cliente(cnh,telefone,nome,cpf);
            clienteService.atualizar(cliente);

            System.out.println("Cliente atualizado com sucesso! ");
        } catch (DadosInvalidosException | EntidadeNaoEncontradaException e) {
            System.out.println("Erro: "+e.getMessage());
        }

    }
    private void removerCliente(){

    }
}
