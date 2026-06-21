package main;

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
            // Lê as variáveis o lauch.json diretamente aqui, mantendo o código seguro.
            // O HashMap funciona utilizando uma "chave e valor"
            Map<String, String> propriedadesModificadas = new HashMap<>();
            // A chave é o nome de propriedade do JPA, já o valor é a variável de conexão no launch.json
            // O "System.getenv" busca o valor configurado nas variáveis de ambiente no lauch.json
            // O Hibernate lê o 'persistence.xml', mas antes de conectar, ele olha dentro deste HashMap, que possui os dados necesários
            propriedadesModificadas.put("jakarta.persistence.jdbc.url", System.getenv("DB_URL"));
            propriedadesModificadas.put("jakarta.persistence.jdbc.user", System.getenv("DB_USER"));
            propriedadesModificadas.put("jakarta.persistence.jdbc.password", System.getenv("DB_PASSWORD"));

            // Inicializar a fábrica passando as credenciais recuperadas
            // Esse método aceita um Map como segundo parâmetro para ajudar na segurança da Database
            emf = Persistence.createEntityManagerFactory("universidade-pu", propriedadesModificadas);
            System.out.println("Conexão e validação com a AWS realizadas com sucesso!");
            // Instancia o DAO 
            UsuarioDAO dao = new UsuarioDAO(emf);


            System.out.println("--- Operação: Consultar Usuários ---");
            /*
              O DAO executará uma query JPQL. O Hibernate traduz essa query para SQL nativo e transforma o ResultSet do banco diretamente 
              em objetos 'Usuario' prontos para o Java.
            */
            List<Usuario> listaInicial = dao.consultarUsuarios();
            imprimirTabelaUsuarios(listaInicial);
            System.out.println();

            System.out.println("--- Operação: Inserir Novo Usuário ---");
            // Criando listas normais do Java, o Hibernate fará a conversão para o JSONB no Postgres automaticamente
            List<String> emailsNovos = List.of("lucas.silva@souufs.br", "lucas.pessoal@gmail.com");
            List<String> telefonesNovos = List.of("79988887777", "7932112233");
            Usuario testeUsuario = new Usuario(99988877711L, "Professor Teste", LocalDate.of(1985, 5, 20), emailsNovos, telefonesNovos, "proftst", "senha123");
            //O Hibernate converte de forma transparente as listas de e-mails/telefones do Java em arrays formatados como JSONB exigidos pela coluna do PostgreSQL na AWS.
            dao.inserirUsuario(testeUsuario);
            System.out.println();

            System.out.println("--- Operação: Consultar Após Inserção ---");
            dao.consultarUsuarios().forEach(System.out::println);
            System.out.println();

            System.out.println("--- Operação: Atualizar Usuário ---");
            // Alteramos o dado apenas na memória do Java primeiro
            testeUsuario.setNome("Prof. XX Modificado");
            testeUsuario.setEmail(List.of("prof_novo_email@email.com"));
            // O Hibernate compara o objeto da memória com o que está na AWS e altera na nuvem
            dao.atualizarUsuario(testeUsuario);
            System.out.println();

            System.out.println("--- Operação: Consultar Após Atualização ---");
            dao.consultarUsuarios().forEach(System.out::println);
            System.out.println();

            System.out.println("--- Operação: Deletar Usuário ---");
            //Uma vez localizado, o Hibernate dispara um comando 'DELETE FROM usuario WHERE cpf = ...' limpando o registro de forma segura.
            dao.deletarUsuario(99988877711L);
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

    // Método criado para ser possível mostrar a tabela do postgres, talvez seja um método temporário (principalmente se o toString das classes poder ser usado)
    private static void imprimirTabelaUsuarios(List<Usuario> usuarios) {
        // Linha divisória com a contagem exata de caracteres para cada coluna
        String divisoria = "+--------------+----------------------+------------+--------------------------------+----------------------+------------+";
        
        System.out.println(divisoria);
        // Cabeçalho idêntico ao tamanho limite das colunas
        System.out.printf("| %-12s | %-20s | %-10s | %-30s | %-20s | %-10s |\n", 
                "CPF", "NOME", "DATA NASC.", "EMAILS", "TELEFONES", "LOGIN");
        System.out.println(divisoria);

        if (usuarios == null || usuarios.isEmpty()) {
            System.out.printf("| %-118s |\n", "NENHUM REGISTRO ENCONTRADO NO BANCO DE DADOS");
            System.out.println(divisoria);
            return;
        }

        for (Usuario u : usuarios) {
            // Evita NullPointerException se algum dado estiver vazio no banco
            String nome = (u.getNome() != null) ? u.getNome() : "";
            String data = (u.getData_nascimento() != null) ? u.getData_nascimento().toString() : "";
            String emailsStr = (u.getEmail() != null) ? String.join(", ", u.getEmail()) : "";
            String fonesStr = (u.getTelefone() != null) ? String.join(", ", u.getTelefone()) : "";
            String login = (u.getLogin() != null) ? u.getLogin() : "";

            // Trunca o texto se ele ultrapassar o limite da coluna para não deformar a tabela
            if (nome.length() > 20) nome = nome.substring(0, 17) + "...";
            if (emailsStr.length() > 30) emailsStr = emailsStr.substring(0, 27) + "...";
            if (fonesStr.length() > 20) fonesStr = fonesStr.substring(0, 17) + "...";
            if (login.length() > 10) login = login.substring(0, 7) + "...";

            System.out.printf("| %-12d | %-20s | %-10s | %-30s | %-20s | %-10s |\n",
                    u.getCpf(), nome, data, emailsStr, fonesStr, login);
        }
        System.out.println(divisoria);
    }
}

