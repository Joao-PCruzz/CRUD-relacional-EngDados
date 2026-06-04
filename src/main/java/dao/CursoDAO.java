package dao;
import jakarta.persistence.*;

public class CursoDAO {
    //Criação da fábrica de conexões diretamente na classe DAO
    private final EntityManagerFactory emf;

    //No construtor para obter a conexão criada no main e trabalhar a partir dela
    public CursoDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

    //Métodos do CRUD aqui
}
