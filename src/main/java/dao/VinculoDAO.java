package dao;
import jakarta.persistence.*;
import model.Vinculo;

import java.util.List;

public class VinculoDAO extends BaseDAO{
    //CONSTRUCTOR
    public VinculoDAO(EntityManagerFactory emf) {
        super(emf);
    }

    //METHODS
    //CREATE
    public void inserirVinculo(Vinculo vinculo) {
        runInTransaction(em -> em.persist(vinculo));
        System.out.println("Vínculo acadêmico inserido com sucesso.");
    }

    //READ
    public Vinculo buscarPorId(Integer idVinculo) {
        return runQuery(em -> em.find(Vinculo.class, idVinculo));
    }

    public List<Vinculo> consultarVinculos() {
        return runQuery(em -> em.createQuery("SELECT v FROM Vinculo v", Vinculo.class)).getResultList();
    }

    //UPDATE
    public void atualizarVinculo(Vinculo vinculoAtualizado) {
        runInTransaction(em -> {
            Vinculo vinculoBanco = em.find(Vinculo.class, vinculoAtualizado.getIdVinculo());
            if (vinculoBanco != null) {
                vinculoBanco.setData_entrada(vinculoAtualizado.getData_entrada());
                vinculoBanco.setStatus(vinculoAtualizado.getStatus());
                vinculoBanco.setData_saida(vinculoAtualizado.getData_saida());
                vinculoBanco.setEstudante(vinculoAtualizado.getEstudante());
                vinculoBanco.setCurso(vinculoAtualizado.getCurso());
                System.out.println("Vínculo com ID " + vinculoAtualizado.getIdVinculo() + " atualizado com sucesso.");
            } else {
                System.out.println("Erro: Vínculo com ID " + vinculoAtualizado.getIdVinculo() + " não encontrado.");
            }
        });
    }

    //DELETE
    public void deletarVinculo(Integer idVinculo) {
        runInTransaction(em -> {
            Vinculo vinculo = em.find(Vinculo.class, idVinculo);
            if (vinculo != null) {
                em.remove(vinculo);
                System.out.println("Vínculo deletado com sucesso!");
            } else {
                System.out.println("Vínculo não encontrado para remoção.");
            }
        });
    }
}
