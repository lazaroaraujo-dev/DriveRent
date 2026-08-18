package service;

import dao.PersistenciaDao;
import exception.DadosInvalidosException;
import exception.DataInvalidaException;
import exception.EntidadeNaoEncontradaException;
import exception.VeiculoIndisponivelException;
import model.entities.Cliente;
import model.entities.Locacao;
import model.entities.Veiculo;
import model.enums.StatusLocacao;
import model.enums.StatusVeiculo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LocacaoService {

    private final PersistenciaDao<Locacao> locacaoDao;
    private final PersistenciaDao<Cliente> clienteDao;
    private final PersistenciaDao<Veiculo> veiculoDao;
    private final VeiculoService veiculoService;

    public LocacaoService(PersistenciaDao<Locacao> locacaoDao, PersistenciaDao<Cliente> clienteDao,
                          PersistenciaDao<Veiculo> veiculoDao, VeiculoService veiculoService){
        this.locacaoDao = locacaoDao;
        this.clienteDao = clienteDao;
        this.veiculoDao = veiculoDao;
        this.veiculoService = veiculoService;
    }

    public void cadastrar(Locacao locacao){
        if (locacao == null){
            throw new DadosInvalidosException("Os dados da locação não podem ser nulos.");
        }
        validarDatas(locacao.getDataInicio(), locacao.getDataFim());
        Cliente cliente = clienteDao.buscarPorId(locacao.getCliente().getCpf());
        if (cliente == null) {
            throw new EntidadeNaoEncontradaException("Cliente não cadastrado no sistema.");
        }

        Veiculo veiculo = veiculoService.buscarPorPlaca(locacao.getVeiculo().getId());

        if (veiculo.getStatusVeiculo() != StatusVeiculo.DISPONIVEL) {
            throw new VeiculoIndisponivelException("O veículo não está disponível para locação.");
        }

        long dias = ChronoUnit.DAYS.between(locacao.getDataInicio(), locacao.getDataFim());
        double valorBase = veiculo.calcularDiaria((int) dias);
        locacao.setValorBase(valorBase);

        locacao.setStatusLocacao(StatusLocacao.ATIVA);

        locacaoDao.salvar(locacao);
        veiculoService.alterarStatus(veiculo.getId(), StatusVeiculo.ALUGADO);
    }
    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new DadosInvalidosException("As datas de início e fim são obrigatórias.");
        }
        if (dataInicio.isBefore(LocalDate.now())) {
            throw new DataInvalidaException("A data de início não pode ser no passado.");
        }
        if (!dataFim.isAfter(dataInicio)) {
            throw new DataInvalidaException("A data de fim deve ser posterior à data de início.");
        }
    }
}
