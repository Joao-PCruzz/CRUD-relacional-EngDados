package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public abstract class BaseDAO { //A classe abstrata BaseDAO reúne interfaces e métodos para realização de transações e leitura
    //ATTRIBUTES
    protected final EntityManagerFactory emf;

    //CONSTRUCTOR
    public BaseDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //INTERFACES
    @FunctionalInterface
    protected interface EntityManagerCommand { //Comando a ser executado na transação
        void execute(EntityManager em);
    }

    @FunctionalInterface
    protected interface EntityManagerQuery<T> { //Versão que apenas executa uma operação (usada para leitura)
        T execute(EntityManager em);
    }

    //METHODS
    protected EntityManager getEntityManager() { //Método para criar um EntityManager
        return this.emf.createEntityManager();
    }

    protected void runInTransaction(EntityManagerCommand command) { //Método para transação genérica
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            command.execute(em);
            em.getTransaction().commit();
            System.out.println("Operação concluída com sucesso.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro na operação de banco de dados.");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    protected <T> T runQuery(EntityManagerQuery<T> query) { //Método para executar um comando (leitura)
        EntityManager em = getEntityManager();
        try {
            return query.execute(em);
        } finally {
            em.close();
        }
    }
}
