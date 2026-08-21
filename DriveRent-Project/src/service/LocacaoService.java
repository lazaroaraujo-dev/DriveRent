package service;

import dao.PersistenciaDao;
import exception.*;
import model.entities.Cliente;
import model.entities.Locacao;
import model.entities.Veiculo;
import model.enums.StatusLocacao;
import model.enums.StatusVeiculo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        if (locacao.getCliente() == null) {
            throw new DadosInvalidosException("A locação precisa de um cliente associado.");
        }
        if (locacao.getVeiculo() == null) {
            throw new DadosInvalidosException("A locação precisa de um veículo associado.");
        }

        locacao.setDataInicio(LocalDate.now());
        validarDatas(locacao.getDataInicio(), locacao.getDataFim());

        Cliente cliente = clienteDao.buscarPorId(locacao.getCliente().getCpf());
        if (cliente == null) {
            throw new EntidadeNaoEncontradaException("Cliente não cadastrado no sistema.");
        }

        Veiculo veiculo = veiculoService.buscarPorPlaca(locacao.getVeiculo().getPlaca());

        if (veiculo.getStatusVeiculo() != StatusVeiculo.DISPONIVEL) {
            throw new VeiculoIndisponivelException("O veículo não está disponível para locação.");
        }

        long dias = ChronoUnit.DAYS.between(locacao.getDataInicio(), locacao.getDataFim());
        double valorBase = veiculo.calcularDiaria((int) dias);
        locacao.setValorBase(valorBase);

        locacao.setStatusLocacao(StatusLocacao.ATIVA);

        locacaoDao.salvar(locacao);
        veiculoService.alterarStatus(veiculo.getPlaca(), StatusVeiculo.ALUGADO);
    }

    public List<Locacao> listarTodos(){
        return locacaoDao.listarTodos();
    }

    public List<Locacao> buscarPorCliente(String cpf){
        if (cpf == null || cpf.isEmpty()){
            throw new DadosInvalidosException("O CPF é obrigatório.");
        }
        return locacaoDao.listarTodos().stream()
                .filter(l -> l.getCliente().getCpf().equals(cpf))
                .toList();
    }
    public List<Locacao> buscarPorVeiculo(String placa){
        if (placa == null || placa.isEmpty()){
            throw new DadosInvalidosException("A placa é obrigatória.");
        }
        return locacaoDao.listarTodos().stream()
                .filter(l -> l.getVeiculo().getPlaca().equals(placa))
                .toList();
    }
    public Locacao buscarPorId(String id){
        if (id == null || id.isEmpty()){
            throw new DadosInvalidosException("O campo do id não pode ser nulo.");
        }
        Locacao locacao = locacaoDao.buscarPorId(id);
        if (locacao == null){
            throw new EntidadeNaoEncontradaException("Não existe nenhuma locação com o id: "+id);
        }return locacao;
    }
    public void atualizar(Locacao locacaoAtualizada){
        if (locacaoAtualizada == null){
            throw new DadosInvalidosException("Os dados da locação não podem ser nulos.");
        }
        validarId(locacaoAtualizada.getId());

        if (locacaoDao.buscarPorId(locacaoAtualizada.getId()) == null) {
            throw new EntidadeNaoEncontradaException("A locação não possui cadastro no sistema.");
        }

        locacaoDao.atualizar(locacaoAtualizada);
    }

    public void deletar(String id){
        validarId(id);
        Locacao locacao = locacaoDao.buscarPorId(id);
        if (locacao == null) {
            throw new EntidadeNaoEncontradaException("Não existe nenhuma locação com o id: " + id);
        }
        if (locacao.getStatusLocacao() == StatusLocacao.ATIVA){
            throw new LocacaoAtivaException("Não é possível deletar uma locação ativa.");
        }
        locacaoDao.deletar(id);
    }
    public void finalizarLocacao(String id, LocalDate dataDevolucao, double valorMultaPorDia){
        Locacao locacao = buscarPorId(id);

        if (locacao.getStatusLocacao() != StatusLocacao.ATIVA) {
            throw new DadosInvalidosException("Só é possível finalizar uma locação que está ativa.");
        }

        locacao.registrarDevolucao(dataDevolucao, valorMultaPorDia);
        locacaoDao.atualizar(locacao);

        veiculoService.alterarStatus(locacao.getVeiculo().getPlaca(), StatusVeiculo.DISPONIVEL);
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new DadosInvalidosException("As datas de início e fim são obrigatórias.");
        }
        if (!dataFim.isAfter(dataInicio)) {
            throw new DataInvalidaException("A data de fim deve ser posterior à data de início.");
        }
    }
    private void validarId(String id){
        if (id == null || id.isEmpty()){
            throw new DadosInvalidosException("Os id não pode ser nulo.");
        }
    }

}
