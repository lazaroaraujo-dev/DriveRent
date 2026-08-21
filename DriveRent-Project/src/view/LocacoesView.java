package view;

import exception.DadosInvalidosException;
import exception.DataInvalidaException;
import exception.EntidadeNaoEncontradaException;
import exception.VeiculoIndisponivelException;
import model.entities.Cliente;
import model.entities.Locacao;
import model.entities.Veiculo;
import service.ClienteService;
import service.LocacaoService;
import service.VeiculoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class LocacoesView {
    private final Scanner scanner;
    private final LocacaoService locacaoService;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;

    public LocacoesView(Scanner scanner, LocacaoService locacaoService, ClienteService clienteService, VeiculoService veiculoService){
        this.scanner = scanner;
        this.locacaoService = locacaoService;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
    }

    public void exibirMenu(){
        int opcao = -1;
        while (opcao!=0){
            System.out.println("=== Menu Locação ===");
            System.out.println("1. Cadastrar locação");
            System.out.println("2. Buscar locação");
            System.out.println("3. Listar todos as locações");
            System.out.println("4. Atualizar locação");
            System.out.println("5. Remover locação");
            System.out.println("0. Voltar");
            System.out.println("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine().trim());

            switch (opcao){
                case 1 -> cadastrarLocacao();
                case 2 -> buscarLocacao();
                case 3 -> listarLocacao();
                case 4 -> atualizarLocacao();
                case 5 -> removerLocacao();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    public void cadastrarLocacao(){
        try {
            System.out.println("Digite o CPF do cliente: ");
            String cpf = scanner.nextLine();

            System.out.println("Digite a placa do veículo: ");
            String placa = scanner.nextLine();

            System.out.println("Data de fim: (dd/MM/yyyy)");
            String dataFimTexto = scanner.nextLine();
            LocalDate dataFim = LocalDate.parse(dataFimTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            Cliente cliente = clienteService.buscarPorCpf(cpf);
            Veiculo veiculo = veiculoService.buscarPorPlaca(placa);

            Locacao locacao = new Locacao(UUID.randomUUID().toString(), cliente, veiculo, null, dataFim, 0.0, null);

            locacaoService.cadastrar(locacao);
            System.out.println("Locação cadastrada com sucesso!");
        } catch (DadosInvalidosException | DataInvalidaException |
                 EntidadeNaoEncontradaException | VeiculoIndisponivelException e){
            System.out.println("Erro: "+ e.getMessage());
        }
    }
    public void buscarLocacao(){
        int opcao = -1;
        while (opcao!=0){
            System.out.println("1. Cliente");
            System.out.println("2. Placa");
            System.out.println("Digite o número relativo ao tipo de busca: ");
            opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao){
                case 1 -> buscarPorCpf();
                case 2 -> buscarPorPlaca();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    public void buscarPorCpf(){
        try {
            System.out.println("Digite o CPF do cliente: ");
            String cpf = scanner.nextLine();

            clienteService.buscarPorCpf(cpf.trim());
            List<Locacao> locacoesDoCliente = locacaoService.buscarPorCliente(cpf.trim());
            for (Locacao locacao : locacoesDoCliente){
                System.out.println(locacao);
            }
        } catch (DadosInvalidosException | EntidadeNaoEncontradaException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }
    public void buscarPorPlaca(){
        try {
            System.out.println("Digite a placa do veículo: ");
            String placa = scanner.nextLine();

            veiculoService.buscarPorPlaca(placa.trim());
            List<Locacao> locacaosDoVeiculo = locacaoService.buscarPorVeiculo(placa.trim());
            for (Locacao locacao : locacaosDoVeiculo){
                System.out.println(locacao);
            }
        } catch (DadosInvalidosException | EntidadeNaoEncontradaException e){
            System.out.println("Erro: "+ e.getMessage());
        }

    }
    public void listarLocacao(){

    }
    public void atualizarLocacao(){

    }
    public void removerLocacao(){

    }
}
