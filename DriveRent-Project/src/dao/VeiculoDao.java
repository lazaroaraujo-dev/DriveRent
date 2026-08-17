package dao;

import model.entities.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class VeiculoDao implements PersistenciaDao<Veiculo> {

    private static final String ARQUIVO_VEICULO = "data/veiculos.json";
    private final List<Veiculo> veiculoList;

    public VeiculoDao(){
        this.veiculoList = new ArrayList<>();
    }

    @Override
    public void salvar(Veiculo obj) {
        veiculoList.add(obj);
    }

    @Override
    public Veiculo buscarPorId(String id) {
        for (Veiculo veiculo : veiculoList){
            if (veiculo.getId().equals(id)){
                return veiculo;
            }
        }return null;
    }

    @Override
    public List<Veiculo> listarTodos() {
        return this.veiculoList;
    }

    @Override
    public void atualizar(Veiculo obj) {
        for (int i = 0; i < veiculoList.size(); i++) {
            if (veiculoList.get(i).getId().equals(obj.getId())){
                veiculoList.set(i, obj);
                break;
            }
        }
    }

    @Override
    public void deletar(String id) {
        veiculoList.remove(buscarPorId(id));
    }
}
