package model.entities;

import model.enums.StatusLocacao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Locacao {

    private String id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataDevolucaoEfetiva = null;
    private double valorBase;
    private StatusLocacao statusLocacao;
    private Pagamento pagamento;

    public Locacao() {
    }

    public Locacao(String id, Cliente cliente, Veiculo veiculo, LocalDate dataInicio, LocalDate dataFim, double valorTotal, StatusLocacao statusLocacao) {
        this.id = id;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorBase = valorTotal;
        this.statusLocacao = statusLocacao;
        this.pagamento = null;
    }

    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    public void setDataDevolucaoEfetiva(LocalDate dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public StatusLocacao getStatusLocacao() {
        return statusLocacao;
    }

    public void setStatusLocacao(StatusLocacao statusLocacao) {
        this.statusLocacao = statusLocacao;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public double calcularMultaAtraso(double valorMultaPorDia){
        if (this.dataDevolucaoEfetiva != null && dataDevolucaoEfetiva.isAfter(this.dataFim)){
            long diasAtraso = ChronoUnit.DAYS.between(dataFim, dataDevolucaoEfetiva);

            return diasAtraso * valorMultaPorDia;
        }
        return 0.0;
    }
    public void registrarDevolucao(LocalDate dataDevolucao, double valorMultaPorDia){
        this.dataDevolucaoEfetiva = dataDevolucao;
        double valorMulta = calcularMultaAtraso(valorMultaPorDia);
        if (valorMulta>0) this.valorBase += valorMulta;

        this.statusLocacao = StatusLocacao.CONCLUIDA;
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locacao locacao = (Locacao) o;
        return Objects.equals(id, locacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
