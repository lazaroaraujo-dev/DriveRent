package view;

import model.entities.Veiculo;

import java.util.Scanner;

public class MenuPrincipalView {
    private final Scanner scanner;
    private final ClientesView clientesView;
    private final VeiculoView veiculoView;
    private final LocacoesView locacoesView;

    public MenuPrincipalView(Scanner scanner, ClientesView clientesView, VeiculoView veiculoView, LocacoesView locacoesView){
        this.scanner = scanner;
        this.clientesView = clientesView;
        this.veiculoView = veiculoView;
        this.locacoesView = locacoesView;
    }
    public void exibirMenu() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("=== Menu principal ===");
            System.out.println("1. Gerenciar Cliente(s)");
            System.out.println("2. Gerenciar Veículo(s)");
            System.out.println("3. Gerenciar Locações(s)");
            System.out.println("0. Sair");
            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> clientesView.exibirMenu();
                    case 2 -> veiculoView.exibirMenu();
                    case 3 -> locacoesView.exibirMenu();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
