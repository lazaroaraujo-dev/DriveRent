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
            System.out.println("6. Cancelar locação");
            System.out.println("0. Voltar");
            System.out.println("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine().trim());

            switch (opcao){
                case 1 -> cadastrarLocacao();
                case 2 -> buscarLocacao();
                case 3 -> listarLocacao();
                case 4 -> atualizarLocacao();
                case 5 -> removerLocacao();
                case 6 -> cancelarLocacao();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    private void cadastrarLocacao(){
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
    private void buscarLocacao(){
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
    private void buscarPorCpf(){
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
    private void buscarPorPlaca(){
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
    private void listarLocacao(){
        List<Locacao> locacoes = locacaoService.listarTodos();
        if (locacoes.isEmpty()){
            System.out.println("Nenhuma locação cadastrada.");
        } else {
            for (Locacao locacao : locacoes){
                System.out.println(locacao);
            }
        }
    }
    private void atualizarLocacao(){
        int opcao = -1;
        while (opcao!=0){
            System.out.println("1. Atualizar data final");
            System.out.println("2. Atualizar veículo da locação");
            System.out.println("0. voltar");
            opcao = Integer.parseInt(scanner.nextLine());
            switch (opcao){
                case 1 -> atualizarDataFinal();
                case 2 -> atualizarVeiculo();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida!");
            }
        }
    }
    private void atualizarDataFinal(){
        try {
            System.out.print("Digite o CPF do cliente: ");
            String cpf = scanner.nextLine();

            List<Locacao> locacoes = locacaoService.buscarPorCliente(cpf);
            if (locacoes.isEmpty()) {
                System.out.println("Nenhuma locação encontrada para esse cliente.");
                return;
            }

            System.out.println("Locações encontradas:");
            for (int i = 0; i < locacoes.size(); i++) {
                System.out.println((i + 1) + " - " + locacoes.get(i));
            }

            System.out.print("Escolha o número da locação: ");
            int escolha = Integer.parseInt(scanner.nextLine().trim()) - 1;
            Locacao locacaoEscolhida = locacoes.get(escolha);

            System.out.print("Nova data de fim (dd/MM/yyyy): ");
            String dataTexto = scanner.nextLine();
            LocalDate novaDataFim = LocalDate.parse(dataTexto, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            locacaoService.atualizarDataFim(locacaoEscolhida.getId(), novaDataFim);

            System.out.println("Data de devolução atualizada com sucesso!");
        } catch (DadosInvalidosException | DataInvalidaException |
                 EntidadeNaoEncontradaException | java.time.format.DateTimeParseException e){
            System.out.println("Erro: "+ e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Erro: opção inválida.");
        }
    }
    private void atualizarVeiculo(){}
    private void cancelarLocacao(){}
    private void removerLocacao(){

    }
}
