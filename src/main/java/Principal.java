import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import dao.UsuarioDAO;
import model.Usuario;
import jakarta.persistence.*;

public class Principal {
    // Deixamos a fábrica acessível para os DAOs se precisarem
    // A própria fábrica fica isolada e segura aqui dentro
    public static jakarta.persistence.EntityManagerFactory emf;
    public static void main(String[] args) {
        System.out.println("Iniciando o teste de conexão com o Banco de Dados PostgreSQL na AWS via ORM...");

        try {
            // 1. LER AS VARIÁVEIS DO LAUNCH.JSON DIRETAMENTE AQUI
            Map<String, String> propriedadesModificadas = new HashMap<>();
            propriedadesModificadas.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
            propriedadesModificadas.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
            propriedadesModificadas.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

            // 2. INICIALIZAR A FÁBRICA PASSANDO AS CREDENCIAIS RECUPERADAS
            emf = Persistence.createEntityManagerFactory("universidade-pu", propriedadesModificadas);
            System.out.println("Conexão e validação com a AWS realizadas com sucesso!");
            // Instancia o DAO (garanta que o bloco static antigo foi removido de lá)
            UsuarioDAO dao = new UsuarioDAO();


            System.out.println("--- Operação: Consultar Usuários ---");
            /*
              O DAO executará uma query JPQL. O Hibernate traduz essa query para SQL nativo e transforma o ResultSet do banco diretamente 
              em objetos 'Usuario' prontos para o Java.
            */
            List<Usuario> listaInicial = dao.consultarUsuarios();
            listaInicial.forEach(System.out::println);
            System.out.println();

            System.out.println("--- Operação: Inserir Novo Usuário ---");
            // Criando listas normais do Java; o Hibernate fará a conversão para o JSONB no Postgres automaticamente
            List<String> emails = List.of("profA@email.com", "profA.trabalho@email.com");
            List<String> telefones = List.of("79999998888", "7932221111");
            Usuario testeUsuario = new Usuario(111111100L, "Prof XX", LocalDate.of(1980, 3, 5), emails, telefones, "profaaar", "senha1");
            /*
             O Hibernate converte de forma transparente as listas de e-mails/telefones do Java em arrays formatados como JSONB exigidos pela coluna do PostgreSQL na AWS.
            */
            dao.inserirUsuario(testeUsuario);
            System.out.println();

            System.out.println("--- Operação: Consultar Após Inserção ---");
            dao.consultarUsuarios().forEach(System.out::println);
            System.out.println();

            System.out.println("--- Operação: Atualizar Usuário ---");
            // Alteramos o dado apenas na memória do Java primeiro
            testeUsuario.setNome("Prof. XX Modificado");
            // Alterando um dado da lista JSONB para testar a flexibilidade
            testeUsuario.setEmail(List.of("prof_novo_email@email.com")); 
            //O Hibernate compara o objeto da memória com o que está na AWS e altera na nuvem, exclusivamente as colunas de texto modificadas no Java.
            dao.atualizarUsuario(testeUsuario);
            System.out.println();

            System.out.println("--- Operação: Consultar Após Atualização ---");
            dao.consultarUsuarios().forEach(System.out::println);
            System.out.println();

            System.out.println("--- Operação: Deletar Usuário ---");
            //Uma vez localizado, o Hibernate dispara um comando 'DELETE FROM usuario WHERE cpf = ...' limpando o registro de forma segura.
            dao.deletarUsuario(111111100L);
            System.out.println();

            System.out.println("--- Operação: Consultar Final ---");
            dao.consultarUsuarios().forEach(System.out::println);
            System.out.println();

        } catch (Exception e) {
            System.err.println("Erro crítico durante a execução dos testes de persistência!");
            e.printStackTrace();
        } finally {
            // Fecha a fábrica de conexões ao encerrar o programa para liberar os recursos da AWS
            if (emf != null && emf.isOpen()) {
                emf.close();
                System.out.println("Fábrica de conexões do JPA encerrada.");
            }
        }
    }
}

