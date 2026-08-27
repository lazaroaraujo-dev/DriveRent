package service;

import dao.PersistenciaDao;
import exception.DadosInvalidosException;
import exception.EntidadeNaoEncontradaException;
import exception.LocacaoAtivaException;
import exception.VeiculoIndisponivelException;
import model.entities.CarroPasseio;
import model.entities.Moto;
import model.entities.Utilitario;
import model.entities.Veiculo;
import model.enums.CategoriaVeiculo;
import model.enums.StatusVeiculo;

import java.time.LocalDate;
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

        validarAno(veiculo.getAno());
        validarPlaca(veiculo.getPlaca());
        validarValorDiaria(veiculo.getValorDiarioBase());
        validarDadosEspecificos(veiculo);

        if (veiculoDao.buscarPorId(veiculo.getPlaca()) != null) {
            throw new DadosInvalidosException("Já existe um veículo cadastrado com esta placa.");
        }
        veiculo.setStatusVeiculo(StatusVeiculo.DISPONIVEL);
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
        validarAno(veiculoAtualizado.getAno());
        validarPlaca(veiculoAtualizado.getPlaca());
        validarValorDiaria(veiculoAtualizado.getValorDiarioBase());
        validarDadosEspecificos(veiculoAtualizado);

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
    private void validarAno(int ano){
        int anoAtual = LocalDate.now().getYear();

        if (ano < 1950 || ano > anoAtual) {
            throw new DadosInvalidosException("O ano do veículo deve estar entre 1950 e " + anoAtual + ".");
        }
    }
    private void validarValorDiaria(double valor) {
        if (valor <= 0) {
            throw new DadosInvalidosException("O valor diário base deve ser maior que zero.");
        }
    }
    private void validarCilindradas(int cilindradas) {
        if (cilindradas < 50 || cilindradas > 2500) {
            throw new DadosInvalidosException("Cilindradas da moto devem estar entre 50 e 2500 cc.");
        }
    }
    private void validarDadosEspecificos(Veiculo veiculo) {
        if (veiculo instanceof Moto moto) {
            validarCilindradas(moto.getCilindradas());
        }
        else if (veiculo instanceof CarroPasseio carro) {
            if (carro.getNumeroPortas() < 2 || carro.getNumeroPortas() > 5) {
                throw new DadosInvalidosException("Número de portas do carro deve ser entre 2 e 5.");
            }
        } else if (veiculo instanceof Utilitario utilitario) {
            if (utilitario.getCapacidadeCargaTon() <= 0) {
                throw new DadosInvalidosException("Capacidade de carga do utilitário deve ser maior que 0.");
            }
        }
    }
}


