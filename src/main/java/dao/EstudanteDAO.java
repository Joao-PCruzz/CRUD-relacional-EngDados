package dao;
import jakarta.persistence.*;
import model.Estudante;

import java.util.List;

public class EstudanteDAO extends BaseDAO {
    //CONSTRUCTOR
    public EstudanteDAO(EntityManagerFactory emf) {
        super(emf);
    }

    //METHODS
    //CREATE
    public void inserirEstudante(Estudante estudante) { //Método para inserir um novo estudante no banco de dados
        runInTransaction(em -> em.persist(estudante));
        System.out.println("Estudante inserido com sucesso.");
    }

    //READ
    public Estudante buscarPorMatricula(String mat) { //Método para ler um estudante específico pela matrícula
        return runQuery(em -> em.find(Estudante.class, mat));
    }

    public List<Estudante> consultarEstudantes() { //Método para ler todos os estudantes no banco
        return runQuery(em -> em.createQuery("SELECT e FROM Estudante e", Estudante.class).getResultList());
    }

    //UPDATE
    public void atualizarEstudante(Estudante estudanteAtualizado) { //Método para atualizar os dados de um estudante
        runInTransaction(em -> {
            Estudante estudanteBanco = em.find(Estudante.class, estudanteAtualizado.getMat_estudante());

            if (estudanteBanco != null) {
                estudanteBanco.setMc(estudanteAtualizado.getMc());
                estudanteBanco.setAno_ingresso(estudanteAtualizado.getAno_ingresso());
                estudanteBanco.setUsuario(estudanteAtualizado.getUsuario());
                System.out.println("Estudante com matrícula " + estudanteAtualizado.getMat_estudante() + " atualizado com sucesso.");
            } else {
                System.out.println("Erro: Estudante com matrícula " + estudanteAtualizado.getMat_estudante() + " não encontrado.");
            }
        });
    }

    //DELETE
    public void deletarEstudante(String mat) { //Método para remover um estudante do banco
        runInTransaction(em -> {
            Estudante estudante = em.find(Estudante.class, mat);
            if (estudante != null) {
                em.remove(estudante);
                System.out.println("Estudante deletado com sucesso.");
            } else {
                System.out.println("Estudante não encontrado para remoção.");
            }
        });
    }

}
