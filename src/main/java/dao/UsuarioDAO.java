package dao; //Esse package é responsável por implementar todas as funções do CRUD separadamente
import model.Usuario;
import java.util.List;
import jakarta.persistence.*;


public class UsuarioDAO {
    //Fábrica de conexões pelo Hibernate
    private final EntityManagerFactory emf;

    // O construtor obriga quem criar o DAO a passar a fábrica de conexões
    public UsuarioDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

    // --- CRIAR (CREATE) ---
    public void inserirUsuario(Usuario usuario) {
        // Abre um "Gerenciador de Entidades" (como se fosse o canal de conversa com o banco)
        EntityManager em = getEntityManager();
        try {
            // Em operações que alteram dados (Insert, Update, Delete), precisamos abrir uma Transação
            em.getTransaction().begin();
            /*
              O método .persist() pega o objeto Java completo e o transforma em um comando SQL.
              O Hibernate lê as anotações da classe Usuario, transforma a List de e-mails/telefones 
              em um formato JSONB e monta o "INSERT INTO..." para enviar para a AWS automaticamente.
             */
            em.persist(usuario); 
            // Confirma a operação. Só depois do commit os dados são salvos de verdade na AWS
            em.getTransaction().commit();
            System.out.println("Usuario inserido com sucesso via ORM!");
        } catch (Exception e) {
            // Se der qualquer erro (ex: CPF duplicado), o rollback cancela tudo para não quebrar o banco
            em.getTransaction().rollback();
            System.err.println("Erro ao inserir usuario.");
            e.printStackTrace();
        } finally {
            // Fecha o canal de conversa para liberar a memória e a conexão com a nuvem
            em.close();
        }
    }

    // --- LER (READ) ---
    public List<Usuario> consultarUsuarios() {
        EntityManager em = getEntityManager();
        try {
            // Usamos JPQL (Orientado a Objetos) e não SQL nativo. "Usuario" refere-se à classe Java.
            /*
              O "SELECT u FROM Usuario u" aponta para a CLASSE Java "Usuario" (com U maiúsculo), e não para a tabela física. 
              O Hibernate traduz isso para o SQL do Postgres sozinho, o .getResultList() já converte todas as linhas retornadas em uma Lista de Objetos pronta.
            */
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            // Consultas simples não alteram dados, então não precisam de transaction.begin() ou commit().
            em.close();
        }
    }

    // --- ATUALIZAR (UPDATE) ---
    //Esse método é impossibilitado de alterar somente o CPF
    public void atualizarUsuario(Usuario usuarioAtualizado) {
       EntityManager em = getEntityManager();
    try {
        em.getTransaction().begin();
         //Tentamos encontrar o usuário original no banco da AWS usando o CPF fornecido.
        Usuario usuarioBanco = em.find(Usuario.class, usuarioAtualizado.getCpf());
        //Se o retorno for diferente de nulo, o usuário realmente existe na AWS.
        if (usuarioBanco != null) {
            // Copiamos os novos dados para o objeto que o Hibernate já está rastreando
            usuarioBanco.setNome(usuarioAtualizado.getNome());
            usuarioBanco.setData_nascimento(usuarioAtualizado.getData_nascimento());
            usuarioBanco.setEmail(usuarioAtualizado.getEmail());
            usuarioBanco.setTelefone(usuarioAtualizado.getTelefone());
            usuarioBanco.setLogin(usuarioAtualizado.getLogin());
            usuarioBanco.setSenha(usuarioAtualizado.getSenha());

            //Ao fazermos o ".commit()" abaixo, o Hibernate detecta as mudanças e dispara um comando SQL UPDATE automaticamente para a AWS.
            System.out.println("Usuário atualizado com sucesso!");
        } else {
            // Se o usuário não existir no banco, nós avisamos e não fazemos nada
            System.out.println("Aviso: Usuário com o CPF " + usuarioAtualizado.getCpf() + " não foi encontrado. Atualização cancelada.");
        }
        
        // Finaliza a transação (salvando as alterações se o objeto existia)
        em.getTransaction().commit();
        
    } catch (Exception e) {
        em.getTransaction().rollback();
        System.err.println("Erro ao tentar atualizar o usuário.");
        e.printStackTrace();
    } finally {
        em.close();
    }
    }

    // --- DELETAR (DELETE) ---
    public void deletarUsuario(Long cpf) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            //Primeiro usamos o em.find() para buscar o usuário na AWS pelo CPF e carregá-lo na memória.
            Usuario usuario = em.find(Usuario.class, cpf);
            // Se o usuário foi encontrado no banco de dados, prosseguimos
            if (usuario != null) {
                //O método .remove() avisa ao Hibernate para deletar o registro correspondente a esse objeto específico lá no banco da AWS
                em.remove(usuario);
                System.out.println("Usuário deletado com sucesso!");
            } else {
                System.out.println("Usuário não encontrado.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}

