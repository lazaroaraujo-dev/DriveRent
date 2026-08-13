package model.entities;

import model.enums.StatusVeiculo;

public class CarroPasseio extends Veiculo{

    private boolean possuiArcondicionado;
    private int numeroPortas;

    public CarroPasseio(String id, String marca, int ano, double valorDiarioBase, StatusVeiculo statusVeiculo, boolean possuiArcondicionado, int numeroPortas) {
        super(id, marca, ano, valorDiarioBase, statusVeiculo);
        this.possuiArcondicionado = possuiArcondicionado;
        this.numeroPortas = numeroPortas;
    }

    public boolean isPossuiArcondicionado() {
        return possuiArcondicionado;
    }

    public void setPossuiArcondicionado(boolean possuiArcondicionado) {
        this.possuiArcondicionado = possuiArcondicionado;
    }

    public int getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(int numeroPortas) {
        this.numeroPortas = numeroPortas;
    }

    @Override
    public double calcularDiaria(int dias){
        if (isPossuiArcondicionado()){
            return dias * (getValorDiarioBase() + 20.0);
        } return dias * getValorDiarioBase();
    }
}
