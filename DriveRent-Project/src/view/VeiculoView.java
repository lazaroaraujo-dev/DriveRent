package view;

import exception.DadosInvalidosException;
import exception.EntidadeNaoEncontradaException;
import exception.VeiculoIndisponivelException;
import model.entities.CarroPasseio;
import model.entities.Moto;
import model.entities.Utilitario;
import model.entities.Veiculo;
import model.enums.CategoriaVeiculo;
import model.enums.StatusVeiculo;
import service.VeiculoService;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class VeiculoView {
    private final Scanner scanner;
    private final VeiculoService veiculoService;

    public VeiculoView(Scanner scanner, VeiculoService veiculoService){
        this.scanner = scanner;
        this.veiculoService = veiculoService;
    }
    public void exibirMenu(){
        int opcao = -1;
        while (opcao!=0){
            System.out.println("=== Menu veículo ===");
            System.out.println("1. Cadastrar veículo");
            System.out.println("2. Buscar por placa");
            System.out.println("3. Listar todos os veículos");
            System.out.println("4. Atualizar veículo");
            System.out.println("5. Alterar status do veículo");
            System.out.println("6. Deletar veículo");
            System.out.println("0. Voltar");

            opcao = Integer.parseInt(scanner.nextLine().trim());

            switch (opcao){
                case 1 -> cadastrarVeiculo();
                case 2 -> buscarPorPlaca();
                case 3 -> listarVeiculos();
                case 4 -> atualizarVeiculo();
                case 5 -> alterarStatus();
                case 6 -> deletarVeiculo();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    private void cadastrarVeiculo(){
        try {
            System.out.println("Digite a placa do veículo: (AAA-9999 ou AAA9A99(Mercosul)):");
            String placa = scanner.nextLine().trim().toUpperCase();

            System.out.println("Tipo do veículo: (MOTO, CARRO_PASSEIO, UTILITARIO: ");
            CategoriaVeiculo categoriaVeiculo = CategoriaVeiculo.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.println("Digite o ano do veículo: ");
            int ano = Integer.parseInt(scanner.nextLine());

            System.out.println("Digite a marca do veículo: ");
            String marca = scanner.nextLine();

            System.out.println("Valor diário base do veículo: R$ ");
            double value = Double.parseDouble(scanner.nextLine().trim());

            Veiculo veiculo = switch (categoriaVeiculo) {
                case MOTO -> {
                    System.out.println("Cilindradas: ");
                    int cilindradas = Integer.parseInt(scanner.nextLine());
                    yield new Moto(placa, marca, ano, value, null, cilindradas);
                }
                case CARRO_PASSEIO -> {
                    System.out.println("Número de portas: ");
                    int numPortas = Integer.parseInt(scanner.nextLine().trim());

                    boolean possuiArCondicionado = lerSimNao(scanner, "Possui ar-condicionado? (SIM/NÃO)");

                    yield new CarroPasseio(placa, marca, ano, value, null, possuiArCondicionado, numPortas);
                }
                case UTILITARIO -> {
                    System.out.println("Capacidade de carga em toneladas: ");
                    double capacidade = Double.parseDouble(scanner.nextLine());
                    yield new Utilitario(placa, marca, ano, value, null, capacidade);
                }
            };
            veiculoService.cadastrar(veiculo);
            System.out.println("Veículo cadastrado com sucesso!");

        } catch (DadosInvalidosException | EntidadeNaoEncontradaException e){
            System.out.println("Erro: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: categoria de veículo inválida. Use MOTO, CARRO_PASSEIO ou UTILITARIO.");
        }
    }
    private void buscarPorPlaca(){
        try {
            System.out.println("Digite a placa do veículo: (AAA-9999 ou AAA9A99)");
            String placa = scanner.nextLine();

            Veiculo veiculo = veiculoService.buscarPorPlaca(placa);
            System.out.println("Veículo encontrado!");
            System.out.println(veiculo);
        }catch (EntidadeNaoEncontradaException | DadosInvalidosException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }
    private void listarVeiculos(){
        List<Veiculo> veiculos = veiculoService.listarTodos();

        if (veiculos.isEmpty()){
            System.out.println("Nenhum veículo cadastrado no sistema!");
            return;
        }
        System.out.println("=== Lista de Veículos ===");
        for (Veiculo veiculo : veiculos){
            System.out.println(veiculo);
        }
    }
    private void atualizarVeiculo(){
        try {
            System.out.println("Digite a placa do veículo: (AAA-9999 ou AAA9A99)");
            String placa = scanner.nextLine();

            Veiculo veiculoExistente = veiculoService.buscarPorPlaca(placa);
            System.out.println("Dados atuais: " + veiculoExistente);
            System.out.println("Deixe em branco para manter o valor atual.");

            System.out.print("Nova marca (" + veiculoExistente.getMarca() + "): ");
            String marca = scanner.nextLine();
            if (marca.isBlank()) marca = veiculoExistente.getMarca();

            System.out.print("Novo ano (" + veiculoExistente.getAno() + "): ");
            String anoTexto = scanner.nextLine();
            int ano = anoTexto.isBlank() ? veiculoExistente.getAno() : Integer.parseInt(anoTexto.trim());

            System.out.print("Novo valor diário base (" + veiculoExistente.getValorDiarioBase() + "): ");
            String valorTexto = scanner.nextLine();
            double valorDiarioBase = valorTexto.isBlank() ? veiculoExistente.getValorDiarioBase() : Double.parseDouble(valorTexto.trim());

            Veiculo veiculoAtualizado = switch (veiculoExistente) {
                case Moto motoExistente -> {
                    System.out.print("Nova cilindrada (" + motoExistente.getCilindradas() + "): ");
                    String cilindradaTexto = scanner.nextLine();
                    int cilindrada = cilindradaTexto.isBlank() ? motoExistente.getCilindradas() : Integer.parseInt(cilindradaTexto.trim());
                    yield new Moto(placa, marca, ano, valorDiarioBase, null, cilindrada);
                }
                case CarroPasseio carroExistente -> {
                    System.out.print("Novo número de portas (" + carroExistente.getNumeroPortas() + "): ");
                    String portasTexto = scanner.nextLine();
                    int portas = portasTexto.isBlank() ? carroExistente.getNumeroPortas() : Integer.parseInt(portasTexto.trim());
                    boolean arCondicionado = lerSimNao(scanner, "Possui ar-condicionado? (SIM/NÃO)");
                    yield new CarroPasseio(placa, marca, ano, valorDiarioBase, null, arCondicionado, portas);
                }
                case Utilitario utilitarioExistente -> {
                    System.out.print("Nova capacidade de carga (" + utilitarioExistente.getCapacidadeCargaTon() + "): ");
                    String capacidadeTexto = scanner.nextLine();
                    double capacidade = capacidadeTexto.isBlank() ? utilitarioExistente.getCapacidadeCargaTon() : Double.parseDouble(capacidadeTexto.trim());
                    yield new Utilitario(placa, marca, ano, valorDiarioBase, null, capacidade);
                }
                default -> throw new DadosInvalidosException("Tipo de veículo não reconhecido.");
            };

            veiculoService.atualizar(veiculoAtualizado);
            System.out.println("Veículo atualizado com sucesso!");

        }catch (EntidadeNaoEncontradaException | DadosInvalidosException | NumberFormatException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }
    private void deletarVeiculo(){
        try {
            System.out.println("Digite a plca do veículo (Formato: AAA-9999 ou AAA9A99): ");
            String placa = scanner.nextLine();

            Veiculo veiculo = veiculoService.buscarPorPlaca(placa);
            System.out.println("Dados do veículo: "+"\n"+veiculo);

            boolean confirmacao = lerSimNao(scanner, "Deseja realmente excluir? (SIM/NÃO)");

            veiculoService.remover(placa);
            System.out.println("Veículo removido com sucesso!");

        } catch (DadosInvalidosException | EntidadeNaoEncontradaException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }
    private void alterarStatus() {
        try {
            System.out.println("Digite a placa do veículo: ");
            String placa = scanner.nextLine();

            System.out.println("Escolha o novo status:");
            System.out.println("1 - DISPONIVEL");
            System.out.println("2 - EM_MANUTENCAO");
            System.out.print("Opção: ");
            String opcao = scanner.nextLine().trim();

            StatusVeiculo novoStatus;
            switch (opcao) {
                case "1" -> novoStatus = StatusVeiculo.DISPONIVEL;
                case "2" -> novoStatus = StatusVeiculo.EM_MANUTENCAO;
                default -> throw new DadosInvalidosException("Opção de status inválida.");
            }

            veiculoService.alterarStatus(placa, novoStatus);
            System.out.println("Status do veículo alterado com sucesso!");

        } catch (DadosInvalidosException | VeiculoIndisponivelException | EntidadeNaoEncontradaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    private boolean lerSimNao(Scanner scanner, String mensagem) {
        while (true) {
            System.out.println(mensagem);
            String entrada = scanner.nextLine().trim();

            if (entrada.equalsIgnoreCase("SIM") || entrada.equalsIgnoreCase("S")) {
                return true;
            }
            if (entrada.equalsIgnoreCase("NAO") || entrada.equalsIgnoreCase("NÃO") || entrada.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Entrada inválida! Por favor, digite apenas SIM ou NÃO.");
        }
    }
}
