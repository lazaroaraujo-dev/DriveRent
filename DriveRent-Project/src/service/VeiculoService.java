package service;

import dao.PersistenciaDao;
import exception.DadosInvalidosException;
import exception.EntidadeNaoEncontradaException;
import exception.LocacaoAtivaException;
import exception.VeiculoIndisponivelException;
import model.entities.Veiculo;
import model.enums.StatusVeiculo;

import java.util.List;

public class VeiculoService {

    private final PersistenciaDao<Veiculo> veiculoDao;

    public VeiculoService(PersistenciaDao<Veiculo> veiculoDao) {
        this.veiculoDao = veiculoDao;
    }

    public void cadastrar(Veiculo veiculo) {
        if (veiculo == null) {
            throw new DadosInvalidosException("Os dados do veículo não podem ser nulos.");
        }

        validarPlaca(veiculo.getPlaca());

        if (veiculoDao.buscarPorId(veiculo.getPlaca()) != null) {
            throw new DadosInvalidosException("Já existe um veículo cadastrado com esta placa.");
        }

        veiculoDao.salvar(veiculo);
    }

    public Veiculo buscarPorPlaca(String placa){
        validarPlaca(placa);
        Veiculo veiculo = veiculoDao.buscarPorId(placa);
        if (veiculo == null){
            throw new EntidadeNaoEncontradaException("Nenhum veículo encontrado com a placa: "+placa);
        }
        return veiculo;
    }

    public List<Veiculo> listarTodos(){
        return veiculoDao.listarTodos();
    }

    public void atualizar(Veiculo veiculoAtualizado) {
        if (veiculoAtualizado == null) {
            throw new DadosInvalidosException("Os dados do veículo não podem ser nulos.");
        }

        validarPlaca(veiculoAtualizado.getPlaca());

        Veiculo veiculoExistente = veiculoDao.buscarPorId(veiculoAtualizado.getPlaca());
        if (veiculoExistente == null) {
            throw new EntidadeNaoEncontradaException("O veículo não possui cadastro no sistema.");
        }

        // O status NÃO é alterado aqui — só via alterarStatus()
        veiculoAtualizado.setStatusVeiculo(veiculoExistente.getStatusVeiculo());

        veiculoDao.atualizar(veiculoAtualizado);
    }

    public void remover(String placa) {
        Veiculo veiculo = buscarPorPlaca(placa);

        if (veiculo.getStatusVeiculo() == StatusVeiculo.ALUGADO) {
            throw new LocacaoAtivaException("Não é possível remover um veículo que está alugado.");
        }

        veiculoDao.deletar(placa);
    }

    public void alterarStatus(String placa, StatusVeiculo novoStatus) {
        if (novoStatus == null) {
            throw new DadosInvalidosException("O novo status não pode ser nulo.");
        }

        Veiculo veiculo = buscarPorPlaca(placa);

        if (veiculo.getStatusVeiculo() == StatusVeiculo.ALUGADO && novoStatus == StatusVeiculo.EM_MANUTENCAO) {
            throw new VeiculoIndisponivelException("Não é possível enviar para manutenção um veículo que está alugado.");
        }

        veiculo.setStatusVeiculo(novoStatus);
        veiculoDao.atualizar(veiculo);
    }

    private void validarPlaca(String placa){
        if (placa == null || placa.trim().isEmpty()){
            throw new DadosInvalidosException("A placa do veículo é obrigatória.");
        }

        String placaLimpa = placa.trim().toUpperCase().replace("-","");

        boolean formatoAntigo = placaLimpa.matches("[A-Z]{3}\\d{4}");
        boolean formatoMercoSul = placaLimpa.matches("[A-Z]{3}\\d[A-Z]\\d{2}");

        if (!formatoAntigo && !formatoMercoSul){
            throw new DadosInvalidosException("A placa deve estar no formato AAA-9999 ou AAA9A99 (Mercosul).");
        }
    }


}


