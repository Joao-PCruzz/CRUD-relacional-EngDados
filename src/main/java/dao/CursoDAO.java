package dao;
import jakarta.persistence.*;
import model.Curso;

import java.util.List;

public class CursoDAO extends BaseDAO {
    //CONSTRUCTOR
    public CursoDAO(EntityManagerFactory emf) {
        super(emf);
    }

    //METHODS
    //CREATE
    public void inserirCurso(Curso curso) {
        runInTransaction(em -> em.persist(curso));
        System.out.println("Curso inserido com sucesso.");
    }

    //READ
    public Curso buscarPorId(Integer idCurso) {
        return runQuery(em -> em.find(Curso.class, idCurso));
    }

    public List<Curso> consultarCursos() {
        return runQuery(em -> em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList());
    }

    //UPDATE
    public void atualizarCurso(Curso cursoAtualizado) {
        runInTransaction(em -> {
            Curso cursoBanco = em.find(Curso.class, cursoAtualizado.getIdCurso());
            if (cursoBanco != null) {
                cursoBanco.setNome(cursoAtualizado.getNome());
                cursoBanco.setCampus(cursoAtualizado.getCampus());
                cursoBanco.setGrau(cursoAtualizado.getGrau());
                cursoBanco.setTurno(cursoAtualizado.getTurno());
                cursoBanco.setNivel(cursoAtualizado.getNivel());
                System.out.println("Curso ID " + cursoAtualizado.getIdCurso() + " atualizado com sucesso!");
            } else {
                System.out.println("Erro: Curso com ID " + cursoAtualizado.getIdCurso() + " não encontrado.");
            }
        });
    }

    //DELETE
    public void deletarCurso(Integer idCurso) {
        runInTransaction(em -> {
            Curso curso = em.find(Curso.class, idCurso);
            if (curso != null) {
                em.remove(curso);
                System.out.println("Curso deletado com sucesso");
            } else {
                System.out.println("Curso não encontrado para remoção.");
            }
        });
    }
}
