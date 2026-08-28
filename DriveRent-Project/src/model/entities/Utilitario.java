package model.entities;

import model.enums.StatusVeiculo;

public class Utilitario extends Veiculo{

    private double capacidadeCargaTon;

    public Utilitario(String placa, String marca, int ano, double valorDiarioBase, StatusVeiculo statusVeiculo, double capacidadeCargaTon) {
        super(placa, marca, ano, valorDiarioBase, statusVeiculo);
        this.capacidadeCargaTon = capacidadeCargaTon;
    }

    public double getCapacidadeCargaTon() {
        return capacidadeCargaTon;
    }

    public void setCapacidadeCargaTon(double capacidadeCargaTon) {
        this.capacidadeCargaTon = capacidadeCargaTon;
    }

    @Override
    public double calcularDiaria(int dias) {
        double diariaComTaxa = getValorDiarioBase() + (capacidadeCargaTon * 15.0);
        return diariaComTaxa * dias;
    }

    @Override
    public String toString() {
        return super.toString()+"\n"+"Capacidade (Toneladas): "+capacidadeCargaTon;
    }
}
