package view;

import exception.DadosInvalidosException;
import exception.EntidadeNaoEncontradaException;
import model.entities.CarroPasseio;
import model.entities.Moto;
import model.entities.Utilitario;
import model.entities.Veiculo;
import model.enums.CategoriaVeiculo;
import service.VeiculoService;

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
            System.out.println("5. Deletar veículo");
            System.out.println("0. Voltar");

            opcao = Integer.parseInt(scanner.nextLine().trim());

            switch (opcao){
                case 1 -> cadastrarVeiculo();
                case 2 -> buscarPorPlaca();
                case 3 -> listarVeiculos();
                case 4 -> atualizarVeiculo();
                case 5 -> deletarVeiculo();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }
    public void cadastrarVeiculo(){
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
    public void buscarPorPlaca(){}
    public void listarVeiculos(){}
    public void atualizarVeiculo(){}
    public void deletarVeiculo(){}
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
