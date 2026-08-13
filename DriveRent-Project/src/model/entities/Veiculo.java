package model.entities;

import model.enums.StatusVeiculo;

public abstract class Veiculo {

    private String id;
    private String marca;
    private int ano;
    private double valorDiarioBase;
    private StatusVeiculo statusVeiculo;

    public Veiculo() {
    }

    public Veiculo(String id, String marca, int ano, double valorDiarioBase, StatusVeiculo statusVeiculo) {
        this.id = id;
        this.marca = marca;
        this.ano = ano;
        this.valorDiarioBase = valorDiarioBase;
        this.statusVeiculo = statusVeiculo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getValorDiarioBase() {
        return valorDiarioBase;
    }

    public void setValorDiarioBase(double valorDiarioBase) {
        this.valorDiarioBase = valorDiarioBase;
    }

    public StatusVeiculo getStatusVeiculo() {
        return statusVeiculo;
    }

    public void setStatusVeiculo(StatusVeiculo statusVeiculo) {
        this.statusVeiculo = statusVeiculo;
    }
    public abstract double calcularDiaria(int dias);
}
