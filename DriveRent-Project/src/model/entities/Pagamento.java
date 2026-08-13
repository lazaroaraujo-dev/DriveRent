package model.entities;

import model.enums.MetodoPagamento;
import model.enums.StatusPagamento;

import java.time.LocalDate;
import java.util.Objects;

public class Pagamento {

    private String id;
    private double valor;
    private LocalDate dataPagamento;
    private MetodoPagamento metodo;
    private StatusPagamento status;
    private int parcelas;

    public Pagamento() {
    }

    public Pagamento(String id, double valor, LocalDate dataPagamento, MetodoPagamento metodo, StatusPagamento status) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.metodo = metodo;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void calcularValorFinal(double valorOriginal) {
        switch (this.metodo) {
            case PIX:
                // 4% de desconto
                this.valor = valorOriginal - (valorOriginal * 0.04);
                this.parcelas = 1;
                break;
            case DINHEIRO:
                // 5% de desconto
                this.valor = valorOriginal - (valorOriginal * 0.05);
                this.parcelas = 1;
                break;
            case CARTAO_CREDITO:
                // 2% de acréscimo
                this.valor = valorOriginal + (valorOriginal * 0.02);
                // Regra das parcelas dependendo do valor
                if (this.valor > 1000.0) {
                    this.parcelas = 10;
                } else if (this.valor > 500.0) {
                    this.parcelas = 5;
                } else {
                    this.parcelas = 1;
                }
                break;
            case CARTAO_DEBITO:
                this.valor = valorOriginal;
                this.parcelas = 1;
                break;
        }
    }
}