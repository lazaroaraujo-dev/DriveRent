import dao.ClienteDao;
import dao.LocacaoDao;
import dao.VeiculoDao;

import service.ClienteService;
import service.LocacaoService;
import service.VeiculoService;

import view.ClientesView;
import view.LocacoesView;
import view.MenuPrincipalView;
import view.VeiculoView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // SCANNER

        Scanner scanner = new Scanner(System.in);

        // DAOs

        ClienteDao clienteDao = new ClienteDao();
        VeiculoDao veiculoDao = new VeiculoDao();
        LocacaoDao locacaoDao = new LocacaoDao();


        // SERVICES

        ClienteService clienteService =
                new ClienteService(
                        clienteDao,
                        locacaoDao
                );

        VeiculoService veiculoService =
                new VeiculoService(
                        veiculoDao
                );

        LocacaoService locacaoService =
                new LocacaoService(
                        locacaoDao,
                        clienteDao,
                        veiculoDao,
                        veiculoService
                );

        // VIEWS

        ClientesView clientesView =
                new ClientesView(
                        scanner,
                        clienteService
                );

        VeiculoView veiculoView =
                new VeiculoView(
                        scanner,
                        veiculoService
                );

        LocacoesView locacoesView =
                new LocacoesView(
                        scanner,
                        locacaoService,
                        clienteService,
                        veiculoService
                );

        // MENU PRINCIPAL

        MenuPrincipalView menuPrincipalView =
                new MenuPrincipalView(
                        scanner,
                        clientesView,
                        veiculoView,
                        locacoesView
                );


        // INICIAR SISTEMA
        menuPrincipalView.exibirMenu();

        // FECHAR SCANNER

        scanner.close();
    }
}