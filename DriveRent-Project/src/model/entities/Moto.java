package model.entities;

import model.enums.StatusVeiculo;

public class Moto extends Veiculo{

    private int cilindradas;

    public Moto(String id, String marca, int ano, double valorDiarioBase, StatusVeiculo statusVeiculo, int cilindradas) {
        super(id, marca, ano, valorDiarioBase, statusVeiculo);
        this.cilindradas = cilindradas;
    }

    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    @Override
    public double calcularDiaria(int dias){
        if (getCilindradas()>250) {
            return (dias * getValorDiarioBase()) - (dias*getValorDiarioBase())*0.05;
        }
        return (dias * getValorDiarioBase());
    }
}
