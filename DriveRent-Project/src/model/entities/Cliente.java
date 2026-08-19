package model.entities;

import java.util.Objects;

public class Cliente {

    private String cpf;
    private String nome;
    private String telefone;
    private String cnh;

    public Cliente(String cnh, String telefone, String nome, String cpf) {
        this.cnh = cnh;
        this.telefone = telefone;
        this.nome = nome;
        if (cpf != null){
            this.cpf = cpf.trim().replace(".","").replace("-", "");
        }
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }
    @Override
    public boolean equals(Object o){
        if (this == o){
            return true;
        } if (o == null || getClass() != o.getClass()){
            return false;
        }Cliente cliente = (Cliente) o;
        return Objects.equals(cpf, cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return "Nome: "+getNome()+"\n"+
                "CPF: "+getCpf()+"\n"+
                "CNH: "+getCnh()+"\n"+
                "Telefone: "+getTelefone()+"\n";
    }
}
