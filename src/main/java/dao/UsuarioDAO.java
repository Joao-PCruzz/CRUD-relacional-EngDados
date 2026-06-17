package dao; //Esse package é responsável por implementar todas as funções do CRUD separadamente
import model.Usuario;
import java.util.List;
import jakarta.persistence.*;


public class UsuarioDAO extends BaseDAO{
    //CONSTRUCTOR
    public UsuarioDAO(EntityManagerFactory emf) {
        super(emf);
    }

    //METHODS
    // --- CRIAR (CREATE) ---
    public void inserirUsuario(Usuario usuario) {
        runInTransaction(em -> em.persist(usuario));
        System.out.println("Usuário inserido com sucesso.");
    }

    // --- LER (READ) ---
    public Usuario buscarPorCPF(long cpf) {
        return runQuery(em -> em.find(Usuario.class, cpf));
    }

    public List<Usuario> consultarUsuarios() {
        return runQuery(em -> em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList());
    }

    // --- ATUALIZAR (UPDATE) ---
    //Esse método é impossibilitado de alterar somente o CPF
    public void atualizarUsuario(Usuario usuarioAtualizado) {
       runInTransaction(em -> {
           Usuario usuarioBanco = em.find(Usuario.class, usuarioAtualizado.getCpf());
           if (usuarioBanco != null) {
               usuarioBanco.setNome(usuarioAtualizado.getNome());
               usuarioBanco.setData_nascimento(usuarioAtualizado.getData_nascimento());
               usuarioBanco.setEmail(usuarioAtualizado.getEmail());
               usuarioBanco.setTelefone(usuarioAtualizado.getTelefone());
               usuarioBanco.setLogin(usuarioAtualizado.getLogin());
               usuarioBanco.setSenha(usuarioAtualizado.getSenha());
               System.out.println("Usuário atualizado com sucesso!");
           } else {
               System.out.println("Erro: Usuário com CPF " + usuarioAtualizado.getCpf() + " não encontrado.");
           }
       });
    }

    // --- DELETAR (DELETE) ---
    public void deletarUsuario(long cpf) {
        runInTransaction(em -> {
            Usuario usuario = em.find(Usuario.class, cpf);
            if (usuario != null) {
                em.remove(usuario);
                System.out.println("Usuário deletado com sucesso.");
            } else {
                System.out.println("Usuário não encontrado para remoção.");
            }
        });
    }
}

