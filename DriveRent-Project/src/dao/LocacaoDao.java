package dao;

import model.entities.Locacao;

import java.util.ArrayList;
import java.util.List;

public class LocacaoDao implements DaoPersistencia<Locacao>{
    private static final String ARQUIVO_LOCACAO = "data/locacao.json";
    private List<Locacao> locacaoDaoList;

    public LocacaoDao() {
        this.locacaoDaoList = new ArrayList<>();
    }

    @Override
    public void salvar(Locacao obj) {
        locacaoDaoList.add(obj);
    }

    @Override
    public Locacao buscarPorId(String id) {
        for (Locacao locacao : locacaoDaoList) {
            if (locacao.getId().equals(id)) {
                return locacao;
            }
        }
        return null;
    }

    @Override
    public List<Locacao> listarTodos() {
        return this.locacaoDaoList;
    }

    @Override
    public void atualizar(Locacao obj) {
        for (int i = 0; i < locacaoDaoList.size(); i++) {
            if (locacaoDaoList.get(i).getId().equals(obj.getId())){
                locacaoDaoList.set(i, obj);
                break;
            }
        }
    }

    @Override
    public void deletar(String id) {
        Locacao elementDelete = buscarPorId(id);
        locacaoDaoList.remove(elementDelete);
    }
}
